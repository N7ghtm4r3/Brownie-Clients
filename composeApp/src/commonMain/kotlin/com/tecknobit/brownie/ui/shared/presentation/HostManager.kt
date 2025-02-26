package com.tecknobit.brownie.ui.shared.presentation

import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.OFFLINE
import com.tecknobit.browniecore.enums.HostStatus.ONLINE
import kotlinx.coroutines.CoroutineScope

interface HostManager {

    var requestsScope: CoroutineScope

    fun handleHostStatus(
        host: SavedHost,
        onStatusChange: (HostStatus) -> Unit,
    ) {
        val newStatus = if (host.status.isOnline())
            OFFLINE
        else
            ONLINE
        // TODO: MAKE THE REQUEST THEN
        onStatusChange.invoke(newStatus)
        host.status = newStatus
    }

    fun rebootHost(
        host: SavedHost,
        onStatusChange: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onStatusChange.invoke()
    }

    fun unregisterHost(
        savedHost: SavedHost,
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess()
    }

}