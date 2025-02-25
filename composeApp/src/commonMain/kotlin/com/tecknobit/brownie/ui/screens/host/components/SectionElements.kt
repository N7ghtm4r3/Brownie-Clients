@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
@CompactClassComponent
@NonRestartableComposable
fun ExpandableSection(
    title: StringResource,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        var expanded by remember { mutableStateOf(true) }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                SectionTitle(
                    title = title
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.ArrowDropUp
                        else
                            Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = expanded
        ) {
            Column {
                content()
                HorizontalDivider()
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun SectionTitle(
    title: StringResource,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = stringResource(title),
        style = AppTypography.bodyLarge
    )
}