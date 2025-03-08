package com.tecknobit.brownie.ui.screens.host.data

import com.tecknobit.browniecore.PERCENT_VALUE_KEY
import com.tecknobit.browniecore.TOTAL_VALUE_KEY
import com.tecknobit.browniecore.USAGE_VALUE_KEY
import com.tecknobit.browniecore.enums.StorageType
import com.tecknobit.browniecore.enums.StorageType.SSD
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `SavedHostStats` interface is useful to contains the statistic about a host
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
sealed interface SavedHostStats {

    /**
     *`usageValue` the current usage of a resource of the host
     */
    @SerialName(USAGE_VALUE_KEY)
    val usageValue: Double

    /**
     *`totalValue` the total amount of the resource available of the host
     */
    @SerialName(TOTAL_VALUE_KEY)
    val totalValue: Double

    /**
     *`percentValue` the current percent value of the resource usage
     */
    @SerialName(PERCENT_VALUE_KEY)
    val percentValue: Double

    /**
     * The `SavedHostStatsImpl` class is the implementation of the [SavedHostStats] interface
     *
     * @property usageValue The current usage of a resource of the host
     * @property totalValue The total amount of the resource available of the host
     * @property percentValue The current percent value of the resource usage
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     * @see SavedHostStats
     */
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

/**
 * The `CpuUsage` class represents the statistic related to the current **CPU** usage
 *
 * @property usageValue The current usage of a resource of the host
 * @property clock The current clock of the **CPU**
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see SavedHostStats
 */
@Serializable
data class CpuUsage(
    @SerialName(USAGE_VALUE_KEY)
    override val usageValue: Double,
    val clock: Double,
) : SavedHostStats {

    /**
     *`totalValue` the total amount of the resource available of the host
     */
    @SerialName(TOTAL_VALUE_KEY)
    override val totalValue: Double = 100.0

    /**
     *`percentValue` the current percent value of the resource usage
     */
    @SerialName(PERCENT_VALUE_KEY)
    override val percentValue: Double = usageValue

}

/**
 * The `MemoryUsage` class represents the statistic related to the current **Memory** usage
 *
 * @property usageValue The current usage of a resource of the host
 * @property totalValue The total amount of the resource available of the host
 * @property percentValue The current percent value of the resource usage
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see SavedHostStats
 */
@Serializable
data class MemoryUsage(
    @SerialName(USAGE_VALUE_KEY)
    override val usageValue: Double,
    @SerialName(TOTAL_VALUE_KEY)
    override val totalValue: Double,
    @SerialName(PERCENT_VALUE_KEY)
    override val percentValue: Double,
) : SavedHostStats

/**
 * The `StorageUsage` class represents the statistic related to the current **storage** usage
 *
 * @property usageValue The current usage of a resource of the host
 * @property totalValue The total amount of the resource available of the host
 * @property percentValue The current percent value of the resource usage
 * @property type The type of the storage unit of the host
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see SavedHostStats
 */
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

        /**
         * Method used to convert the [StorageType] value as displayable UI text
         *
         * @return the converted text as [String]
         */
        fun StorageType.asText(): String {
            if (this == SSD)
                return SSD.name
            return this.name
                .replace("_", " ")
                .lowercase()
        }

    }

}

