package com.tecknobit.brownie.ui.screens.hosts.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
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

    lateinit var inputSearch: MutableState<String>

    val statusFilters = mutableStateListOf<HostStatus>()

    init {
        statusFilters.addAll(HostStatus.entries)
    }

    val hostsState = PaginationState<Int, SavedHostImpl>(
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
            refreshDelay = 10000
        )
    }

    private fun loadHosts(
        page: Int,
    ) {
        val e = listOf(
            SavedHostImpl(
                id = Random.nextLong().toString(),
                name = "aa",
                ipAddress = "192.168.1.1",
                status = ONLINE
            ),

            SavedHostImpl(
                id = Random.nextLong().toString(),
                name = "aa",
                ipAddress = "192.168.1.1",
                status = OFFLINE
            ),

            SavedHostImpl(
                id = Random.nextLong().toString(),
                name = "aa",
                ipAddress = "192.168.1.1",
                status = HostStatus.entries[Random.nextInt(3)]
            )
        )
        // TODO: MAKE THE REQUEST THEN
        // TODO: TO APPLY THE FILTERS ALSO 
        hostsState.appendPage(
            items = if (false)
                emptyList()
            else
                e, // TODO: USE THE REAL DATA,
            nextPageKey = page + 1, // TODO: USE THE REAL DATA,
            isLastPage = true || Random.nextBoolean() // TODO: USE THE REAL DATA,
        )
    }

    fun applyStatusFilters(
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onSuccess()
        hostsState.refresh()
    }

    fun handleHostStatus(
        host: SavedHostImpl,
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
        host: SavedHostImpl,
        onStatusChange: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        onStatusChange.invoke()
        //hostsState.refresh()
    }

}