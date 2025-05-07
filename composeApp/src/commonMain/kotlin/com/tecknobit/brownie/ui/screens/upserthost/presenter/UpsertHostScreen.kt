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
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.screens.upserthost.components.HostAddressSection
import com.tecknobit.brownie.ui.screens.upserthost.components.SSHAuthentication
import com.tecknobit.brownie.ui.screens.upserthost.presentation.UpsertHostScreenViewModel
import com.tecknobit.brownie.ui.shared.presenters.UpsertScreen
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import org.jetbrains.compose.resources.stringResource

/**
 * The [UpsertHostScreen] class is useful to handle the insertion or editing of a host
 *
 * @param hostId The identifier of the host to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see UpsertScreen
 */
class UpsertHostScreen(
    hostId: String?,
) : UpsertScreen<SavedHostImpl, UpsertHostScreenViewModel>(
    itemId = hostId,
    viewModel = UpsertHostScreenViewModel(
        hostId = hostId
    )
) {

    /**
     * Custom section used to display the title of the item
     */
    @Composable
    @ScreenSection
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

    /**
     * The form used to insert or edit the item details
     */
    @Composable
    @ScreenSection
    @NonRestartableComposable
    override fun Form() {
        ItemNameSection(
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

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        viewModel.addressError = remember { mutableStateOf(false) }
        viewModel.sshUser = remember { mutableStateOf("") }
        viewModel.sshUserError = remember { mutableStateOf(false) }
        viewModel.sshPassword = remember { mutableStateOf("") }
        viewModel.sshPasswordError = remember { mutableStateOf(false) }
    }

    /**
     * Method to collect or instantiate the states of the screen after a loading required to correctly assign an
     * initial value to the states
     */
    @Composable
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