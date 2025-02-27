@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.brownie.ui.screens.host.components.services

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.service_rebooted
import brownie.composeapp.generated.resources.service_started
import brownie.composeapp.generated.resources.service_started_after_rebooting
import brownie.composeapp.generated.resources.service_stopped
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.brownie.displayFontFamily
import com.tecknobit.brownie.ui.components.EventText
import com.tecknobit.brownie.ui.components.NoEventsAvailable
import com.tecknobit.brownie.ui.components.ServiceEventBadge
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.host.data.HostService.ServiceEvent
import com.tecknobit.browniecore.enums.ServiceEventType
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@NonRestartableComposable
fun ServiceHistory(
    state: SheetState,
    scope: CoroutineScope,
    service: HostService,
) {
    if (state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        bottom = 16.dp
                    ),
                text = service.name,
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            HorizontalDivider()
            val events = service.events
            if (events.isEmpty()) {
                NoEventsAvailable(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .sizeIn(
                            maxWidth = 400.dp,
                            maxHeight = 400.dp
                        )
                )
            } else {
                ServiceEvents(
                    events = events
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun ServiceEvents(
    events: List<ServiceEvent>,
) {
    JetLimeColumn(
        modifier = Modifier
            .animateContentSize(),
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
                    overlineContent = {
                        ServiceEventBadge(
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

@Composable
@NonRestartableComposable
private fun ServiceEvent.eventText() {
    EventText(
        text = when (type) {
            ServiceEventType.RUNNING -> Res.string.service_started
            ServiceEventType.STOPPED -> Res.plurals.service_stopped
            ServiceEventType.REBOOTING -> Res.plurals.service_rebooted
            ServiceEventType.RESTARTED -> Res.string.service_started_after_rebooting
        },
        eventExtra = extra
    )
}