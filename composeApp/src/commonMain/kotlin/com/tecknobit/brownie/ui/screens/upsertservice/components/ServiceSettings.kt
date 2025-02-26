package com.tecknobit.brownie.ui.screens.upsertservice.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.auto_run_after_host_reboot
import brownie.composeapp.generated.resources.auto_run_after_host_reboot_description
import brownie.composeapp.generated.resources.purge_nohup_out_after_reboot
import brownie.composeapp.generated.resources.purge_nohup_out_after_reboot_description
import brownie.composeapp.generated.resources.settings
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun ServiceSettings(
    viewModel: UpsertServiceScreenViewModel,
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                top = 10.dp
            ),
        title = Res.string.settings,
        fontSize = 18.sp
    )
    SettingCheckBox(
        settingState = viewModel.purgeNohupOutAfterReboot,
        settingTitle = Res.string.purge_nohup_out_after_reboot,
        settingDescription = Res.string.purge_nohup_out_after_reboot_description
    )
    SettingCheckBox(
        settingState = viewModel.autoRunAfterHostReboot,
        settingTitle = Res.string.auto_run_after_host_reboot,
        settingDescription = Res.string.auto_run_after_host_reboot_description
    )
}

@Composable
@NonRestartableComposable
private fun SettingCheckBox(
    settingState: MutableState<Boolean>,
    settingTitle: StringResource,
    settingDescription: StringResource,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = settingState.value,
            onCheckedChange = { settingState.value = it }
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(settingTitle)
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(settingDescription)
                )
            }
        )
    }
}