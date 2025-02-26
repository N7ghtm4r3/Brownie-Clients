package com.tecknobit.brownie.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveFromQueue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.unregister_host
import brownie.composeapp.generated.resources.unregister_host_message
import com.tecknobit.brownie.displayFontFamily
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.shared.presentation.HostManager
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

/**
 * `titleStyle` the style to apply to the title of the [EquinoxAlertDialog]
 */
val titleStyle = TextStyle(
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
        titleStyle = titleStyle,
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

/*/**
 * Alert to warn about the logout action
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
@NonRestartableComposable
fun Logout(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
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
        titleStyle = titleStyle,
        text = Res.string.logout_message,
        confirmAction = {
            viewModel.clearSession {
                show.value = false
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}

/**
 * Alert to warn about the account deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
@NonRestartableComposable
fun DeleteAccount(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.account_deletion,
        titleStyle = titleStyle,
        text = Res.string.delete_message,
        confirmAction = {
            viewModel.deleteAccount(
                onDelete = {
                    show.value = false
                    navigator.navigate(SPLASHSCREEN)
                }
            )
        }
    )
}*/