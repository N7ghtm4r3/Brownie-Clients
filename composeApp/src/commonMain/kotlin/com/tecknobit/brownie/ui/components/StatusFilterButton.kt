@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.status
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.entries
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.toggle
import org.jetbrains.compose.resources.stringResource

/**
 * Component used to filter the statuses
 *
 * @param modifier The modifier to apply to the component
 * @param onClick The action to execute when the button clicked
 * @param statusFilters The current statuses used as filters
 */
@Composable
@NonRestartableComposable
fun StatusFilterButton(
    modifier: Modifier = Modifier,
    onClick: (MutableState<Boolean>) -> Unit,
    statusFilters: SnapshotStateList<HostStatus>,
) {
    Column {
        val expanded = remember { mutableStateOf(false) }
        ResponsiveContent(
            onExpandedSizeClass = {
                StatusFilterChip(
                    modifier = modifier,
                    expanded = expanded
                )
            },
            onMediumSizeClass = {
                StatusFilterChip(
                    modifier = modifier,
                    expanded = expanded
                )
            },
            onCompactSizeClass = {
                StatusFilterIconButton(
                    expanded = expanded
                )
            }
        )
        StatusesMenu(
            expanded = expanded,
            onClick = onClick,
            statusFilters = statusFilters
        )
    }
}

/**
 * Custom [FilterChip] used to filter the statuses
 *
 * @param modifier The modifier to apply to the component
 * @param expanded Whether the chip has been expanded
 */
@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun StatusFilterChip(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
) {
    FilterChip(
        modifier = modifier
            .size(
                width = 125.dp,
                height = 53.dp
            ),
        shape = CardDefaults.shape,
        selected = expanded.value,
        onClick = { expanded.value = !expanded.value },
        label = {
            Text(
                text = stringResource(Res.string.status)
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (expanded.value)
                    Icons.Default.ArrowDropUp
                else
                    Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    )
}

/**
 * Custom [IconButton] used to filter the statuses
 *
 * @param expanded Whether the chip has been expanded
 */
@Composable
@CompactClassComponent
@NonRestartableComposable
private fun StatusFilterIconButton(
    expanded: MutableState<Boolean>,
) {
    IconButton(
        onClick = { expanded.value = !expanded.value }
    ) {
        Icon(
            imageVector = Icons.Default.FilterAlt,
            contentDescription = null
        )
    }
}

/**
 * Custom [DropdownMenu] used to select the status to use as filters
 *
 * @param expanded Whether the menu has been expanded
 * @param onClick The action to execute when the option selected
 * @param statusFilters The current statuses used as filters
 */
@Composable
@NonRestartableComposable
private fun StatusesMenu(
    expanded: MutableState<Boolean>,
    onClick: (MutableState<Boolean>) -> Unit,
    statusFilters: SnapshotStateList<HostStatus>,
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { onClick.invoke(expanded) }
    ) {
        entries.forEach { status ->
            DropdownMenuItem(
                onClick = {
                    statusFilters.toggle(
                        element = status
                    )
                },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = statusFilters.contains(status),
                            onCheckedChange = {
                                statusFilters.toggle(
                                    element = status
                                )
                            }
                        )
                        HostStatusBadge(
                            status = status
                        )
                    }
                }
            )
        }
    }
}
