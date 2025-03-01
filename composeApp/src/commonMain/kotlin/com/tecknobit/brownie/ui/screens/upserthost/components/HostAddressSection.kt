package com.tecknobit.brownie.ui.screens.upserthost.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.host_address
import brownie.composeapp.generated.resources.host_address_placeholder
import brownie.composeapp.generated.resources.wrong_host_address
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.upserthost.presentation.UpsertHostScreenViewModel
import com.tecknobit.browniecore.helpers.BrownieInputsValidator.isHostAddressValid
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField

@Composable
@NonRestartableComposable
fun HostAddressSection(
    viewModel: UpsertHostScreenViewModel,
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                vertical = 10.dp
            ),
        title = Res.string.host_address,
        fontSize = 18.sp
    )
    EquinoxOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        value = viewModel.address,
        isError = viewModel.addressError,
        placeholder = Res.string.host_address_placeholder,
        errorText = Res.string.wrong_host_address,
        allowsBlankSpaces = false,
        validator = { isHostAddressValid(it) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}