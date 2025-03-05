package com.tecknobit.brownie.ui.screens.upserthost.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.helpers.KReviewer
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.browniecore.helpers.BrownieInputsValidator.isHostAddressValid
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class UpsertHostScreenViewModel(
    hostId: String?,
) : UpsertScreenViewModel<SavedHostImpl>(
    itemId = hostId
) {

    lateinit var address: MutableState<String>

    lateinit var addressError: MutableState<Boolean>

    lateinit var sshUser: MutableState<String>

    lateinit var sshUserError: MutableState<Boolean>

    lateinit var sshPassword: MutableState<String>

    lateinit var sshPasswordError: MutableState<Boolean>

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun retrieveItem() {
        if (itemId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getHost(
                        hostId = itemId
                    )
                },
                onSuccess = { _item.value = json.decodeFromJsonElement(it.toResponseData()) },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    override fun insert() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    registerHost(
                        hostName = name.value,
                        hostAddress = address.value,
                        sshUser = sshUser.value,
                        sshPassword = sshPassword.value
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
                    editHost(
                        hostId = itemId!!,
                        hostName = name.value,
                        hostAddress = address.value,
                        sshUser = sshUser.value,
                        sshPassword = sshPassword.value
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

    override fun validForm(): Boolean {
        val validity = super.validForm()
        if (!validity)
            return false
        if (!isHostAddressValid(address.value)) {
            addressError.value = true
            return false
        }
        return true
    }

}