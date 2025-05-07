package com.tecknobit.brownie.ui.screens.hosts.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.OFFLINE
import com.tecknobit.browniecore.enums.HostStatus.ONLINE
import com.tecknobit.browniecore.enums.HostStatus.REBOOTING
import com.tecknobit.equinoxcore.helpers.HOST_ADDRESS_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `SavedHost` interface is useful to give the basic details about a Brownie's host
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface SavedHost {

    /**
     *`id` the identifier of the host
     */
    val id: String

    /**
     *`name` the name of the host
     */
    val name: String

    /**
     *`hostAddress` the address of the host
     */
    @SerialName(HOST_ADDRESS_KEY)
    val hostAddress: String

    /**
     *`status` the current status of the host
     */
    var status: HostStatus

    companion object {

        /**
         * Scoped function used to get a specific color for an [HostStatus] value
         *
         * @return the specific color for an [HostStatus] as [Color]
         */
        @Composable
        fun HostStatus.asColor(): Color {
            return when (this) {
                OFFLINE -> red()
                ONLINE -> green()
                REBOOTING -> yellow()
            }
        }

    }

    /**
     * The `SavedHostImpl` class is the implementation of the [SavedHost] interface
     *
     * @property id The identifier of the host
     * @property name The name of the host
     * @property hostAddress The address of the host
     * @property status The current status of the host
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     * @see SavedHost
     */
    @Serializable
    data class SavedHostImpl(
        override val id: String,
        override val name: String,
        @SerialName(HOST_ADDRESS_KEY)
        override val hostAddress: String,
        override var status: HostStatus,
    ) : SavedHost

}
