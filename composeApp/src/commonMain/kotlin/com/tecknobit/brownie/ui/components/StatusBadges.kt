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
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.Companion.asColor
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.ServiceEventType
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.equinoxcompose.components.ChameleonText

@Composable
@NonRestartableComposable
fun HostStatusBadge(
    status: HostStatus,
) {
    StatusBadge(
        color = status.asColor(),
        name = status.name
    )
}

@Composable
@NonRestartableComposable
fun ServiceStatusBadge(
    status: ServiceStatus,
) {
    StatusBadge(
        color = status.asColor(),
        name = status.name
    )
}

@Composable
@NonRestartableComposable
fun ServiceEventBadge(
    eventType: ServiceEventType,
) {
    StatusBadge(
        color = eventType.asColor(),
        name = eventType.name
    )
}

@Composable
@NonRestartableComposable
private fun StatusBadge(
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