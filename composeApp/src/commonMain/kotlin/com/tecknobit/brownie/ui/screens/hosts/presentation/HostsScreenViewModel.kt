@file:OptIn(ExperimentalPaginationApi::class)

package com.tecknobit.brownie.ui.screens.hosts.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.shared.presentations.HostManager
import com.tecknobit.browniecore.STATUS_KEY
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseArrayData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import io.github.ahmad_hamwi.compose.pagination.ExperimentalPaginationApi
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

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

    private val _refreshingHosts = MutableSharedFlow<Boolean>(
        replay = 1
    )
    val refreshingHosts = _refreshingHosts.asSharedFlow()

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

    fun retrieveCurrentHostsStatus() {
        retrieve(
            currentContext = HostsScreen::class,
            routine = {
                // TODO: TO REMOVE THIS WORKAROUND WHEN ROUTINE suspend
                viewModelScope.launch {
                    requester.sendRequest(
                        request = {
                            getHostsStatus(
                                currentHosts = hostsState.allItems
                            )
                        },
                        onSuccess = {
                            viewModelScope.launch {
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
                            }
                        },
                        onFailure = { setHasBeenDisconnectedValue(true) },
                        onConnectionError = { setServerOfflineValue(true) }
                    )
                }
            },
            refreshDelay = 10_000
        )
    }

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