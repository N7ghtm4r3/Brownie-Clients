@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.screens.host.components.services

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.no_services_available
import brownie.composeapp.generated.resources.undraw_server_cluster
import com.tecknobit.brownie.ui.components.FirstPageProgressIndicator
import com.tecknobit.brownie.ui.components.NewPageProgressIndicator
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presentation.HostScreenViewModel
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


/**
 * Custom [PaginatedLazyColumn] component used to display the services
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param savedHostOverview The host overview data
 */
@Composable
@NonRestartableComposable
fun ServicesList(
    modifier: Modifier = Modifier,
    viewModel: HostScreenViewModel,
    savedHostOverview: SavedHostOverview
) {
    PaginatedLazyColumn(
        modifier = modifier
            .widthIn(
                max = 1000.dp
            )
            .heightIn(
                max = 700.dp
            )
            .animateContentSize(),
        paginationState = viewModel.servicesState,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageEmptyIndicator = { NoServicesRegistered() },
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        items(
            items = viewModel.servicesState.allItems!!,
            key = { service -> service.id }
        ) { service ->
            ServiceCard(
                viewModel = viewModel,
                savedHostOverview = savedHostOverview,
                service = service
            )
        }
    }
}

/**
 * Custom [EmptyState] component used to display the no availability of services to display
 */
@Composable
@NonRestartableComposable
private fun NoServicesRegistered() {
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 400.dp },
            onMediumSizeClass = { 300.dp },
            onMediumWidthCompactHeight = { 215.dp },
            onCompactSizeClass = { 250.dp }
        ),
        resource = painterResource(Res.drawable.undraw_server_cluster),
        contentDescription = "Services list empty",
        title = stringResource(Res.string.no_services_available),
        titleStyle = AppTypography.titleLarge
    )
}