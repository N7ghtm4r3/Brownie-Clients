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
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.mergeIfNotContained
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class HostScreenViewModel(
    private val hostId: String,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), HostManager {

    override var requestsScope: CoroutineScope = viewModelScope

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
                }
            },
            refreshDelay = 3000
        )
    }

    val servicesState = PaginationState<Int, HostService>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadServices(
                page = page
            )
        }
    )

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