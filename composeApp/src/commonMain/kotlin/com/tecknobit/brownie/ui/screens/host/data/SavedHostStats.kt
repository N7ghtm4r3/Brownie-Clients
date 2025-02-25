package com.tecknobit.brownie.ui.screens.host.data

import com.tecknobit.browniecore.PERCENT_VALUE_KEY
import com.tecknobit.browniecore.STORAGE_TYPE_KEY
import com.tecknobit.browniecore.TOTAL_VALUE_KEY
import com.tecknobit.browniecore.USAGE_VALUE_KEY
import com.tecknobit.browniecore.enums.HostStatType
import com.tecknobit.browniecore.enums.HostStatType.CPU_USAGE
import com.tecknobit.browniecore.enums.HostStatType.MEMORY_USAGE
import com.tecknobit.browniecore.enums.HostStatType.STORAGE_USAGE
import com.tecknobit.browniecore.enums.StorageType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SavedHostStats {
    val type: HostStatType

    @SerialName(USAGE_VALUE_KEY)
    val usageValue: Double

    @SerialName(TOTAL_VALUE_KEY)
    val totalValue: Long

    @SerialName(PERCENT_VALUE_KEY)
    val percentValue: Double

    @Serializable
    data class SavedHostStatsImpl(
        override val type: HostStatType,
        @SerialName(USAGE_VALUE_KEY)
        override val usageValue: Double,
        @SerialName(TOTAL_VALUE_KEY)
        override val totalValue: Long,
        @SerialName(PERCENT_VALUE_KEY)
        override val percentValue: Double,
    ) : SavedHostStats

}

@Serializable
data class CpuUsage(
    @SerialName(USAGE_VALUE_KEY)
    override val usageValue: Double,
    val clock: Double,
) : SavedHostStats {

    override val type: HostStatType = CPU_USAGE

    override val totalValue: Long = 100

    override val percentValue: Double = usageValue

}

@Serializable
data class MemoryUsage(
    @SerialName(USAGE_VALUE_KEY)
    override val usageValue: Double,
    @SerialName(TOTAL_VALUE_KEY)
    override val totalValue: Long,
    @SerialName(PERCENT_VALUE_KEY)
    override val percentValue: Double,
) : SavedHostStats {

    override val type: HostStatType = MEMORY_USAGE

}

@Serializable
data class StorageUsage(
    @SerialName(USAGE_VALUE_KEY)
    override val usageValue: Double,
    @SerialName(TOTAL_VALUE_KEY)
    override val totalValue: Long,
    @SerialName(PERCENT_VALUE_KEY)
    override val percentValue: Double,
    @SerialName(STORAGE_TYPE_KEY)
    val storageType: StorageType,
) : SavedHostStats {

    override val type: HostStatType = STORAGE_USAGE

}

