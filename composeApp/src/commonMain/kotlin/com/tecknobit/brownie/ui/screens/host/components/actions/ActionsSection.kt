@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components.actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.actions
import brownie.composeapp.generated.resources.reboot
import brownie.composeapp.generated.resources.server_is_currently
import brownie.composeapp.generated.resources.start
import brownie.composeapp.generated.resources.stop
import com.tecknobit.brownie.ui.components.HostStatusBadge
import com.tecknobit.brownie.ui.screens.host.components.ExpandableSection
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel
import com.tecknobit.brownie.ui.theme.green
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.brownie.ui.theme.yellow
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import org.jetbrains.compose.resources.stringResource

/**
 * The control section to handle the host status
 *
 * @param viewModel The support viewmodel for the screen
 * @param hostOverview The overview data of the host
 */
@Composable
@ScreenSection
@LayoutCoordinator
fun ActionsSection(
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            FixedActionsSection(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        },
        onMediumSizeClass = {
            FixedActionsSection(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        },
        onCompactSizeClass = {
            ExpandableActionsSection(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        }
    )
}

/**
 * The control section to handle the host status displayed on the large screen size classes
 *
 * @param viewModel The support viewmodel for the screen
 * @param hostOverview The overview data of the host
 */
@Composable
@ScreenSection
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun FixedActionsSection(
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp
            ),
        title = Res.string.actions
    )
    ActionsContent(
        modifier = Modifier
            .padding(
                top = 12.dp
            ),
        viewModel = viewModel,
        hostOverview = hostOverview
    )
}

/**
 * The control section to handle the host status displayed on the compact screen size classes
 *
 * @param viewModel The support viewmodel for the screen
 * @param hostOverview The overview data of the host
 */
@Composable
@ScreenSection
@CompactClassComponent
@NonRestartableComposable
private fun ExpandableActionsSection(
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview
) {
    ExpandableSection(
        title = Res.string.actions,
        content = {
            ActionsContent(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        }
    )
}

/**
 * The content of the control section to handle the host status
 *
 * @param modifier The modifier to apply to the content
 * @param viewModel The support viewmodel for the screen
 * @param hostOverview The overview data of the host
 */
@Composable
private fun ActionsContent(
    modifier: Modifier = Modifier,
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview
) {
    val statusState = remember { mutableStateOf(hostOverview.status) }
    LaunchedEffect(hostOverview) {
        statusState.value = hostOverview.status
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp
            )
    ) {
        Row(
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(Res.string.server_is_currently)
            )
            HostStatusBadge(
                status = statusState.value
            )
        }
        AnimatedVisibility(
            visible = !statusState.value.isRebooting()
        ) {

            Row(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ActionButtons(
                    viewModel = viewModel,
                    hostOverview = hostOverview,
                    statusState = statusState
                )
            }
        }
    }
}

/**
 * The buttons used to handle the host status
 *
 * @param buttonModifier The modifier to apply to the button
 * @param viewModel The support viewmodel for the screen
 * @param hostOverview The overview data of the host
 * @param statusState The state container used to trigger the UI when the status changed
 */
@Composable
private fun ActionButtons(
    buttonModifier: Modifier = Modifier,
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview,
    statusState: MutableState<HostStatus>,
) {
    if (statusState.value.isOnline()) {
        Row {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = red()
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = {
                    viewModel.handleHostStatus(
                        host = hostOverview,
                        onStatusChange = { newStatus ->
                            statusState.value = newStatus
                        }
                    )
                }
            ) {
                ChameleonText(
                    text = stringResource(Res.string.stop),
                    backgroundColor = red()
                )
            }
            Button(
                modifier = buttonModifier
                    .padding(
                        start = 10.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellow()
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = {
                    viewModel.rebootHost(
                        host = hostOverview,
                        onStatusChange = { statusState.value = HostStatus.REBOOTING }
                    )
                }
            ) {
                ChameleonText(
                    text = stringResource(Res.string.reboot),
                    backgroundColor = yellow()
                )
            }
        }
    } else {
        Button(
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = green()
            ),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            onClick = {
                viewModel.handleHostStatus(
                    host = hostOverview,
                    onStatusChange = { newStatus ->
                        statusState.value = newStatus
                    }
                )
            }
        ) {
            ChameleonText(
                text = stringResource(Res.string.start),
                backgroundColor = green()
            )
        }
    }
}