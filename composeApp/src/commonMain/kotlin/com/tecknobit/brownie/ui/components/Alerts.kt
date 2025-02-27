package com.tecknobit.brownie.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.RemoveFromQueue
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.delete_message
import brownie.composeapp.generated.resources.delete_service
import brownie.composeapp.generated.resources.delete_service_message
import brownie.composeapp.generated.resources.delete_session
import brownie.composeapp.generated.resources.logout
import brownie.composeapp.generated.resources.logout_message
import brownie.composeapp.generated.resources.remove_service
import brownie.composeapp.generated.resources.remove_service_message
import brownie.composeapp.generated.resources.unregister_host
import brownie.composeapp.generated.resources.unregister_host_message
import com.tecknobit.brownie.SPLASHSCREEN
import com.tecknobit.brownie.displayFontFamily
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.screens.adminpanel.presentation.AdminPanelScreenViewModel
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.brownie.ui.shared.presentations.HostManager
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

/**
 * `alertTitleStyle` the style to apply to the title of the [EquinoxAlertDialog]
 */
val alertTitleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 20.sp
)

@Composable
@NonRestartableComposable
fun UnregisterSavedHost(
    show: MutableState<Boolean>,
    hostManager: HostManager,
    host: SavedHost,
    onSuccess: () -> Unit,
) {
    EquinoxAlertDialog(
        icon = Icons.Default.RemoveFromQueue,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = (hostManager) as EquinoxViewModel,
        show = show,
        title = Res.string.unregister_host,
        titleStyle = alertTitleStyle,
        text = Res.string.unregister_host_message,
        confirmAction = {
            hostManager.unregisterHost(
                savedHost = host,
                onSuccess = {
                    show.value = false
                    onSuccess()
                }
            )
        }
    )
}

@Composable
@NonRestartableComposable
fun RemoveService(
    show: MutableState<Boolean>,
    viewModel: UpsertServiceScreenViewModel,
) {
    EquinoxAlertDialog(
        icon = Icons.Default.PowerOff,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.remove_service,
        titleStyle = alertTitleStyle,
        text = Res.string.remove_service_message,
        confirmAction = {
            viewModel.removeService(
                onSuccess = {
                    show.value = false
                }
            )
        }
    )
}

@Composable
@NonRestartableComposable
fun DeleteService(
    show: MutableState<Boolean>,
    viewModel: UpsertServiceScreenViewModel,
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Warning,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.delete_service,
        titleStyle = alertTitleStyle,
        text = Res.string.delete_service_message,
        confirmAction = {
            viewModel.deleteService(
                onSuccess = { show.value = false }
            )
        }
    )
}

/**
 * Alert to warn about the logout action
 *
 * @param show Whether the alert is shown
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@NonRestartableComposable
fun Logout(
    show: MutableState<Boolean>,
    viewModel: AdminPanelScreenViewModel,
) {
    EquinoxAlertDialog(
        icon = Icons.AutoMirrored.Filled.Logout,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.logout,
        titleStyle = alertTitleStyle,
        text = Res.string.logout_message,
        confirmAction = {
            viewModel.clearSession(
                onClear = {
                    show.value = false
                    navigator.navigate(SPLASHSCREEN)
                }
            )
        }
    )
}

/**
 * Alert to warn about the session deletion action
 *
 * @param show Whether the alert is shown
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@NonRestartableComposable
fun DeleteSession(
    show: MutableState<Boolean>,
    viewModel: AdminPanelScreenViewModel,
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.delete_session,
        titleStyle = alertTitleStyle,
        text = Res.string.delete_message,
        confirmAction = {
            viewModel.deleteSession(
                onClear = {
                    show.value = false
                    navigator.navigate(SPLASHSCREEN)
                }
            )
        }
    )
}