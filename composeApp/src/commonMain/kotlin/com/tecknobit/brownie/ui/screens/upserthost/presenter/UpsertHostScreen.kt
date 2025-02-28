package com.tecknobit.brownie.ui.screens.upserthost.presenter

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import brownie.composeapp.generated.resources.Res.string
import brownie.composeapp.generated.resources.edit_host
import brownie.composeapp.generated.resources.host_name
import brownie.composeapp.generated.resources.host_name_placeholder
import brownie.composeapp.generated.resources.register_host
import brownie.composeapp.generated.resources.wrong_host_name
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.screens.upserthost.components.HostAddressSection
import com.tecknobit.brownie.ui.screens.upserthost.components.SSHAuthentication
import com.tecknobit.brownie.ui.screens.upserthost.presentation.UpsertHostScreenViewModel
import com.tecknobit.brownie.ui.shared.presenters.UpsertScreen
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import org.jetbrains.compose.resources.stringResource

class UpsertHostScreen(
    hostId: String?,
) : UpsertScreen<SavedHost, UpsertHostScreenViewModel>(
    itemId = hostId,
    viewModel = UpsertHostScreenViewModel(
        hostId = hostId
    )
) {

    @Composable
    @NonRestartableComposable
    override fun Title() {
        Text(
            text = stringResource(
                resource = if (isEditing)
                    string.edit_host
                else
                    string.register_host
            ),
            style = AppTypography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    @Composable
    @NonRestartableComposable
    override fun Form() {
        ItemNameSection(
            viewModel = viewModel,
            header = string.host_name,
            placeholder = string.host_name_placeholder,
            errorText = string.wrong_host_name
        )
        HostAddressSection(
            viewModel = viewModel
        )
        SSHAuthentication(
            viewModel = viewModel,
            isEditing = isEditing
        )
    }

    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStates() {
        super.CollectStates()
        viewModel.addressError = remember { mutableStateOf(false) }
        viewModel.sshUser = remember { mutableStateOf("") }
        viewModel.sshUserError = remember { mutableStateOf(false) }
        viewModel.sshPassword = remember { mutableStateOf("") }
        viewModel.sshPasswordError = remember { mutableStateOf(false) }
    }

    @Composable
    @NonRestartableComposable
    override fun CollectStatesAfterLoading() {
        viewModel.name = remember {
            mutableStateOf(
                if (isEditing)
                    item.value!!.name
                else
                    ""
            )
        }
        viewModel.address = remember {
            mutableStateOf(
                if (isEditing)
                    item.value!!.hostAddress
                else
                    ""
            )
        }
    }

}