package com.tecknobit.brownie.ui.screens.host.components.history

import androidx.compose.animation.animateContentSize
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
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.host_rebooted
import brownie.composeapp.generated.resources.host_started
import brownie.composeapp.generated.resources.host_started_after_rebooting
import brownie.composeapp.generated.resources.host_stopped
import brownie.composeapp.generated.resources.service_added
import brownie.composeapp.generated.resources.service_deleted
import brownie.composeapp.generated.resources.service_removed
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.brownie.ui.components.EventText
import com.tecknobit.brownie.ui.components.HistoryEventBadge
import com.tecknobit.brownie.ui.components.NoEventsAvailable
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview.HostHistoryEvent
import com.tecknobit.browniecore.enums.HostEventType.OFFLINE
import com.tecknobit.browniecore.enums.HostEventType.ONLINE
import com.tecknobit.browniecore.enums.HostEventType.REBOOTING
import com.tecknobit.browniecore.enums.HostEventType.RESTARTED
import com.tecknobit.browniecore.enums.HostEventType.SERVICE_ADDED
import com.tecknobit.browniecore.enums.HostEventType.SERVICE_DELETED
import com.tecknobit.browniecore.enums.HostEventType.SERVICE_REMOVED
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString

/**
 * Custom component used to display the events related to the host lifecycle
 *
 * @param hostOverview The host overview data
 */
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

/**
 * Component used to display the events timeline
 *
 * @param events The events related to the host lifecycle
 */
@Composable
@NonRestartableComposable
private fun HistoryEvents(
    events: List<HostHistoryEvent>,
) {
    JetLimeColumn(
        modifier = Modifier
            .heightIn(
                max = 700.dp
            )
            .animateContentSize(),
        itemsList = ItemsList(
            items = events
        ),
        contentPadding = PaddingValues(
            top = 12.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
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
                        event.eventText()
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

/**
 * Custom component used to display a [HostHistoryEvent] information
 */
@Composable
@NonRestartableComposable
private fun HostHistoryEvent.eventText() {
    EventText(
        text = when (type) {
            ONLINE -> Res.string.host_started
            OFFLINE -> Res.plurals.host_stopped
            REBOOTING -> Res.plurals.host_rebooted
            RESTARTED -> Res.string.host_started_after_rebooting
            SERVICE_ADDED -> Res.string.service_added
            SERVICE_REMOVED -> Res.string.service_removed
            SERVICE_DELETED -> Res.string.service_deleted
        },
        eventExtra = extra
    )
}