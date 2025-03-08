package com.tecknobit.brownie.ui.screens.upsertservice.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res.string
import brownie.composeapp.generated.resources.add_service
import brownie.composeapp.generated.resources.edit_service
import brownie.composeapp.generated.resources.remove
import brownie.composeapp.generated.resources.service_name
import brownie.composeapp.generated.resources.service_name_placeholder
import brownie.composeapp.generated.resources.wrong_service_name
import com.tecknobit.brownie.ui.components.DeleteService
import com.tecknobit.brownie.ui.components.RemoveService
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.upsertservice.components.ProgramArgumentsSection
import com.tecknobit.brownie.ui.screens.upsertservice.components.ServiceSettings
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.brownie.ui.shared.presenters.UpsertScreen
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import org.jetbrains.compose.resources.stringResource

class UpsertServiceScreen(
    hostId: String,
    private val hostName: String,
    serviceId: String?,
) : UpsertScreen<HostService, UpsertServiceScreenViewModel>(
    itemId = serviceId,
    viewModel = UpsertServiceScreenViewModel(
        hostId = hostId,
        serviceId = serviceId
    )
) {

    @Composable
    @NonRestartableComposable
    override fun Title() {
        Column {
            Text(
                text = hostName,
                style = AppTypography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(
                    resource = if (isEditing)
                        string.edit_service
                    else
                        string.add_service
                ),
                style = AppTypography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    @Composable
    @NonRestartableComposable
    override fun Actions() {
        if (isEditing) {
            val deleteService = remember { mutableStateOf(false) }
            IconButton(
                onClick = { deleteService.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = red()
                )
            }
            DeleteService(
                show = deleteService,
                viewModel = viewModel
            )
        }
    }

    @Composable
    @NonRestartableComposable
    override fun Form() {
        ItemNameSection(
            header = string.service_name,
            placeholder = string.service_name_placeholder,
            errorText = string.wrong_service_name
        )
        ProgramArgumentsSection(
            viewModel = viewModel
        )
        ServiceSettings(
            viewModel = viewModel
        )
    }

    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun UpsertButtons(
        modifier: Modifier,
    ) {
        if (isEditing) {
            val removeService = remember { mutableStateOf(false) }
            Button(
                modifier = modifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = red()
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { removeService.value = true }
            ) {
                ChameleonText(
                    text = stringResource(string.remove),
                    backgroundColor = red()
                )
            }
            RemoveService(
                show = removeService,
                viewModel = viewModel
            )
        }
        super.UpsertButtons(
            modifier = modifier
        )
    }

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
        viewModel.programArguments = remember {
            mutableStateOf(
                if (isEditing)
                    item.value!!.configuration.programArguments
                else
                    ""
            )
        }
        viewModel.purgeNohupOutAfterReboot = remember {
            mutableStateOf(
                if (isEditing)
                    item.value!!.configuration.purgeNohupOutAfterReboot
                else
                    true
            )
        }
        viewModel.autoRunAfterHostReboot = remember {
            mutableStateOf(
                if (isEditing)
                    item.value!!.configuration.autoRunAfterHostReboot
                else
                    false
            )
        }
    }

}