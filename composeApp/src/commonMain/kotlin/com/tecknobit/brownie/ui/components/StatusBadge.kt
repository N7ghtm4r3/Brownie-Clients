package com.tecknobit.brownie.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.Companion.asColor
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.equinoxcompose.components.ChameleonText

@Composable
@NonRestartableComposable
fun StatusBadge(
    status: HostStatus,
) {
    val color = status.asColor()
    ChameleonText(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(
                horizontal = 4.dp
            ),
        text = status.name,
        backgroundColor = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}