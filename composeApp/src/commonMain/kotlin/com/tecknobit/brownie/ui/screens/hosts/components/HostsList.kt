@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.hosts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.add
import brownie.composeapp.generated.resources.no_hosts_registered
import brownie.composeapp.generated.resources.server_down
import com.tecknobit.brownie.ui.components.FirstPageProgressIndicator
import com.tecknobit.brownie.ui.components.NewPageProgressIndicator
import com.tecknobit.brownie.ui.screens.hosts.presentation.HostsScreenViewModel
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen.Companion.MAX_CONTAINER_WIDTH
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun HostsList(
    viewModel: HostsScreenViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaginatedLazyColumn(
            modifier = Modifier
                .widthIn(
                    max = MAX_CONTAINER_WIDTH
                )
                .fillMaxHeight(),
            paginationState = viewModel.hostsState,
            contentPadding = PaddingValues(
                all = 16.dp
            ),
            firstPageEmptyIndicator = {
                EmptyState(
                    resourceSize = responsiveAssignment(
                        onExpandedSizeClass = { 400.dp },
                        onMediumSizeClass = { 300.dp },
                        onCompactSizeClass = { 250.dp }
                    ),
                    resource = painterResource(Res.drawable.server_down),
                    contentDescription = "Hosts list empty",
                    title = stringResource(Res.string.no_hosts_registered),
                    titleStyle = AppTypography.titleLarge,
                    action = {
                        TextButton(
                            onClick = {
                                // TODO: NAV TO CREATE 
                            }
                        ) {
                            Text(
                                text = stringResource(Res.string.add)
                            )
                        }
                    }
                )
            },
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() }
        ) {
            items(
                items = viewModel.hostsState.allItems!!,
                key = { host -> host.id }
            ) { host ->

            }
        }
    }
}