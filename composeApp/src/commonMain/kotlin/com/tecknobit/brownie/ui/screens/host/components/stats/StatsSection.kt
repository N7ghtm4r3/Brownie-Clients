@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components.stats

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.stats
import com.tecknobit.brownie.ui.screens.host.components.ExpandableSection
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent

/**
 * The section used to display the stats related to a host
 *
 * @param hostOverview The overview data of the host
 */
@Composable
@NonRestartableComposable
fun StatsSection(
    hostOverview: SavedHostOverview
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            RowStats(
                hostOverview = hostOverview
            )
        },
        onMediumSizeClass = {
            RowStats(
                hostOverview = hostOverview
            )
        },
        onCompactSizeClass = {
            ColumnStats(
                hostOverview = hostOverview
            )
        }
    )
}

/**
 * Custom [StatsSection] displayed in large screen size classes
 *
 * @param hostOverview The overview data of the host
 */
@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun RowStats(
    hostOverview: SavedHostOverview
) {
    SectionTitle(
        modifier = Modifier
            .padding(
                top = 16.dp,
                start = 16.dp
            ),
        title = Res.string.stats
    )
    LazyVerticalGrid(
        modifier = Modifier
            .heightIn(
                max = 300.dp
            ),
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(
            all = 16.dp
        ),
        horizontalArrangement = Arrangement.Center
    ) {
        item {
            CpuUsageChart(
                usage = hostOverview.cpuUsage
            )
        }
        item {
            MemoryUsageChart(
                usage = hostOverview.memoryUsage
            )
        }
        item {
            StorageUsageChart(
                usage = hostOverview.storageUsage
            )
        }
    }
}

/**
 * Custom [StatsSection] displayed in compact screen size classes
 *
 * @param hostOverview The overview data of the host
 */
@Composable
@CompactClassComponent
@NonRestartableComposable
private fun ColumnStats(
    hostOverview: SavedHostOverview
) {
    ExpandableSection(
        title = Res.string.stats,
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        max = 700.dp
                    )
                    .animateContentSize(),
                contentPadding = PaddingValues(
                    all = 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {
                    CpuUsageChart(
                        usage = hostOverview.cpuUsage
                    )
                }
                item {
                    MemoryUsageChart(
                        usage = hostOverview.memoryUsage
                    )
                }
                item {
                    StorageUsageChart(
                        usage = hostOverview.storageUsage
                    )
                }
            }
        }
    )
}
