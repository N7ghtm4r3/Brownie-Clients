package com.tecknobit.brownie.ui.screens.host.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.ui.screens.host.data.CpuUsage
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.host.data.MemoryUsage
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.data.StorageUsage
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.brownie.ui.shared.presentation.HostManager
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import com.tecknobit.browniecore.enums.StorageType
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class HostScreenViewModel(
    hostId: String,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), HostManager {

    override var requestsScope: CoroutineScope = viewModelScope

    private val _hostOverview = MutableStateFlow<SavedHostOverview?>(
        value = null
    )
    val hostOverview = _hostOverview.asStateFlow()

    fun retrieveHostOverview() {
        retrieve(
            currentContext = HostScreen::class,
            routine = {
                // TODO: REMOVE THIS WORKAROUND WHEN ROUTINE SUSPENDABLE
                viewModelScope.launch {
                    // TODO: MAKE THE REQUEST THEN
                    _hostOverview.value = SavedHostOverview(
                        id = Random.nextLong().toString(),
                        name = "Debian 11",
                        ipAddress = "192.168.1.1",
                        status = HostStatus.entries[Random.nextInt(3)],
                        cpuUsage = CpuUsage(
                            Random.nextInt(100).toDouble(),
                            clock = 4.57
                        ),
                        memoryUsage = MemoryUsage(
                            usageValue = 15.6,
                            totalValue = 32,
                            Random.nextInt(100).toDouble(),
                        ),
                        storageUsage = StorageUsage(
                            usageValue = 444.0,
                            totalValue = 4000,
                            Random.nextInt(100).toDouble(),
                            storageType = StorageType.entries[Random.nextInt(2)]
                        )
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
                name = "Ametista-1.0.0.jar",
                pid = Random.nextLong(1000000),
                status = ServiceStatus.entries[Random.nextInt(ServiceStatus.entries.size)],
                configuration = HostService.ServiceConfiguration(
                    purgeNohupOutAfterReboot = false
                )
            )
        )
        servicesState.appendPage(
            items = if (Random.nextBoolean())
                emptyList()
            else
                e, // TODO: USE THE REAL DATA,
            nextPageKey = page + 1, // TODO: USE THE REAL DATA,
            isLastPage = true || Random.nextBoolean() // TODO: USE THE REAL DATA,
        )
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