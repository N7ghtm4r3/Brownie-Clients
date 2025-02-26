@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.hosts.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.enter_name_or_ip
import brownie.composeapp.generated.resources.hosts
import brownie.composeapp.generated.resources.register
import com.tecknobit.brownie.CloseApplicationOnNavBack
import com.tecknobit.brownie.ui.components.StatusFilterButton
import com.tecknobit.brownie.ui.icons.AssignmentAdd
import com.tecknobit.brownie.ui.screens.hosts.components.HostsList
import com.tecknobit.brownie.ui.screens.hosts.presentation.HostsScreenViewModel
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import org.jetbrains.compose.resources.stringResource

class HostsScreen : EquinoxScreen<HostsScreenViewModel>(
    viewModel = HostsScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        BrownieTheme {
            ManagedContent(
                viewModel = viewModel,
                content = {
                    Scaffold(
                        modifier = Modifier
                            .navigationBarsPadding(),
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                title = {
                                    Text(
                                        text = stringResource(Res.string.hosts)
                                    )
                                }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel.snackbarHostState!!
                            )
                        },
                        floatingActionButton = {
                            ResponsiveContent(
                                onExpandedSizeClass = { ExpandedFAB() },
                                onMediumSizeClass = { ExpandedFAB() },
                                onCompactSizeClass = {
                                    FloatingActionButton(
                                        onClick = {
                                            // TODO: NAV TO CREATE
                                        }
                                    ) {
                                        Icon(
                                            imageVector = AssignmentAdd,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FiltersBar(
                                modifier = Modifier
                                    .padding(
                                        top = it.calculateTopPadding() + 16.dp
                                    )
                            )
                            HostsList(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    private fun ExpandedFAB() {
        ExtendedFloatingActionButton(
            onClick = {
                // TODO: NAV TO CREATE
            }
        ) {
            Text(
                text = stringResource(Res.string.register)
            )
            Icon(
                modifier = Modifier
                    .padding(
                        start = 5.dp
                    ),
                imageVector = AssignmentAdd,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FiltersBar(
        modifier: Modifier,
    ) {
        Row(
            modifier = modifier
                .widthIn(
                    max = MAX_CONTAINER_WIDTH
                )
                .padding(
                    horizontal = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            EquinoxTextField(
                shape = CardDefaults.shape,
                value = viewModel.inputSearch,
                onValueChange = {
                    viewModel.inputSearch.value = it
                    viewModel.hostsState.refresh()
                },
                placeholder = stringResource(Res.string.enter_name_or_ip),
                maxLines = 1,
                textFieldColors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.inputSearch.value = ""
                            viewModel.hostsState.refresh()
                        }
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
            StatusFilterButton(
                onClick = { menuExpanded ->
                    viewModel.applyStatusFilters(
                        onSuccess = { menuExpanded.value = false }
                    )
                },
                statusFilters = viewModel.statusFilters
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshHostsList()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.inputSearch = remember { mutableStateOf("") }
    }

}