package com.tecknobit.brownie.ui.screens.host.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.ui.screens.host.data.CpuUsage
import com.tecknobit.brownie.ui.screens.host.data.MemoryUsage
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.data.StorageUsage
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.StorageType
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class HostScreenViewModel(
    hostId: String,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _hostOverview = MutableStateFlow<SavedHostOverview?>(
        value = null
    )
    val hostOverview = _hostOverview.asStateFlow()

    fun retrieveHostOverview() {
        retrieve(
            currentContext = HostScreen::class,
            routine = {
                viewModelScope.launch {
                    // TODO: MAKE THE REQUEST THEN
                    _hostOverview.value = SavedHostOverview(
                        id = Random.nextLong().toString(),
                        name = "Debian 11",
                        ipAddress = "192.168.1.1",
                        status = HostStatus.ONLINE,
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

}