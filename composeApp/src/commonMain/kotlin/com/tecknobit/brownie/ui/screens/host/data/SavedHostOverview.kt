package com.tecknobit.brownie.ui.screens.host.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.serviceAddedColor
import com.tecknobit.brownie.ui.theme.serviceDeletedColor
import com.tecknobit.brownie.ui.theme.serviceRemovedColor
import com.tecknobit.brownie.ui.theme.violet
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.CPU_USAGE_KEY
import com.tecknobit.browniecore.EVENT_DATE_KEY
import com.tecknobit.browniecore.HOST_ADDRESS_KEY
import com.tecknobit.browniecore.MEMORY_USAGE_KEY
import com.tecknobit.browniecore.STORAGE_USAGE_KEY
import com.tecknobit.browniecore.enums.HostEventType
import com.tecknobit.browniecore.enums.HostEventType.OFFLINE
import com.tecknobit.browniecore.enums.HostEventType.ONLINE
import com.tecknobit.browniecore.enums.HostEventType.REBOOTING
import com.tecknobit.browniecore.enums.HostEventType.RESTARTED
import com.tecknobit.browniecore.enums.HostEventType.SERVICE_ADDED
import com.tecknobit.browniecore.enums.HostEventType.SERVICE_DELETED
import com.tecknobit.browniecore.enums.HostEventType.SERVICE_REMOVED
import com.tecknobit.browniecore.enums.HostStatus
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedHostOverview(
    override val id: String,
    override val name: String,
    @SerialName(HOST_ADDRESS_KEY)
    override val hostAddress: String,
    override var status: HostStatus,
    @SerialName(CPU_USAGE_KEY)
    val cpuUsage: CpuUsage,
    @SerialName(MEMORY_USAGE_KEY)
    val memoryUsage: MemoryUsage,
    @SerialName(STORAGE_USAGE_KEY)
    val storageUsage: StorageUsage,
    val history: List<HostHistoryEvent> = emptyList(),
) : SavedHost {

    companion object {

        @Composable
        fun HostEventType.asColor(): Color {
            return when (this) {
                ONLINE -> green()
                OFFLINE -> red()
                REBOOTING -> yellow()
                RESTARTED -> violet()
                SERVICE_ADDED -> serviceAddedColor()
                SERVICE_REMOVED -> serviceRemovedColor()
                SERVICE_DELETED -> serviceDeletedColor()
            }
        }

    }

    @Serializable
    data class HostHistoryEvent(
        val id: String,
        val type: HostEventType,
        @SerialName(EVENT_DATE_KEY)
        val eventDate: Long,
        @Contextual
        val extra: Any? = null,
    )

}
