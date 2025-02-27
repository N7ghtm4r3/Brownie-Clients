package com.tecknobit.brownie.ui.screens.host.components.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.brownie.ui.components.HistoryEventBadge
import com.tecknobit.brownie.ui.components.NoEventsAvailable
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview.HostHistoryEvent
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString

@Composable
@NonRestartableComposable
fun HostHistory(
    hostOverview: SavedHostOverview,
) {
    val history = hostOverview.history
    if (history.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoEventsAvailable(
                modifier = Modifier
                    .sizeIn(
                        maxWidth = 400.dp,
                        maxHeight = 400.dp
                    )
            )
        }
    } else {
        HistoryEvents(
            events = history
        )
    }
}

@Composable
@NonRestartableComposable
private fun HistoryEvents(
    events: List<HostHistoryEvent>,
) {
    JetLimeColumn(
        modifier = Modifier
            .heightIn(
                max = 700.dp
            ),
        itemsList = ItemsList(
            items = events
        ),
        contentPadding = PaddingValues(
            all = 16.dp
        ),
        style = JetLimeDefaults.columnStyle(
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(30f, 30f),
                phase = 0f,
            )
        ),
        key = { _, event -> event.id }
    ) { _, event, position ->
        JetLimeEvent(
            style = JetLimeEventDefaults.eventStyle(
                position = position
            )
        ) {
            Card {
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    overlineContent = {
                        HistoryEventBadge(
                            eventType = event.type
                        )
                    },
                    headlineContent = {
                        //event.eventText()
                    },
                    supportingContent = {
                        Text(
                            text = event.eventDate.toDateString()
                        )
                    }
                )
            }
        }
    }
}

/*@Composable
@NonRestartableComposable
private fun HostHistoryEvent.eventText() {
    val text = when (this.type) {
        ServiceEventType.RUNNING -> Res.string.service_started
        ServiceEventType.STOPPED -> Res.plurals.service_stopped
        ServiceEventType.REBOOTING -> Res.plurals.service_rebooted
        ServiceEventType.RESTARTED -> Res.string.service_started_after_rebooting
    }
    Text(
        text = if (text is PluralStringResource) {
            val extra = extra as Int
            pluralStringResource(
                resource = text,
                quantity = extra,
                extra
            )
        } else {
            if (extra != null) {
                stringResource(
                    resource = text as StringResource,
                    extra
                )
            } else
                stringResource(text as StringResource)
        }
    )
}*/