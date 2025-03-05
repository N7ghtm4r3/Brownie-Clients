package com.tecknobit.brownie.ui.screens.host.data

import com.tecknobit.browniecore.PERCENT_VALUE_KEY
import com.tecknobit.browniecore.TOTAL_VALUE_KEY
import com.tecknobit.browniecore.USAGE_VALUE_KEY
import com.tecknobit.browniecore.enums.StorageType
import com.tecknobit.browniecore.enums.StorageType.SSD
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SavedHostStats {

    @SerialName(USAGE_VALUE_KEY)
    val usageValue: Double

    @SerialName(TOTAL_VALUE_KEY)
    val totalValue: Double

    @SerialName(PERCENT_VALUE_KEY)
    val percentValue: Double

    @Serializable
    data class SavedHostStatsImpl(
        @SerialName(USAGE_VALUE_KEY)
        override val usageValue: Double,
        @SerialName(TOTAL_VALUE_KEY)
        override val totalValue: Double,
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

    @SerialName(TOTAL_VALUE_KEY)
    override val totalValue: Double = 100.0

    @SerialName(PERCENT_VALUE_KEY)
    override val percentValue: Double = usageValue

}

@Serializable
data class MemoryUsage(
    @SerialName(USAGE_VALUE_KEY)
    override val usageValue: Double,
    @SerialName(TOTAL_VALUE_KEY)
    override val totalValue: Double,
    @SerialName(PERCENT_VALUE_KEY)
    override val percentValue: Double,
) : SavedHostStats

@Serializable
data class StorageUsage(
    @SerialName(USAGE_VALUE_KEY)
    override val usageValue: Double,
    @SerialName(TOTAL_VALUE_KEY)
    override val totalValue: Double,
    @SerialName(PERCENT_VALUE_KEY)
    override val percentValue: Double,
    val type: StorageType,
) : SavedHostStats {
    companion object {

        fun StorageType.asText(): String {
            if (this == SSD)
                return SSD.name
            return this.name
                .replace("_", " ")
                .lowercase()
        }

    }

}

