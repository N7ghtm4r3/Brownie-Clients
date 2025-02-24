package com.tecknobit.brownie.ui.screens.hosts.data

import com.tecknobit.browniecore.IP_ADDRESS_KEY
import com.tecknobit.browniecore.enums.HostStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavedHost(
    val id: String,
    val name: String,
    @SerialName(IP_ADDRESS_KEY)
    val ipAddress: String,
    val status: HostStatus,
)
