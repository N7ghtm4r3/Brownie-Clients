@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.services
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent

@Composable
@NonRestartableComposable
fun ServicesSection(
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            FixedServicesSection(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        },
        onMediumSizeClass = {
            FixedServicesSection(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        },
        onCompactSizeClass = {
            ExpandableServicesSection(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        }
    )
}

@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun FixedServicesSection(
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview,
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp
            ),
        title = Res.string.services
    )
    ActionsContent(
        modifier = Modifier
            .padding(
                top = 12.dp
            ),
        viewModel = viewModel,
        hostOverview = hostOverview
    )
}

@Composable
@CompactClassComponent
@NonRestartableComposable
private fun ExpandableServicesSection(
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview,
) {
    ExpandableSection(
        title = Res.string.services,
        content = {
            ActionsContent(
                viewModel = viewModel,
                hostOverview = hostOverview
            )
        }
    )
}

@Composable
@NonRestartableComposable
private fun ActionsContent(
    modifier: Modifier = Modifier,
    viewModel: HostScreenViewModel,
    hostOverview: SavedHostOverview,
) {
}
