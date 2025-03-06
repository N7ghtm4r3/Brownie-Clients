package com.tecknobit.brownie.ui.shared.presentations

import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.OFFLINE
import com.tecknobit.browniecore.enums.HostStatus.ONLINE
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

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
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    handleHostStatus(
                        host = host,
                        newStatus = newStatus
                    )
                },
                onSuccess = {
                    onStatusChange.invoke(newStatus)
                    host.status = newStatus
                },
                onFailure = {
                    showFailure(
                        error = it
                    )
                }
            )
        }
    }

    fun rebootHost(
        host: SavedHost,
        onStatusChange: () -> Unit,
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    rebootHost(
                        host = host
                    )
                },
                onSuccess = { onStatusChange.invoke() },
                onFailure = {
                    showFailure(
                        error = it
                    )
                }
            )
        }
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
                onFailure = {
                    showFailure(
                        error = it
                    )
                }
            )
        }
    }

    fun showFailure(
        error: JsonObject,
    )

}