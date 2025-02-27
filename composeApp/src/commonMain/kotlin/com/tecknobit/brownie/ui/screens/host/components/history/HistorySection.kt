@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components.history

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.history
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
fun HistorySection(
    viewModel: HostScreenViewModel,
    savedHostOverview: SavedHostOverview,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            FixedHistorySection(
                savedHostOverview = savedHostOverview
            )
        },
        onMediumSizeClass = {
            FixedHistorySection(
                savedHostOverview = savedHostOverview
            )
        },
        onCompactSizeClass = {
            ExpandableHistorySection(
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
private fun FixedHistorySection(
    savedHostOverview: SavedHostOverview,
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp
            ),
        title = Res.string.history
    )
    HistoryContent(
        savedHostOverview = savedHostOverview
    )
}

@Composable
@CompactClassComponent
@NonRestartableComposable
private fun ExpandableHistorySection(
    savedHostOverview: SavedHostOverview,
) {
    ExpandableSection(
        title = Res.string.history,
        content = {
            HistoryContent(
                savedHostOverview = savedHostOverview
            )
        }
    )
}

@Composable
@NonRestartableComposable
private fun HistoryContent(
    savedHostOverview: SavedHostOverview,
) {
    HostHistory(
        hostOverview = savedHostOverview
    )
}