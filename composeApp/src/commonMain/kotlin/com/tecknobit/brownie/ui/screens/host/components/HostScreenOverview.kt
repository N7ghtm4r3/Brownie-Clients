package com.tecknobit.brownie.ui.screens.host.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.brownie.ui.screens.host.components.actions.ActionsSection
import com.tecknobit.brownie.ui.screens.host.components.history.HistorySection
import com.tecknobit.brownie.ui.screens.host.components.services.ServicesSection
import com.tecknobit.brownie.ui.screens.host.components.stats.StatsSection
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel

/**
 * Custom component used to display the overview data related to a host
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param hostOverview The overview data of the host
 *
 */
@Composable
@NonRestartableComposable
fun HostOverview(
    modifier: Modifier,
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                bottom = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .widthIn(
                    max = 1000.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            StatsSection(
                hostOverview = hostOverview
            )
            ActionsSection(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
            ServicesSection(
                viewModel = viewModel,
                savedHostOverview = hostOverview
            )
            HistorySection(
                savedHostOverview = hostOverview
            )
        }
    }
}
