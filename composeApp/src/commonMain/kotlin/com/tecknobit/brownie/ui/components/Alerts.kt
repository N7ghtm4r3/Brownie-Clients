package com.tecknobit.brownie.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.RemoveFromQueue
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.delete_message
import brownie.composeapp.generated.resources.delete_service
import brownie.composeapp.generated.resources.delete_service_message
import brownie.composeapp.generated.resources.delete_session
import brownie.composeapp.generated.resources.logout
import brownie.composeapp.generated.resources.logout_message
import brownie.composeapp.generated.resources.password
import brownie.composeapp.generated.resources.remove_service
import brownie.composeapp.generated.resources.remove_service_message
import brownie.composeapp.generated.resources.unregister_host
import brownie.composeapp.generated.resources.unregister_host_message
import brownie.composeapp.generated.resources.wrong_password
import com.tecknobit.brownie.SPLASHSCREEN
import com.tecknobit.brownie.displayFontFamily
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.screens.adminpanel.presentation.AdminPanelScreenViewModel
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.brownie.ui.shared.presentations.HostManager
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.helpers.InputsValidator
import org.jetbrains.compose.resources.stringResource

/**
 * `alertTitleStyle` the style to apply to the title of the [EquinoxAlertDialog]
 */
val alertTitleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 20.sp
)

/**
 * Alert to warn about the un-registration of a host
 *
 * @param show Whether the alert is shown
 * @param hostManager The host manager support instance
 * @param host The host to unregister
 * @param onSuccess The success callback to invoke
 */
@Composable
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
                host = host,
                onSuccess = {
                    show.value = false
                    onSuccess()
                }
            )
        }
    )
}

/**
 * Alert to warn about the removing of a service
 *
 * @param show Whether the alert is shown
 * @param viewModel The support viewmodel for the screen
 */
@Composable
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

/**
 * Alert to warn about the deletion of a service
 *
 * @param show Whether the alert is shown
 * @param viewModel The support viewmodel for the screen
 */
@Composable
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
        text = {
            viewModel.password = remember { mutableStateOf("") }
            viewModel.passwordError = remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(Res.string.delete_message),
                    textAlign = TextAlign.Justify
                )
                var hiddenPassword by remember { mutableStateOf(true) }
                EquinoxOutlinedTextField(
                    value = viewModel.password,
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    label = stringResource(Res.string.password),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    allowsBlankSpaces = false,
                    trailingIcon = {
                        IconButton(
                            onClick = { hiddenPassword = !hiddenPassword }
                        ) {
                            Icon(
                                imageVector = if (hiddenPassword)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (hiddenPassword)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,
                    errorText = stringResource(Res.string.wrong_password),
                    isError = viewModel.passwordError,
                    validator = { InputsValidator.isPasswordValid(it) },
                )
            }
        },
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