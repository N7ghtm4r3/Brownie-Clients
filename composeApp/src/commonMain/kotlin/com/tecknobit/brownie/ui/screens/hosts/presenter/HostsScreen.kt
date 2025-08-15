@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class,
    ExperimentalComposeApi::class
)

package com.tecknobit.brownie.ui.screens.hosts.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.ExperimentalComposeApi
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
import com.tecknobit.brownie.ADMIN_CONTROL_PANEL_SCREEN
import com.tecknobit.brownie.CloseApplicationOnNavBack
import com.tecknobit.brownie.UPSERT_HOST_SCREEN
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.components.StatusFilterButton
import com.tecknobit.brownie.ui.icons.AssignmentAdd
import com.tecknobit.brownie.ui.screens.hosts.components.HostsList
import com.tecknobit.brownie.ui.screens.hosts.presentation.HostsScreenViewModel
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.equinoxcompose.components.DebouncedTextField
import com.tecknobit.equinoxcompose.components.RetryButton
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import org.jetbrains.compose.resources.stringResource

/**
 * The [HostsScreen] class is used to display and handle the current hosts owned by the session
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
class HostsScreen : EquinoxScreen<HostsScreenViewModel>(
    viewModel = HostsScreenViewModel()
) {

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        BrownieTheme {
            SessionFlowContainer(
                modifier = Modifier
                    .fillMaxSize(),
                state = viewModel.sessionFlowState,
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
                                },
                                actions = {
                                    IconButton(
                                        onClick = { navigator.navigate(ADMIN_CONTROL_PANEL_SCREEN) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AdminPanelSettings,
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel.snackbarHostState!!
                            )
                        },
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                onClick = { navigator.navigate(UPSERT_HOST_SCREEN) },
                                expanded = responsiveAssignment(
                                    onExpandedSizeClass = { true },
                                    onMediumSizeClass = { true },
                                    onCompactSizeClass = { false }
                                ),
                                icon = {
                                    Icon(
                                        imageVector = AssignmentAdd,
                                        contentDescription = null
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(Res.string.register)
                                    )
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
                },
                retryFailedFlowContent = {
                    RetryButton(
                        onRetry = {
                            viewModel.hostsState.retryLastFailedRequest()
                        }
                    )
                }
            )
        }
    }

    /**
     * Custom section used to filter the hosts result
     *
     * @param modifier The modifier to apply to the section
     */
    @Composable
    private fun FiltersBar(
        modifier: Modifier
    ) {
        Row(
            modifier = modifier
                .responsiveMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DebouncedTextField(
                shape = CardDefaults.shape,
                value = viewModel.inputSearch,
                debounce = { viewModel.hostsState.refresh() },
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

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveCurrentHostsStatus()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.inputSearch = remember { mutableStateOf("") }
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

}