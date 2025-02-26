@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components.services

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.services
import com.tecknobit.brownie.ui.screens.host.components.ExpandableSection
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
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
    savedHostOverview: SavedHostOverview,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            FixedServicesSection(
                viewModel = viewModel,
                savedHostOverview = savedHostOverview
            )
        },
        onMediumSizeClass = {
            FixedServicesSection(
                viewModel = viewModel,
                savedHostOverview = savedHostOverview
            )
        },
        onCompactSizeClass = {
            ExpandableServicesSection(
                viewModel = viewModel,
                savedHostOverview = savedHostOverview
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
    savedHostOverview: SavedHostOverview,
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp
            ),
        title = Res.string.services
    )
    ServicesContent(
        modifier = Modifier
            .padding(
                top = 12.dp
            ),
        viewModel = viewModel,
        savedHostOverview = savedHostOverview
    )
}

@Composable
@CompactClassComponent
@NonRestartableComposable
private fun ExpandableServicesSection(
    viewModel: HostScreenViewModel,
    savedHostOverview: SavedHostOverview,
) {
    ExpandableSection(
        title = Res.string.services,
        content = {
            ServicesContent(
                viewModel = viewModel,
                savedHostOverview = savedHostOverview
            )
        }
    )
}

@Composable
@NonRestartableComposable
private fun ServicesContent(
    modifier: Modifier = Modifier,
    viewModel: HostScreenViewModel,
    savedHostOverview: SavedHostOverview,
) {
    ServicesList(
        modifier = modifier,
        viewModel = viewModel,
        savedHostOverview = savedHostOverview
    )
}