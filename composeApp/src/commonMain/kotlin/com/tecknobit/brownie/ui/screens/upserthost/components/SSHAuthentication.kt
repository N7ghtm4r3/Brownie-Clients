package com.tecknobit.brownie.ui.screens.upserthost.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
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

@Composable
@NonRestartableComposable
fun SSHAuthentication(
    viewModel: UpsertHostScreenViewModel,
    isEditing: Boolean,
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

@Composable
@NonRestartableComposable
private fun SSHUser(
    viewModel: UpsertHostScreenViewModel,
    isEditing: Boolean,
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

@Composable
@NonRestartableComposable
private fun SSHPassword(
    viewModel: UpsertHostScreenViewModel,
    isEditing: Boolean,
) {
    Text(
        text = stringResource(Res.string.ssh_password_placeholder),
        style = AppTypography.titleMedium
    )
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
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}