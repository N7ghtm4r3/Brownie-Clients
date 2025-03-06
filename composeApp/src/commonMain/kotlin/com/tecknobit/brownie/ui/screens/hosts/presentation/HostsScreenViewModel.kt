package com.tecknobit.brownie.ui.screens.hosts.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.shared.presentations.HostManager
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

class HostsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), HostManager {

    override var requestsScope: CoroutineScope = viewModelScope

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

    private fun loadHosts(
        page: Int,
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getHosts(
                        page = page,
                        keywords = inputSearch.value,
                        statuses = statusFilters
                    )
                },
                serializer = SavedHostImpl.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    hostsState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    fun applyStatusFilters(
        onSuccess: () -> Unit,
    ) {
        onSuccess()
        hostsState.refresh()
    }

    override fun showFailure(
        error: JsonObject,
    ) {
        showSnackbarMessage(
            response = error
        )
    }

}