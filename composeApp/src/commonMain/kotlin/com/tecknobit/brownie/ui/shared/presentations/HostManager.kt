package com.tecknobit.brownie.ui.shared.presentations

import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.OFFLINE
import com.tecknobit.browniecore.enums.HostStatus.ONLINE
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    unregisterHost(
                        host = savedHost
                    )
                },
                onSuccess = { onSuccess() },
                onFailure = {}
            )
        }
    }

}