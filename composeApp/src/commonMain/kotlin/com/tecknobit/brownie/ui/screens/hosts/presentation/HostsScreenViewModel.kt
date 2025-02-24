package com.tecknobit.brownie.ui.screens.hosts.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlin.random.Random

class HostsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val hostsState = PaginationState<Int, SavedHost>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadHosts(
                page = page
            )
        }
    )

    private fun loadHosts(
        page: Int,
    ) {
        val e = listOf(
            SavedHost(
                id = Random.nextLong().toString(),
                name = "aa",
                ipAddress = "192.168.1.1",
                status = HostStatus.ONLINE
            )
        )
        // TODO: MAKE THE REQUEST THEN
        hostsState.appendPage(
            items = if (true)
                emptyList()
            else
                e, // TODO: MAKE THE REQUEST,
            nextPageKey = page + 1, // TODO: MAKE THE REQUEST
            isLastPage = Random.nextBoolean() // TODO: MAKE THE REQUEST
        )
    }

}