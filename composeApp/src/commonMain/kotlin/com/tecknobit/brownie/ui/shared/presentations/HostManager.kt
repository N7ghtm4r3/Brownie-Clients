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

/**
 * The `HostManager` interface is used to manage the hosts with their status and persistence in the
 * system
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface HostManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    var requestsScope: CoroutineScope

    /**
     * Method used to handle the status of the host such online or offline
     *
     * @param host The host to handle its status
     * @param onStatusChange The callback to execute when the status of the host changed
     */
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

    /**
     * Method used to reboot a host
     *
     * @param host The host to reboot
     * @param onStatusChange The callback to execute when the status of the host changed
     */
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

    /**
     * Method used to unregister a host from the session
     *
     * @param host The host to unregister
     * @param onSuccess The success callback to invoke
     */
    fun unregisterHost(
        host: SavedHost,
        onSuccess: () -> Unit,
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    unregisterHost(
                        host = host
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

    /**
     * Method used to display an eventual occurred error
     * @param error The error to display
     */
    fun showFailure(
        error: JsonObject,
    )

}