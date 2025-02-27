@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.enter_name_or_pid
import brownie.composeapp.generated.resources.filter_services
import brownie.composeapp.generated.resources.services
import brownie.composeapp.generated.resources.status
import com.tecknobit.brownie.ui.components.ServiceStatusBadge
import com.tecknobit.brownie.ui.components.alertTitleStyle
import com.tecknobit.brownie.ui.screens.host.components.ExpandableSection
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.toggle
import org.jetbrains.compose.resources.stringResource

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
        title = Res.string.services,
        filtersContent = {
            FiltersSection(
                viewModel = viewModel
            )
        }
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
        filtersContent = {
            FiltersSection(
                viewModel = viewModel
            )
        },
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
private fun FiltersSection(
    viewModel: HostScreenViewModel,
) {
    val show = remember { mutableStateOf(false) }
    Icon(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { show.value = true },
        imageVector = Icons.Default.FilterAlt,
        contentDescription = null
    )
    FiltersDialog(
        show = show,
        viewModel = viewModel
    )
}

@Composable
@NonRestartableComposable
private fun FiltersDialog(
    show: MutableState<Boolean>,
    viewModel: HostScreenViewModel,
) {
    viewModel.servicesQuery = remember { mutableStateOf("") }
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        icon = Icons.Default.FilterAlt,
        viewModel = viewModel,
        title = Res.string.filter_services,
        titleStyle = alertTitleStyle,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EquinoxTextField(
                    shape = CardDefaults.shape,
                    value = viewModel.servicesQuery,
                    placeholder = stringResource(Res.string.enter_name_or_pid),
                    maxLines = 1,
                    textFieldColors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.servicesQuery.value = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
                SectionTitle(
                    title = Res.string.status
                )
                ServiceStatus.entries.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = viewModel.statusFilters.contains(status),
                            onCheckedChange = {
                                viewModel.statusFilters.toggle(
                                    element = status
                                )
                            }
                        )
                        ServiceStatusBadge(
                            status = status
                        )
                    }
                }
            }
        },
        onDismissAction = {
            viewModel.clearFilters {
                show.value = false
            }
        },
        confirmAction = {
            viewModel.applyFilters {
                show.value = false
            }
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