@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.brownie.ui.screens.upsertservice.presentation

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.helpers.KReviewer
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `UpsertServiceScreenViewModel` class is the support class used by the
 * [com.tecknobit.brownie.ui.screens.upsertservice.presenter.UpsertServiceScreen] screen
 *
 * @param hostId The identifier of the host owner of the service
 * @param serviceId The identifier of the service
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see UpsertScreenViewModel
 *
 */
class UpsertServiceScreenViewModel(
    private val hostId: String,
    serviceId: String?,
) : UpsertScreenViewModel<HostService>(
    itemId = serviceId
) {

    /**
     * `programArguments` the arguments of the program
     */
    lateinit var programArguments: MutableState<String>

    /**
     * `purgeNohupOutAfterReboot` whether the `nohup.out` file related to the service must be
     * deleted at each service start
     */
    lateinit var purgeNohupOutAfterReboot: MutableState<Boolean>

    /**
     * `autoRunAfterHostReboot` whether the service must be automatically restarted after the host start or
     * the host restart
     */
    lateinit var autoRunAfterHostReboot: MutableState<Boolean>

    /**
     * Method used to retrieve the information of the item to display
     */
    override fun retrieveItem() {
        if (itemId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getService(
                        hostId = hostId,
                        serviceId = itemId
                    )
                },
                onSuccess = {
                    sessionFlowState.notifyOperational()
                    _item.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { showSnackbarMessage(it) },
                onConnectionError = { sessionFlowState.notifyServerOffline() }
            )
        }
    }

    /**
     * Method used to remove a service
     *
     * @param onSuccess The callback to invoke after service removed
     */
    fun removeService(
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    removeService(
                        hostId = hostId,
                        serviceId = itemId!!
                    )
                },
                onSuccess = {
                    navigator.popBackStack()
                    onSuccess()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method used to delete a service
     *
     * @param onSuccess The callback to invoke after service deleted
     */
    fun deleteService(
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteService(
                        hostId = hostId,
                        serviceId = itemId!!
                    )
                },
                onSuccess = {
                    navigator.popBackStack()
                    onSuccess()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method used to insert a new item
     */
    override fun insert() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    addService(
                        hostId = hostId,
                        serviceName = name.value,
                        programArguments = programArguments.value,
                        purgeNohupOutAfterReboot = purgeNohupOutAfterReboot.value,
                        autoRunAfterHostReboot = autoRunAfterHostReboot.value
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        navigator.popBackStack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method used to edit an existing item
     */
    override fun update() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    editService(
                        hostId = hostId,
                        serviceId = itemId!!,
                        serviceName = name.value,
                        programArguments = programArguments.value,
                        purgeNohupOutAfterReboot = purgeNohupOutAfterReboot.value,
                        autoRunAfterHostReboot = autoRunAfterHostReboot.value
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        navigator.popBackStack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}