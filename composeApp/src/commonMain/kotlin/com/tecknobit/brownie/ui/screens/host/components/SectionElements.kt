@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Custom section displayed on compact size classes can be expanded to show its content
 *
 * @param modifier The modifier to apply to the component
 * @param title The title of the section
 * @param filtersContent The content where the user can insert the filter to apply to the results
 * @param content The content of the section
 */
@Composable
@ScreenSection
@CompactClassComponent
fun ExpandableSection(
    modifier: Modifier = Modifier,
    title: StringResource,
    filtersContent: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column {
        var expanded by rememberSaveable { mutableStateOf(false) }
        Row(
            modifier = modifier
                .padding(
                    start = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                SectionTitle(
                    title = title,
                    filtersContent = if (filtersContent != null) {
                        {
                            AnimatedVisibility(
                                visible = expanded
                            ) {
                                filtersContent()
                            }
                        }
                    } else
                        null
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
            }
        }
        AnimatedVisibility(
            visible = !expanded
        ) {
            HorizontalDivider()
        }
    }
}

/**
 * Custom section displayed on large size classes
 *
 * @param modifier The modifier to apply to the component
 * @param title The title of the section
 * @param fontSize The font size of the title
 * @param filtersContent The content where the user can insert the filter to apply to the results
 */
@Composable
@ScreenSection
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: StringResource,
    fontSize: TextUnit = TextUnit.Unspecified,
    filtersContent: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = stringResource(title),
            style = AppTypography.bodyLarge,
            fontSize = fontSize
        )
        filtersContent?.invoke()
    }
}