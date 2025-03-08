@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.brownie.ui.screens.host.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.RemoveFromQueue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.add_service
import com.tecknobit.brownie.UPSERT_SERVICE_SCREEN
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.components.UnregisterSavedHost
import com.tecknobit.brownie.ui.icons.ServerSpark
import com.tecknobit.brownie.ui.screens.host.components.HostOverview
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import org.jetbrains.compose.resources.stringResource

/**
 * The [HostsScreen] class is used to display and to handle a host data from monitoring its current
 * stats, handle its status and to handle its services
 *
 * @param hostId The identifier of the host
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
class HostScreen(
    hostId: String,
) : EquinoxScreen<HostScreenViewModel>(
    viewModel = HostScreenViewModel(
        hostId = hostId
    )
) {

    /**
     *`hostOverview` the current host overview data
     */
    private lateinit var hostOverview: State<SavedHostOverview?>

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        BrownieTheme {
            ManagedContent(
                viewModel = viewModel,
                initialDelay = 2000,
                loadingRoutine = { hostOverview.value != null },
                content = {
                    Scaffold(
                        modifier = Modifier
                            .navigationBarsPadding(),
                        topBar = {
                            MediumTopAppBar(
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                navigationIcon = {
                                    IconButton(
                                        onClick = { navigator.goBack() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                            contentDescription = null
                                        )
                                    }
                                },
                                title = {
                                    Column {
                                        Text(
                                            text = hostOverview.value!!.hostAddress,
                                            style = AppTypography.labelMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = hostOverview.value!!.name,
                                            style = AppTypography.headlineSmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                },
                                actions = {
                                    val unregister = remember { mutableStateOf(false) }
                                    IconButton(
                                        onClick = { unregister.value = true }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RemoveFromQueue,
                                            contentDescription = null
                                        )
                                    }
                                    UnregisterSavedHost(
                                        show = unregister,
                                        hostManager = viewModel,
                                        host = hostOverview.value!!,
                                        onSuccess = {
                                            viewModel.suspendRetriever()
                                            navigator.goBack()
                                        }
                                    )
                                }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel.snackbarHostState!!
                            )
                        },
                        floatingActionButton = { FAB() }
                    ) {
                        HostOverview(
                            modifier = Modifier
                                .padding(
                                    top = it.calculateTopPadding()
                                ),
                            viewModel = viewModel,
                            hostOverview = hostOverview.value!!
                        )
                    }
                }
            )
        }
    }

    /**
     * Section dedicated to display the [FloatingActionButton] based on the current screen class size
     */
    @Composable
    @NonRestartableComposable
    private fun FAB() {
        ResponsiveContent(
            onExpandedSizeClass = { ExpandedFab() },
            onMediumSizeClass = { ExpandedFab() },
            onCompactSizeClass = {
                FloatingActionButton(
                    onClick = {
                        navigator.navigate(
                            route = "$UPSERT_SERVICE_SCREEN/${hostOverview.value!!.id}" +
                                    "/${hostOverview.value!!.name}"
                        )
                    }
                ) {
                    Icon(
                        imageVector = ServerSpark,
                        contentDescription = null
                    )
                }
            }
        )
    }

    /**
     * Custom [ExtendedFloatingActionButton] to allow the user to add a new service
     */
    @Composable
    @NonRestartableComposable
    private fun ExpandedFab() {
        ExtendedFloatingActionButton(
            onClick = {
                navigator.navigate(
                    route = "$UPSERT_SERVICE_SCREEN/${hostOverview.value!!.id}" +
                            "/${hostOverview.value!!.name}"
                )
            }
        ) {
            Text(
                text = stringResource(Res.string.add_service)
            )
            Icon(
                modifier = Modifier
                    .padding(
                        start = 5.dp
                    ),
                imageVector = ServerSpark,
                contentDescription = null
            )
        }
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveHostOverview()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        hostOverview = viewModel.hostOverview.collectAsState()
    }

}