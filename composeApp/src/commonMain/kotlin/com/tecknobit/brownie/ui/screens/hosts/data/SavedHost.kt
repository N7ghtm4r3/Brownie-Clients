package com.tecknobit.brownie.ui.screens.hosts.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.IP_ADDRESS_KEY
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.OFFLINE
import com.tecknobit.browniecore.enums.HostStatus.ONLINE
import com.tecknobit.browniecore.enums.HostStatus.REBOOTING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedHost(
    val id: String,
    val name: String,
    @SerialName(IP_ADDRESS_KEY)
    val ipAddress: String,
    var status: HostStatus,
) {

    companion object {

        @Composable
        fun HostStatus.asColor(): Color {
            return when (this) {
                OFFLINE -> red()
                ONLINE -> green()
                REBOOTING -> yellow()
            }
        }

    }

}
