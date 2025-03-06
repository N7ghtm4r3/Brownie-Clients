package com.tecknobit.brownie.ui.screens.upsertservice.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.helpers.KReviewer
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class UpsertServiceScreenViewModel(
    private val hostId: String,
    serviceId: String?,
) : UpsertScreenViewModel<HostService>(
    itemId = serviceId
) {

    lateinit var programArguments: MutableState<String>

    lateinit var purgeNohupOutAfterReboot: MutableState<Boolean>

    lateinit var autoRunAfterHostReboot: MutableState<Boolean>

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
                onSuccess = { _item.value = Json.decodeFromJsonElement(it.toResponseData()) },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

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
                    navigator.goBack()
                    onSuccess()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

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
                    navigator.goBack()
                    onSuccess()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

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
                        navigator.goBack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

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
                        navigator.goBack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}