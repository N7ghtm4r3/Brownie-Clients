package com.tecknobit.brownie.ui.screens.hosts.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.OFFLINE
import com.tecknobit.browniecore.enums.HostStatus.ONLINE
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

    fun refreshHostsList() {
        retrieve(
            currentContext = HostsScreen::class,
            routine = { hostsState.refresh() },
            refreshDelay = 5000
        )
    }

    private fun loadHosts(
        page: Int,
    ) {
        val e = listOf(
            SavedHost(
                id = Random.nextLong().toString(),
                name = "aa",
                ipAddress = "192.168.1.1",
                status = ONLINE
            ),

            SavedHost(
                id = Random.nextLong().toString(),
                name = "aa",
                ipAddress = "192.168.1.1",
                status = OFFLINE
            ),

            SavedHost(
                id = Random.nextLong().toString(),
                name = "aa",
                ipAddress = "192.168.1.1",
                status = HostStatus.entries[Random.nextInt(3)]
            )
        )
        // TODO: MAKE THE REQUEST THEN
        hostsState.appendPage(
            items = if (false)
                emptyList()
            else
                e, // TODO: MAKE THE REQUEST,
            nextPageKey = page + 1, // TODO: MAKE THE REQUEST
            isLastPage = true || Random.nextBoolean() // TODO: MAKE THE REQUEST
        )
    }

    fun handleHostStatus(
        host: SavedHost,
        onStatusChange: (HostStatus) -> Unit,
    ) {
        val newStatus = if (host.status.isOnline())
            OFFLINE
        else
            ONLINE
        // TODO: MAKE THE REQUEST THEN
        onStatusChange.invoke(newStatus)
        host.status = newStatus
        //hostsState.refresh()
    }

    fun rebootHost(
        host: SavedHost,
        onStatusChange: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onStatusChange.invoke()
        //hostsState.refresh()
    }

}