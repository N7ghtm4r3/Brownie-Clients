package com.tecknobit.brownie.ui.screens.host.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.brownie.ui.shared.presentations.HostManager
import com.tecknobit.browniecore.PID_KEY
import com.tecknobit.browniecore.STATUS_KEY
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.json.treatsAsLong
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.equinoxcore.mergeIfNotContained
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseArrayData
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject

class HostScreenViewModel(
    private val hostId: String,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), HostManager {

    override var requestsScope: CoroutineScope = MainScope()

    private val _hostOverview = MutableStateFlow<SavedHostOverview?>(
        value = null
    )
    val hostOverview = _hostOverview.asStateFlow()

    lateinit var servicesQuery: MutableState<String>

    val statusFilters = mutableStateListOf<ServiceStatus>()

    init {
        statusFilters.addAll(ServiceStatus.entries)
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val servicesState = PaginationState<Int, HostService>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadServices(
                page = page
            )
        }
    )

    private val _refreshingServices = MutableSharedFlow<Boolean>(
        replay = 1
    )
    val refreshingServices = _refreshingServices.asSharedFlow()

    fun retrieveHostOverview() {
        retrieve(
            currentContext = HostScreen::class,
            routine = {
                // TODO: REMOVE THIS WORKAROUND WHEN ROUTINE SUSPENDABLE
                viewModelScope.launch {
                    requester.sendRequest(
                        request = {
                            getHostOverview(
                                hostId = hostId
                            )
                        },
                        onSuccess = {
                            setServerOfflineValue(false)
                            _hostOverview.value = json.decodeFromJsonElement(it.toResponseData())
                        },
                        onFailure = { showSnackbarMessage(it) },
                        onConnectionError = { setServerOfflineValue(true) }
                    )
                    requester.sendRequest(
                        request = {
                            getServicesStatus(
                                hostId = hostId,
                                currentServices = servicesState.allItems
                            )
                        },
                        onSuccess = {
                            viewModelScope.launch {
                                setServerOfflineValue(false)
                                _refreshingServices.emit(true)
                                val services = it.toResponseArrayData()
                                services.forEach { serviceEntry ->
                                    updateServiceStatus(
                                        serviceInfo = serviceEntry.jsonObject
                                    )
                                }
                                delay(100)
                                _refreshingServices.emit(false)
                            }
                        },
                        onFailure = { showSnackbarMessage(it) },
                        onConnectionError = { setServerOfflineValue(true) }
                    )
                }
            },
            refreshDelay = 3000
        )
    }

    private fun updateServiceStatus(
        serviceInfo: JsonObject,
    ) {
        val serviceId = serviceInfo[IDENTIFIER_KEY].treatsAsString()
        val status = ServiceStatus.valueOf(
            value = serviceInfo[STATUS_KEY].treatsAsString()
        )
        val pid = serviceInfo[PID_KEY].treatsAsLong(
            defValue = 0 // TODO: REMOVE DEF VALUE WHEN RENAMING DONE
        )
        val service = servicesState.allItems?.find { service -> service.id == serviceId }
        service?.let {
            service.status = status
            service.pid = pid
        }
    }

    private fun loadServices(
        page: Int,
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getServices(
                        hostId = hostId,
                        page = page,
                        keywords = servicesQuery.value,
                        statuses = statusFilters
                    )
                },
                serializer = HostService.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    servicesState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = {
                    showSnackbarMessage(it)
                },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    fun clearFilters(
        onClear: () -> Unit,
    ) {
        servicesQuery.value = ""
        statusFilters.mergeIfNotContained(
            collectionToMerge = ServiceStatus.entries
        )
        restartRetriever()
        onClear()
    }

    fun applyFilters(
        onApply: () -> Unit,
    ) {
        onApply()
        restartRetriever()
        servicesState.refresh()
    }

    fun handleServiceStatus(
        service: HostService,
        onStatusChange: (ServiceStatus) -> Unit,
    ) {
        val newStatus = if (service.status.isRunning())
            STOPPED
        else
            RUNNING
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    handleServiceStatus(
                        hostId = hostId,
                        service = service,
                        newStatus = newStatus
                    )
                },
                onSuccess = {
                    onStatusChange.invoke(newStatus)
                    service.status = newStatus
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun rebootService(
        service: HostService,
        onStatusChange: () -> Unit,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    rebootService(
                        hostId = hostId,
                        service = service,
                    )
                },
                onSuccess = { onStatusChange.invoke() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    override fun showFailure(
        error: JsonObject,
    ) {
        showSnackbarMessage(
            response = error
        )
    }

}