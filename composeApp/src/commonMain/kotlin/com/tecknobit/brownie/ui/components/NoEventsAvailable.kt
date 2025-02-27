@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.no_events_available
import brownie.composeapp.generated.resources.undraw_events
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.components.EmptyState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun NoEventsAvailable(
    modifier: Modifier = Modifier,
) {
    EmptyState(
        containerModifier = modifier,
        resource = painterResource(Res.drawable.undraw_events),
        contentDescription = "No events available",
        resourceSize = 300.dp,
        title = stringResource(Res.string.no_events_available),
        titleStyle = AppTypography.titleLarge
    )
}