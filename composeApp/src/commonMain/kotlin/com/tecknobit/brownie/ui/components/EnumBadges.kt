package com.tecknobit.brownie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.brownie.ui.screens.host.data.HostService.Companion.asColor
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview.Companion.asColor
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.Companion.asColor
import com.tecknobit.browniecore.enums.HostEventType
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.ServiceEventType
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcore.annotations.Wrapper

/**
 * Custom component used to represent the [HostStatus] as badge
 *
 * @param status The status to represent
 */
@Wrapper
@Composable
@NonRestartableComposable
fun HostStatusBadge(
    status: HostStatus,
) {
    EnumBadge(
        color = status.asColor(),
        name = status.name
    )
}

/**
 * Custom component used to represent the [ServiceStatus] as badge
 *
 * @param status The status to represent
 */
@Wrapper
@Composable
@NonRestartableComposable
fun ServiceStatusBadge(
    status: ServiceStatus,
) {
    EnumBadge(
        color = status.asColor(),
        name = status.name
    )
}

/**
 * Custom component used to represent the [ServiceEventType] as badge
 *
 * @param eventType The event type to represent
 */
@Wrapper
@Composable
@NonRestartableComposable
fun ServiceEventBadge(
    eventType: ServiceEventType,
) {
    EnumBadge(
        color = eventType.asColor(),
        name = eventType.name
    )
}

/**
 * Custom component used to represent the [HostEventType] as badge
 *
 * @param eventType The event type to represent
 */
@Wrapper
@Composable
@NonRestartableComposable
fun HistoryEventBadge(
    eventType: HostEventType,
) {
    EnumBadge(
        color = eventType.asColor(),
        name = eventType.name.replace("_", " ")
    )
}

/**
 * Custom component used to represent an [Enum] value as badge
 *
 * @param color The color of the badge
 * @param name The name to display in the badge
 */
@Composable
@NonRestartableComposable
private fun EnumBadge(
    color: Color,
    name: String,
) {
    ChameleonText(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(
                horizontal = 4.dp
            ),
        text = name,
        backgroundColor = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}