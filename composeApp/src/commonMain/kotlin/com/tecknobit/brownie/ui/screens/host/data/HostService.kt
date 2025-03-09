package com.tecknobit.brownie.ui.screens.host.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.violet
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.AUTO_RUN_AFTER_HOST_REBOOT_KEY
import com.tecknobit.browniecore.EVENT_DATE_KEY
import com.tecknobit.browniecore.INSERTION_DATE_KEY
import com.tecknobit.browniecore.PROGRAM_ARGUMENTS_KEY
import com.tecknobit.browniecore.PURGE_NOHUP_OUT_AFTER_REBOOT_KEY
import com.tecknobit.browniecore.SERVICE_EVENTS_KEY
import com.tecknobit.browniecore.enums.ServiceEventType
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.REBOOTING
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.enums.ServiceStatus.STOPPED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * The `HostService` class represent the information about a service
 *
 * @property id The identifier of the service
 * @property name The name of the service
 * @property pid The pid of the service
 * @property status The status of the service
 * @property configuration The configuration data of the service
 * @property events The events related to the service lifecycle
 * @property insertionDate The date when the service has been registered
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class HostService(
    val id: String,
    val name: String,
    var pid: Long,
    var status: ServiceStatus,
    val configuration: ServiceConfiguration,
    @SerialName(SERVICE_EVENTS_KEY)
    val events: List<ServiceEvent> = emptyList(),
    @SerialName(INSERTION_DATE_KEY)
    val insertionDate: Long,
) {

    companion object {

        /**
         * Scoped function used to get a specific color for an [ServiceStatus] value
         *
         * @return the specific color for an [ServiceStatus] as [Color]
         */
        @Composable
        fun ServiceStatus.asColor(): Color {
            return when (this) {
                STOPPED -> red()
                RUNNING -> green()
                REBOOTING -> yellow()
            }
        }

        /**
         * Scoped function used to get a specific color for an [ServiceEventType] value
         *
         * @return the specific color for an [ServiceEventType] as [Color]
         */
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

    /**
     * The `ServiceConfiguration` class represent the configuration of a service
     *
     * @property id The identifier of the configuration
     * @property programArguments The arguments of the program
     * @property purgeNohupOutAfterReboot Whether the `nohup.out` file related to the service
     * must be deleted at each service start
     * @property autoRunAfterHostReboot Whether the service must be automatically restarted after the
     * host start or the host restart
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    @Serializable
    data class ServiceConfiguration(
        val id: String,
        @SerialName(PROGRAM_ARGUMENTS_KEY)
        val programArguments: String = "",
        @SerialName(PURGE_NOHUP_OUT_AFTER_REBOOT_KEY)
        val purgeNohupOutAfterReboot: Boolean = true,
        @SerialName(AUTO_RUN_AFTER_HOST_REBOOT_KEY)
        val autoRunAfterHostReboot: Boolean = false,
    )

    /**
     * The `ServiceEvent` class represent the information related to a service event
     *
     * @property id The identifier of the event
     * @property type The type of the event
     * @property eventDate The date when the event occurred
     * @property extra The extra information related to an event to display
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    @Serializable
    data class ServiceEvent(
        val id: String,
        val type: ServiceEventType,
        @SerialName(EVENT_DATE_KEY)
        val eventDate: Long,
        val extra: JsonElement? = null,
    )

}