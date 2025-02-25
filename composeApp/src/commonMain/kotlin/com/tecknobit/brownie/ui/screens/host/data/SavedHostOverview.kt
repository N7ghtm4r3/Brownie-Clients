package com.tecknobit.brownie.ui.screens.host.data

import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.browniecore.CPU_USAGE_KEY
import com.tecknobit.browniecore.IP_ADDRESS_KEY
import com.tecknobit.browniecore.MEMORY_USAGE_KEY
import com.tecknobit.browniecore.STORAGE_USAGE_KEY
import com.tecknobit.browniecore.enums.HostStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedHostOverview(
    override val id: String,
    override val name: String,
    @SerialName(IP_ADDRESS_KEY)
    override val ipAddress: String,
    override var status: HostStatus,
    @SerialName(CPU_USAGE_KEY)
    val cpuUsage: CpuUsage,
    @SerialName(MEMORY_USAGE_KEY)
    val memoryUsage: MemoryUsage,
    @SerialName(STORAGE_USAGE_KEY)
    val storageUsage: StorageUsage,
) : SavedHost
