@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.connect.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.app_version
import brownie.composeapp.generated.resources.connect
import brownie.composeapp.generated.resources.connect_to_session
import brownie.composeapp.generated.resources.create
import brownie.composeapp.generated.resources.create_session
import brownie.composeapp.generated.resources.github
import brownie.composeapp.generated.resources.host_address
import brownie.composeapp.generated.resources.host_address_not_valid
import brownie.composeapp.generated.resources.join_code
import brownie.composeapp.generated.resources.password
import brownie.composeapp.generated.resources.server_secret
import brownie.composeapp.generated.resources.server_secret_not_valid
import brownie.composeapp.generated.resources.undraw_enter
import brownie.composeapp.generated.resources.wrong_join_code
import brownie.composeapp.generated.resources.wrong_password
import com.tecknobit.brownie.CloseApplicationOnNavBack
import com.tecknobit.brownie.displayFontFamily
import com.tecknobit.brownie.ui.screens.connect.presentation.ConnectScreenViewModel
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcore.helpers.InputsValidator
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isServerSecretValid
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [ConnectScreen] class is used to connect to an existing session or create a new one
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
class ConnectScreen : EquinoxScreen<ConnectScreenViewModel>(
    viewModel = ConnectScreenViewModel()
) {

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        BrownieTheme {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState!!) },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    HeaderSection()
                    FormSection()
                }
            }
        }
    }

    /**
     * Method used to create the header section of the activity
     */
    @Composable
    private fun HeaderSection() {
        Column(
            modifier = Modifier
                .height(125.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val uriHandler = LocalUriHandler.current
                IconButton(
                    onClick = {
                        uriHandler.openUri(
                            uri = "https://github.com/N7ghtm4r3/Brownie-Clients"
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.github),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                Text(
                    text = "v. ${stringResource(Res.string.app_version)}",
                    fontFamily = displayFontFamily,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }

    /**
     * Method used to create the form where the user can fill it with his credentials
     */
    @Composable
    @ScreenSection
    private fun FormSection() {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 16.dp
                )
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EmptyState(
                containerModifier = Modifier
                    .size(190.dp),
                resourceSize = 175.dp,
                resource = painterResource(Res.drawable.undraw_enter),
                contentDescription = "Connect to Brownie",
                title = ""
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
                EquinoxOutlinedTextField(
                    value = viewModel.host,
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    label = stringResource(Res.string.host_address),
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.host_address_not_valid),
                    isError = viewModel.hostError,
                    validator = { InputsValidator.isHostValid(it) }
                )
                EquinoxOutlinedTextField(
                    value = viewModel.serverSecret,
                    shape = RoundedCornerShape(
                        size = 10.dp
                    ),
                    label = stringResource(Res.string.server_secret),
                    keyboardOptions = keyboardOptions,
                    errorText = stringResource(Res.string.server_secret_not_valid),
                    isError = viewModel.serverSecretError,
                    validator = { isServerSecretValid(it) },
                )
                AnimatedVisibility(
                    visible = viewModel.connecting.value
                ) {
                    EquinoxOutlinedTextField(
                        value = viewModel.joinCode,
                        shape = RoundedCornerShape(
                            size = 10.dp
                        ),
                        label = stringResource(Res.string.join_code),
                        keyboardOptions = keyboardOptions,
                        errorText = stringResource(Res.string.wrong_join_code),
                        isError = viewModel.joinCodeError,
                        validator = { it.isNotEmpty() },
                    )
                }
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
                val softwareKeyboardController = LocalSoftwareKeyboardController.current
                Button(
                    modifier = Modifier
                        .padding(
                            top = 10.dp
                        )
                        .height(45.dp)
                        .width(300.dp),
                    shape = CardDefaults.shape,
                    onClick = {
                        softwareKeyboardController?.hide()
                        viewModel.connect()
                    }
                ) {
                    Text(
                        text = stringResource(
                            if (viewModel.connecting.value)
                                Res.string.connect
                            else
                                Res.string.create
                        )
                    )
                }
                TextButton(
                    onClick = { viewModel.connecting.value = !viewModel.connecting.value }
                ) {
                    Text(
                        text = stringResource(
                            if (viewModel.connecting.value)
                                Res.string.create_session
                            else
                                Res.string.connect_to_session
                        )
                    )
                }
            }
        }
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.connecting = remember { mutableStateOf(true) }
        viewModel.host = remember { mutableStateOf("") }
        viewModel.hostError = remember { mutableStateOf(false) }
        viewModel.serverSecret = remember { mutableStateOf("") }
        viewModel.serverSecretError = remember { mutableStateOf(false) }
        viewModel.joinCode = remember { mutableStateOf("") }
        viewModel.joinCodeError = remember { mutableStateOf(false) }
        viewModel.password = remember { mutableStateOf("") }
        viewModel.passwordError = remember { mutableStateOf(false) }
    }

}