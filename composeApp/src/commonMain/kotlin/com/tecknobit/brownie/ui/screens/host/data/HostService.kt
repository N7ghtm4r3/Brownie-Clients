package com.tecknobit.brownie.ui.screens.host.data

import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceType
import kotlinx.serialization.Serializable

@Serializable
data class HostService(
    val name: String,
    val pid: Long,
    val status: ServiceStatus,
    val type: ServiceType,
)