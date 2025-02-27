package com.tecknobit.brownie.ui.components

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
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.browniecore.helpers.BrownieInputsValidator
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import org.jetbrains.compose.resources.StringResource

@Composable
@NonRestartableComposable
fun ItemNameSection(
    viewModel: UpsertScreenViewModel<*>,
    header: StringResource,
    placeholder: StringResource,
    errorText: StringResource,
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
        title = header,
        fontSize = 18.sp
    )
    EquinoxOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        value = viewModel.name,
        isError = viewModel.nameError,
        validator = { BrownieInputsValidator.isItemNameValid(it) },
        placeholder = placeholder,
        errorText = errorText,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )
}