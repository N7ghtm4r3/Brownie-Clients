package com.tecknobit.brownie.ui.screens.upsertservice.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.service_name
import brownie.composeapp.generated.resources.service_name_placeholder
import brownie.composeapp.generated.resources.wrong_service_name
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.browniecore.helpers.BrownieInputsValidator
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField

@Composable
@NonRestartableComposable
fun ServiceNameSection(
    viewModel: UpsertServiceScreenViewModel,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    SectionTitle(
        modifier = Modifier
            .padding(
                bottom = 10.dp
            ),
        title = Res.string.service_name,
        fontSize = 18.sp
    )
    EquinoxOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        value = viewModel.serviceName,
        isError = viewModel.serviceNameError,
        validator = { BrownieInputsValidator.isServiceNameValid(it) },
        placeholder = Res.string.service_name_placeholder,
        errorText = Res.string.wrong_service_name,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
}