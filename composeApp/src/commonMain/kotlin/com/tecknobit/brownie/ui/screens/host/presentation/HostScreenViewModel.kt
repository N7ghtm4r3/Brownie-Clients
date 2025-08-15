@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.brownie.ui.screens.host.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.brownie.ui.shared.presentations.HostManager
import com.tecknobit.browniecore.PID_KEY
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.STATUS_KEY
import com.tecknobit.equinoxcore.json.treatsAsLong
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseArrayData
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.network.sendRequestAsyncHandlers
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

/**
 * The `HostScreenViewModel` class is the support class used by the
 * [com.tecknobit.brownie.ui.screens.host.presenter.HostScreen] screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see HostManager
 *
 */
class HostScreenViewModel(
    private val hostId: String,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), HostManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    override var requestsScope: CoroutineScope = MainScope()

    /**
     *`_hostOverview` the host overview data
     */
    private val _hostOverview = MutableStateFlow<SavedHostOverview?>(
        value = null
    )
    val hostOverview = _hostOverview.asStateFlow()

    /**
     *`servicesQuery` the query used to filter the services
     */
    lateinit var servicesQuery: MutableState<String>

    /**
     *`statusFilters` the list of the statuses used to filter the hosts result
     */
    val statusFilters = mutableStateListOf<ServiceStatus>()

    init {
        statusFilters.addAll(ServiceStatus.entries)
    }

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    lateinit var sessionFlowState: SessionFlowState

    /**
     * `json` the json instance used to ignore the unknown keys during the serialization
     */
    private val json = Json {
        ignoreUnknownKeys = true
    }

    /**
     *`servicesState` the state used to handle the pagination of the services
     */
    val servicesState = PaginationState<Int, HostService>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadServices(
                page = page
            )
        }
    )

    /**
     * Method used to load and retrieve the services of the host
     * @param page The page to request
     */
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
                    sessionFlowState.notifyOperational()
                    servicesState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { showSnackbarMessage(it) },
                onConnectionError = {
                    servicesState.setError(Exception())
                    sessionFlowState.notifyServerOffline()
                }
            )
        }
    }

    /**
     *`_refreshingServices` shared state used to refresh the services statuses
     */
    private val _refreshingServices = MutableSharedFlow<Boolean>(
        replay = 1
    )
    val refreshingServices = _refreshingServices.asSharedFlow()

    /**
     * Method used to retrieve the current host overview data
     */
    fun retrieveHostOverview() {
        retrieve(
            currentContext = HostScreen::class,
            routine = {
                requester.sendRequest(
                    request = {
                        getHostOverview(
                            hostId = hostId
                        )
                    },
                    onSuccess = {
                        sessionFlowState.notifyOperational()
                        _hostOverview.value = json.decodeFromJsonElement(it.toResponseData())
                    },
                    onFailure = { showSnackbarMessage(it) },
                    onConnectionError = { sessionFlowState.notifyServerOffline() }
                )
                requester.sendRequestAsyncHandlers(
                    request = {
                        getServicesStatus(
                            hostId = hostId,
                            currentServices = servicesState.allItems
                        )
                    },
                    onSuccess = {
                        sessionFlowState.notifyOperational()
                        _refreshingServices.emit(true)
                        val services = it.toResponseArrayData()
                        services.forEach { serviceEntry ->
                            updateServiceStatus(
                                serviceInfo = serviceEntry.jsonObject
                            )
                        }
                        delay(100)
                        _refreshingServices.emit(false)
                    },
                    onFailure = { showSnackbarMessage(it) },
                    onConnectionError = { sessionFlowState.notifyServerOffline() }
                )
            },
            refreshDelay = 3000
        )
    }

    /**
     * Method used to update the status of a service
     *
     * @param serviceInfo The updated information of the service
     */
    private fun updateServiceStatus(
        serviceInfo: JsonObject,
    ) {
        val serviceId = serviceInfo[IDENTIFIER_KEY].treatsAsString()
        val status = ServiceStatus.valueOf(
            value = serviceInfo[STATUS_KEY].treatsAsString()
        )
        val pid = serviceInfo[PID_KEY].treatsAsLong()
        val service = servicesState.allItems?.find { service -> service.id == serviceId }
        service?.let {
            service.status = status
            service.pid = pid
        }
    }

    /**
     * Method to clear the current set filters
     *
     * @param onClear The callback to invoke after the filters cleared
     */
    fun clearFilters(
        onClear: () -> Unit,
    ) {
        servicesQuery.value = ""
        restartRetriever()
        onClear()
    }

    /**
     * Method used to apply the selected statuses and refresh the current [servicesState] list
     *
     * @param onApply The callback invoked when the statuses have been applied
     */
    fun applyFilters(
        onApply: () -> Unit,
    ) {
        onApply()
        restartRetriever()
        servicesState.refresh()
    }

    /**
     * Method used to handle the status of the service such running or stopped
     *
     * @param service The host to handle its status
     * @param onStatusChange The callback to execute when the status of the service changed
     */
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

    /**
     * Method used to reboot a service
     *
     * @param service The service to reboot
     * @param onStatusChange The callback to execute when the status of the service changed
     */
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

    /**
     * Method used to display an eventual occurred error
     * @param error The error to display
     */
    override fun showFailure(
        error: JsonObject,
    ) {
        showSnackbarMessage(
            response = error
        )
    }

}