@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.upsertservice.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res.string
import brownie.composeapp.generated.resources.edit
import brownie.composeapp.generated.resources.remove
import brownie.composeapp.generated.resources.save
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
fun ExpandedUpsertButtons(
    isEditing: Boolean,
    viewModel: UpsertServiceScreenViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        UpsertButtons(
            isEditing = isEditing,
            viewModel = viewModel
        )
    }
}

@Composable
@CompactClassComponent
@NonRestartableComposable
fun CompactUpsertButtons(
    isEditing: Boolean,
    viewModel: UpsertServiceScreenViewModel,
) {
    Column {
        UpsertButtons(
            modifier = Modifier
                .fillMaxWidth(),
            isEditing = isEditing,
            viewModel = viewModel
        )
    }
}

@Composable
@NonRestartableComposable
private fun UpsertButtons(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    viewModel: UpsertServiceScreenViewModel,
) {
    if (isEditing) {
        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = red()
            ),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            onClick = { viewModel.removeService() }
        ) {
            ChameleonText(
                text = stringResource(string.remove),
                backgroundColor = red()
            )
        }
    }
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = { viewModel.upsert() }
    ) {
        Text(
            text = stringResource(
                if (isEditing)
                    string.edit
                else
                    string.save
            )
        )
    }
}