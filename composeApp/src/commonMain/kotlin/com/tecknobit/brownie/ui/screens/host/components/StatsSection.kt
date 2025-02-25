@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.stats
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent

@Composable
@NonRestartableComposable
fun StatsSection() {
    ResponsiveContent(
        onExpandedSizeClass = { RowStats() },
        onMediumSizeClass = { RowStats() },
        onCompactSizeClass = { ColumnStats() }
    )
}

@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun RowStats() {
    SectionTitle(
        title = Res.string.stats
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(4)
    ) {

    }
}


@Composable
@CompactClassComponent
@NonRestartableComposable
private fun ColumnStats() {
    ExpandableSection(
        title = Res.string.stats,
        content = {
            LazyColumn {

            }
        }
    )
}

