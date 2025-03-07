@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.brownie.ui.screens.hosts.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RemoveFromQueue
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.tecknobit.brownie.HOST_SCREEN
import com.tecknobit.brownie.UPSERT_HOST_SCREEN
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.components.HostStatusBadge
import com.tecknobit.brownie.ui.components.UnregisterSavedHost
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.screens.hosts.presentation.HostsScreenViewModel
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.REBOOTING

@Composable
@NonRestartableComposable
fun HostCard(
    viewModel: HostsScreenViewModel,
    host: SavedHostImpl,
) {
    val statusState = remember { mutableStateOf(host.status) }
    val refreshingHosts by viewModel.refreshingHosts.collectAsState(
        initial = false
    )
    LaunchedEffect(refreshingHosts) {
        statusState.value = host.status
    }
    Card(
        modifier = Modifier
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = { navigator.navigate("$HOST_SCREEN/${host.id}") },
                onLongClick = { navigator.navigate("$UPSERT_HOST_SCREEN/${host.id}") }
            )
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            overlineContent = {
                HostStatusBadge(
                    status = statusState.value
                )
            },
            headlineContent = {
                Text(
                    text = host.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = host.hostAddress,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                StatusToolbar(
                    viewModel = viewModel,
                    host = host,
                    statusState = statusState
                )
            }
        )
    }
}

@Composable
@NonRestartableComposable
private fun StatusToolbar(
    viewModel: HostsScreenViewModel,
    host: SavedHostImpl,
    statusState: MutableState<HostStatus>,
) {
    Row {
        AnimatedContent(
            targetState = statusState.value
        ) { status ->
            if (!status.isRebooting()) {
                val isOnline = status.isOnline()
                IconButton(
                    onClick = {
                        viewModel.handleHostStatus(
                            host = host,
                            onStatusChange = { newStatus ->
                                statusState.value = newStatus
                            }
                        )
                    }
                ) {
                    Icon(
                        imageVector = if (isOnline)
                            Icons.Default.Stop
                        else
                            Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (isOnline)
                            red()
                        else
                            green()
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = statusState.value.isOnline()
        ) {
            IconButton(
                onClick = {
                    viewModel.rebootHost(
                        host = host,
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
        val unregister = remember { mutableStateOf(false) }
        IconButton(
            onClick = { unregister.value = true }
        ) {
            Icon(
                imageVector = Icons.Default.RemoveFromQueue,
                contentDescription = null
            )
        }
        UnregisterSavedHost(
            show = unregister,
            hostManager = viewModel,
            host = host,
            onSuccess = { viewModel.hostsState.refresh() }
        )
    }
}