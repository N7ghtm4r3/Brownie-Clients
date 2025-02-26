package com.tecknobit.brownie.ui.screens.host.components.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.upsertservice.UpsertServiceScreenViewModel
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField

@Composable
@NonRestartableComposable
fun ServiceSettings(
    viewModel: UpsertServiceScreenViewModel,
    service: HostService,
) {
    Column {
        viewModel.programArguments =
            remember { mutableStateOf(service.configuration.programArguments) }
        viewModel.purgeNohupOutAfterReboot =
            remember { mutableStateOf(service.configuration.purgeNohupOutAfterReboot) }
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),// .focusRequester(focusRequester),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            value = viewModel.programArguments,
            placeholder = "Program arguments",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.purgeNohupOutAfterReboot.value,
                onCheckedChange = {
                    viewModel.purgeNohupOutAfterReboot.value = it
                }
            )
            Text(
                text = "purgeNohupOutAfterReboot"
            )
        }
    }
}