package com.tecknobit.brownie.ui.screens.host.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.violet
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.EVENT_DATE_KEY
import com.tecknobit.browniecore.PROGRAM_ARGUMENT_KEY
import com.tecknobit.browniecore.PURGE_NOHUP_OUT_AFTER_REBOOT_KEY
import com.tecknobit.browniecore.enums.ServiceEventType
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.REBOOTING
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HostService(
    val id: String,
    val name: String,
    val pid: Long,
    var status: ServiceStatus,
    val configuration: ServiceConfiguration,
    val events: List<ServiceEvent>,
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

        @Composable
        fun ServiceEventType.asColor(): Color {
            return when (this) {
                ServiceEventType.STOPPED -> red()
                ServiceEventType.RUNNING -> green()
                ServiceEventType.REBOOTING -> yellow()
                ServiceEventType.RESTARTED -> violet()
            }
        }

    }

    @Serializable
    data class ServiceConfiguration(
        val id: String,
        @SerialName(PROGRAM_ARGUMENT_KEY)
        val programArguments: String = "",
        @SerialName(PURGE_NOHUP_OUT_AFTER_REBOOT_KEY)
        val purgeNohupOutAfterReboot: Boolean,
    )

    @Serializable
    data class ServiceEvent(
        val id: String,
        val type: ServiceEventType,
        @SerialName(EVENT_DATE_KEY)
        val eventDate: Long,
        @Contextual
        val extra: Any? = null,
    )

}