package com.tecknobit.brownie.ui.screens.upserthost.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.ssh_authentication
import brownie.composeapp.generated.resources.ssh_authentication_description
import brownie.composeapp.generated.resources.ssh_field_edit_placeholder
import brownie.composeapp.generated.resources.ssh_password_placeholder
import brownie.composeapp.generated.resources.ssh_user_placeholder
import brownie.composeapp.generated.resources.wrong_ssh_password
import brownie.composeapp.generated.resources.wrong_ssh_user
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.upserthost.presentation.UpsertHostScreenViewModel
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import org.jetbrains.compose.resources.stringResource

/**
 * Section used to allow the user to insert SSH credentials to register a remote host
 *
 * @param viewModel The support viewmodel for the screen
 * @param isEditing Whether the operation is an edit or a new registration
 */
@Composable
fun SSHAuthentication(
    viewModel: UpsertHostScreenViewModel,
    isEditing: Boolean
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                top = 10.dp
            ),
        title = Res.string.ssh_authentication,
        fontSize = 18.sp
    )
    Text(
        text = stringResource(Res.string.ssh_authentication_description),
        style = AppTypography.bodyMedium
    )
    SSHUser(
        viewModel = viewModel,
        isEditing = isEditing
    )
    SSHPassword(
        viewModel = viewModel,
        isEditing = isEditing
    )
}

/**
 * Section used to allow the user to insert SSH user
 *
 * @param viewModel The support viewmodel for the screen
 * @param isEditing Whether the operation is an edit or a new registration
 */
@Composable
private fun SSHUser(
    viewModel: UpsertHostScreenViewModel,
    isEditing: Boolean
) {
    Text(
        text = stringResource(Res.string.ssh_user_placeholder),
        style = AppTypography.titleMedium
    )
    EquinoxOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        value = viewModel.sshUser,
        isError = viewModel.sshUserError,
        placeholder = if (isEditing)
            Res.string.ssh_field_edit_placeholder
        else
            Res.string.ssh_user_placeholder,
        errorText = Res.string.wrong_ssh_user,
        allowsBlankSpaces = false,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
}

/**
 * Section used to allow the user to insert SSH password
 *
 * @param viewModel The support viewmodel for the screen
 * @param isEditing Whether the operation is an edit or a new registration
 */
@Composable
private fun SSHPassword(
    viewModel: UpsertHostScreenViewModel,
    isEditing: Boolean
) {
    Text(
        text = stringResource(Res.string.ssh_password_placeholder),
        style = AppTypography.titleMedium
    )
    var hiddenPassword by remember { mutableStateOf(true) }
    EquinoxOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        value = viewModel.sshPassword,
        isError = viewModel.sshPasswordError,
        placeholder = if (isEditing)
            Res.string.ssh_field_edit_placeholder
        else
            Res.string.ssh_password_placeholder,
        errorText = Res.string.wrong_ssh_password,
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
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}