@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.brownie.ui.screens.host.components.services

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.tecknobit.brownie.UPSERT_SERVICE_SCREEN
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.components.ServiceStatusBadge
import com.tecknobit.brownie.ui.icons.RuleSettings
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.HOST_IDENTIFIER_KEY
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.REBOOTING
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import kotlinx.coroutines.launch

/**
 * Custom [Card] used to display the service information
 *
 * @param viewModel The support viewmodel for the screen
 * @param savedHostOverview The host overview data
 * @param service The service to display
 */
@Composable
fun ServiceCard(
    viewModel: HostScreenViewModel,
    savedHostOverview: SavedHostOverview,
    service: HostService
) {
    val statusState = remember { mutableStateOf(service.status) }
    val pidState = remember { mutableStateOf(service.pid) }
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    val refreshingServices by viewModel.refreshingServices.collectAsState(
        initial = false
    )
    LaunchedEffect(refreshingServices) {
        statusState.value = service.status
        pidState.value = service.pid
    }
    Card(
        onClick = {
            scope.launch {
                state.show()
            }
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
                    text = "PID: ${pidState.value}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                StatusToolbar(
                    viewModel = viewModel,
                    savedHostOverview = savedHostOverview,
                    service = service,
                    statusState = statusState
                )
            }
        )
    }
    ServiceHistory(
        state = state,
        scope = scope,
        service = service
    )
}

/**
 * The toolbar of the [ServiceCard] component used to handle the service status
 *
 * @param viewModel The support viewmodel for the screen
 * @param savedHostOverview The host overview data
 * @param service The service to display
 * @param statusState The state container used to trigger the UI when the status changed
 */
@Composable
private fun StatusToolbar(
    viewModel: HostScreenViewModel,
    savedHostOverview: SavedHostOverview,
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
        AnimatedVisibility(
            visible = !statusState.value.isRebooting()
        ) {
            IconButton(
                onClick = {
                    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
                    savedStateHandle?.let {
                        savedStateHandle[HOST_IDENTIFIER_KEY] = savedHostOverview.id
                        savedStateHandle[NAME_KEY] = savedHostOverview.name
                        savedStateHandle[IDENTIFIER_KEY] = service.id
                    }
                    navigator.navigate(UPSERT_SERVICE_SCREEN)
                }
            ) {
                Icon(
                    imageVector = RuleSettings,
                    contentDescription = null
                )
            }
        }
    }
}