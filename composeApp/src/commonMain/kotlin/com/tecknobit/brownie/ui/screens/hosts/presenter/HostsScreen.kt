@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.hosts.presenter

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.hosts
import brownie.composeapp.generated.resources.register
import com.tecknobit.brownie.CloseApplicationOnNavBack
import com.tecknobit.brownie.ui.icons.AssignmentAdd
import com.tecknobit.brownie.ui.screens.hosts.components.HostsList
import com.tecknobit.brownie.ui.screens.hosts.presentation.HostsScreenViewModel
import com.tecknobit.brownie.ui.theme.BrownieTheme
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
                HostsList(
                    modifier = Modifier
                        .padding(
                            top = it.calculateTopPadding()
                        ),
                    viewModel = viewModel
                )
            }
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

    override fun onStart() {
        super.onStart()
        viewModel.refreshHostsList()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}