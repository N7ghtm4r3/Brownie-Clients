@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.brownie.ui.screens.host.components.services

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.tecknobit.brownie.ui.components.ServiceStatusBadge
import com.tecknobit.brownie.ui.icons.RuleSettings
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.REBOOTING

@Composable
@NonRestartableComposable
fun ServiceCard(
    viewModel: HostScreenViewModel,
    service: HostService,
) {
    val statusState = remember { mutableStateOf(service.status) }
    Card(
        onClick = {
            // TODO: NAV TO EDIT
        }
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            overlineContent = {
                ServiceStatusBadge(
                    status = statusState.value
                )
            },
            headlineContent = {
                Text(
                    text = service.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = "PID: ${service.pid}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                StatusToolbar(
                    viewModel = viewModel,
                    service = service,
                    statusState = statusState
                )
            }
        )
    }
}

@Composable
@NonRestartableComposable
private fun StatusToolbar(
    viewModel: HostScreenViewModel,
    service: HostService,
    statusState: MutableState<ServiceStatus>,
) {
    Row {
        AnimatedContent(
            targetState = statusState.value
        ) { status ->
            if (!status.isRebooting()) {
                val isRunning = status.isRunning()
                IconButton(
                    onClick = {
                        viewModel.handleServiceStatus(
                            service = service,
                            onStatusChange = { newStatus ->
                                statusState.value = newStatus
                            }
                        )
                    }
                ) {
                    Icon(
                        imageVector = if (isRunning)
                            Icons.Default.Stop
                        else
                            Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (isRunning)
                            red()
                        else
                            green()
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = statusState.value.isRunning()
        ) {
            IconButton(
                onClick = {
                    viewModel.rebootService(
                        service = service,
                        onStatusChange = { statusState.value = REBOOTING }
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = null,
                    tint = yellow()
                )
            }
        }
        IconButton(
            onClick = {
            }
        ) {
            Icon(
                imageVector = RuleSettings,
                contentDescription = null
            )
        }
    }
}