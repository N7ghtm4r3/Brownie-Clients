@file:OptIn(ExperimentalComposeUiApi::class)

package com.tecknobit.brownie.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import com.tecknobit.brownie.ui.screens.host.data.HostService.Companion.asColor
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview.Companion.asColor
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.Companion.asColor
import com.tecknobit.browniecore.enums.HostEventType
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.ServiceEventType
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.equinoxcompose.components.BadgeText

/**
 * Custom component used to represent the [HostStatus] as badge
 *
 * @param status The status to represent
 */
@Composable
fun HostStatusBadge(
    status: HostStatus,
) {
    BadgeText(
        badgeColor = status.asColor(),
        badgeText = status.name,
        shape = RoundedCornerShape(4.dp)
    )
}

/**
 * Custom component used to represent the [ServiceStatus] as badge
 *
 * @param status The status to represent
 */
@Composable
fun ServiceStatusBadge(
    status: ServiceStatus,
) {
    BadgeText(
        badgeColor = status.asColor(),
        badgeText = status.name,
        shape = RoundedCornerShape(4.dp)
    )
}

/**
 * Custom component used to represent the [ServiceEventType] as badge
 *
 * @param eventType The event type to represent
 */
@Composable
fun ServiceEventBadge(
    eventType: ServiceEventType,
) {
    BadgeText(
        badgeColor = eventType.asColor(),
        badgeText = eventType.name.replace("_", " "),
        shape = RoundedCornerShape(4.dp)
    )
}

/**
 * Custom component used to represent the [HostEventType] as badge
 *
 * @param eventType The event type to represent
 */
@Composable
fun HistoryEventBadge(
    eventType: HostEventType,
) {
    BadgeText(
        badgeColor = eventType.asColor(),
        badgeText = eventType.name.replace("_", " "),
        shape = RoundedCornerShape(4.dp)
    )
}