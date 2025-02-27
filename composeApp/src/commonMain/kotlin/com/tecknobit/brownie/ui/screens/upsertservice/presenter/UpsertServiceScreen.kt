@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.upsertservice.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import brownie.composeapp.generated.resources.Res.string
import brownie.composeapp.generated.resources.add_service
import brownie.composeapp.generated.resources.edit_service
import brownie.composeapp.generated.resources.service_name
import brownie.composeapp.generated.resources.service_name_placeholder
import brownie.composeapp.generated.resources.wrong_service_name
import com.tecknobit.brownie.ui.components.DeleteService
import com.tecknobit.brownie.ui.components.ItemNameSection
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.upsertservice.components.CompactUpsertButtons
import com.tecknobit.brownie.ui.screens.upsertservice.components.ExpandedUpsertButtons
import com.tecknobit.brownie.ui.screens.upsertservice.components.ProgramArgumentsSection
import com.tecknobit.brownie.ui.screens.upsertservice.components.ServiceSettings
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.brownie.ui.shared.presenters.UpsertScreen
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import org.jetbrains.compose.resources.stringResource

class UpsertServiceScreen(
    private val hostName: String,
    serviceId: String?,
) : UpsertScreen<HostService, UpsertServiceScreenViewModel>(
    itemId = serviceId,
    viewModel = UpsertServiceScreenViewModel(
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
            viewModel = viewModel,
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
    @NonRestartableComposable
    override fun ColumnScope.ButtonsSection() {
        Column(
            modifier = Modifier
                .align(Alignment.End),
            horizontalAlignment = Alignment.End
        ) {
            ResponsiveContent(
                onExpandedSizeClass = {
                    ExpandedUpsertButtons(
                        isEditing = isEditing,
                        viewModel = viewModel
                    )
                },
                onMediumSizeClass = {
                    ExpandedUpsertButtons(
                        isEditing = isEditing,
                        viewModel = viewModel
                    )
                },
                onCompactSizeClass = {
                    CompactUpsertButtons(
                        isEditing = isEditing,
                        viewModel = viewModel
                    )
                }
            )
        }
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