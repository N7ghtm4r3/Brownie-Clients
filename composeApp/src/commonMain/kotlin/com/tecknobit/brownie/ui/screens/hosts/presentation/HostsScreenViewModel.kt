package com.tecknobit.brownie.ui.screens.hosts.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.shared.presentations.HostManager
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.STATUS_KEY
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseArrayData
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequestAsyncHandlers
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * The `HostsScreenViewModel` class is the support class used by the
 * [com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen] screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see HostManager
 *
 */
class HostsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), HostManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    override var requestsScope: CoroutineScope = MainScope()

    /**
     *`inputSearch` is the input inserted by the user to filter the hosts result
     */
    lateinit var inputSearch: MutableState<String>

    /**
     *`statusFilters` the list of the statuses used to filter the hosts result
     */
    val statusFilters = mutableStateListOf<HostStatus>()

    init {
        statusFilters.addAll(HostStatus.entries)
    }

    /**
     *`hostsState` the state used to handle the pagination of the hosts
     */
    val hostsState = PaginationState<Int, SavedHostImpl>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadHosts(
                page = page
            )
        }
    )

    /**
     *`_refreshingHosts` shared state used to refresh the hosts statuses
     */
    private val _refreshingHosts = MutableSharedFlow<Boolean>(
        replay = 1
    )
    val refreshingHosts = _refreshingHosts.asSharedFlow()

    /**
     * Method used to load and retrieve the hosts owned by the session
     * @param page The page to request
     */
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

    /**
     * Method used to retrieve the current hosts statuses and refresh the [hostsState] items
     */
    fun retrieveCurrentHostsStatus() {
        retrieve(
            currentContext = HostsScreen::class,
            routine = {
                requester.sendRequestAsyncHandlers(
                    request = {
                        getHostsStatus(
                            currentHosts = hostsState.allItems
                        )
                    },
                    onSuccess = {
                        setServerOfflineValue(false)
                        _refreshingHosts.emit(true)
                        val statuses = it.toResponseArrayData()
                        statuses.forEach { statusEntry ->
                            updateHostStatus(
                                statusInfo = statusEntry.jsonObject
                            )
                        }
                        delay(100)
                        _refreshingHosts.emit(false)
                    },
                    onFailure = { setHasBeenDisconnectedValue(true) },
                    onConnectionError = { setServerOfflineValue(true) }
                )
            },
            refreshDelay = 10_000
        )
    }

    /**
     * Method used to update the status of a host
     *
     * @param statusInfo The updated information of the host
     */
    private fun updateHostStatus(
        statusInfo: JsonObject,
    ) {
        val hostId = statusInfo[IDENTIFIER_KEY].treatsAsString()
        val status = HostStatus.valueOf(
            value = statusInfo[STATUS_KEY].treatsAsString()
        )
        val host = hostsState.allItems?.find { host -> host.id == hostId }
        host?.let {
            host.status = status
        }
    }

    /**
     * Method used to apply the selected statuses and refresh the current [hostsState] list
     *
     * @param onSuccess The callback invoked when the statuses have been applied
     */
    fun applyStatusFilters(
        onSuccess: () -> Unit,
    ) {
        onSuccess()
        hostsState.refresh()
    }

    /**
     * Method used to display an eventual occurred error
     * @param error The error to display
     */
    override fun showFailure(
        error: JsonObject,
    ) {
        showSnackbarMessage(
            response = error
        )
    }

}