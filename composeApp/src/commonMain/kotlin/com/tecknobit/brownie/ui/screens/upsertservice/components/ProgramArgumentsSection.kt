package com.tecknobit.brownie.ui.screens.upsertservice.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.programs_arguments
import brownie.composeapp.generated.resources.programs_arguments_placeholder
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField

/**
 * Section where the user can insert the program arguments of the service
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@ScreenSection
fun ProgramArgumentsSection(
    viewModel: UpsertServiceScreenViewModel
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                vertical = 10.dp
            ),
        title = Res.string.programs_arguments,
        fontSize = 18.sp
    )
    EquinoxOutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        value = viewModel.programArguments,
        placeholder = Res.string.programs_arguments_placeholder,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}