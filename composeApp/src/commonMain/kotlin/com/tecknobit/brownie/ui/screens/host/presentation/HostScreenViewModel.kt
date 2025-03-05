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
import com.tecknobit.browniecore.enums.ServiceEventType
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.mergeIfNotContained
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.equinoxcore.time.TimeFormatter
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.random.Random

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
                        onFailure = { setHasBeenDisconnectedValue(true) },
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
        // TODO: MAKE THE REQUEST THEN
        // TODO: TO APPLY THE FILTERS ALSO
        val e = listOf(
            HostService(
                id = Random.nextLong().toString(),
                name = "Ametista-1.0.0.jar",
                pid = Random.nextLong(1000000),
                status = ServiceStatus.entries[Random.nextInt(ServiceStatus.entries.size - 1)],
                configuration = HostService.ServiceConfiguration(
                    id = Random.nextLong().toString(),
                    purgeNohupOutAfterReboot = false
                ),
                events = if (Random.nextBoolean()) {
                    listOf(
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RESTARTED,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.REBOOTING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = 11
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RUNNING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.STOPPED,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = 11
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RUNNING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        )
                    )
                } else
                    emptyList()
            ),
            HostService(
                id = Random.nextLong().toString(),
                name = "Ametista-1.0.0.jar",
                pid = Random.nextLong(1000000),
                status = ServiceStatus.entries[Random.nextInt(ServiceStatus.entries.size - 1)],
                configuration = HostService.ServiceConfiguration(
                    id = Random.nextLong().toString(),
                    purgeNohupOutAfterReboot = false
                ),
                events = if (false && Random.nextBoolean()) {
                    listOf(
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RESTARTED,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.REBOOTING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = 11
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RUNNING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.STOPPED,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = 11
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RUNNING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        )
                    )
                } else
                    emptyList()
            ),
            HostService(
                id = Random.nextLong().toString(),
                name = "Ametista-1.0.0.jar",
                pid = Random.nextLong(1000000),
                status = ServiceStatus.entries[Random.nextInt(ServiceStatus.entries.size - 1)],
                configuration = HostService.ServiceConfiguration(
                    id = Random.nextLong().toString(),
                    purgeNohupOutAfterReboot = false
                ),
                events = if (Random.nextBoolean()) {
                    listOf(
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RESTARTED,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.REBOOTING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = 11
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RUNNING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.STOPPED,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = 11
                        ),
                        HostService.ServiceEvent(
                            id = Random.nextLong().toString(),
                            type = ServiceEventType.RUNNING,
                            eventDate = TimeFormatter.currentTimestamp(),
                            extra = Random.nextLong(100000)
                        )
                    )
                } else
                    emptyList()
            )
        )
        servicesState.appendPage(
            items = if (false && Random.nextBoolean())
                emptyList()
            else
                e, // TODO: USE THE REAL DATA,
            nextPageKey = page + 1, // TODO: USE THE REAL DATA,
            isLastPage = true || Random.nextBoolean() // TODO: USE THE REAL DATA,
        )
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
        // TODO: MAKE THE REQUEST THEN
        onStatusChange.invoke(newStatus)
        service.status = newStatus
    }

    fun rebootService(
        service: HostService,
        onStatusChange: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onStatusChange.invoke()
    }

}