package com.tecknobit.brownie.ui.screens.host.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.PURGE_NOHUP_OUT_AFTER_REBOOT_KEY
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.REBOOTING
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HostService(
    val name: String,
    val pid: Long,
    var status: ServiceStatus,
    //val type: ServiceType,
    val configuration: ServiceConfiguration,
) {

    companion object {

        @Composable
        fun ServiceStatus.asColor(): Color {
            return when (this) {
                STOPPED -> red()
                RUNNING -> green()
                REBOOTING -> yellow()
            }
        }

    }

    @Serializable
    data class ServiceConfiguration(
        @SerialName(PURGE_NOHUP_OUT_AFTER_REBOOT_KEY)
        val purgeNohupOutAfterReboot: Boolean,
    )

}