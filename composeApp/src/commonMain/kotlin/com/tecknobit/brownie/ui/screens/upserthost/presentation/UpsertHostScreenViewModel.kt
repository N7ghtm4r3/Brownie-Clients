package com.tecknobit.brownie.ui.screens.upserthost.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.helpers.KReviewer
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.requester
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.browniecore.helpers.BrownieInputsValidator.isHostAddressValid
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `UpsertHostScreenViewModel` class is the support class used by the
 * [com.tecknobit.brownie.ui.screens.upserthost.presenter.UpsertHostScreen] screen
 *
 * @param hostId The identifier of the host to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see UpsertScreenViewModel
 *
 */
class UpsertHostScreenViewModel(
    hostId: String?,
) : UpsertScreenViewModel<SavedHostImpl>(
    itemId = hostId
) {

    /**
     * `address` the value of the address
     */
    lateinit var address: MutableState<String>

    /**
     * `addressError` whether the [address] field is not valid
     */
    lateinit var addressError: MutableState<Boolean>

    /**
     * `sshUser` the value of the SSH user
     */
    lateinit var sshUser: MutableState<String>

    /**
     * `sshUserError` whether the [sshUser] field is not valid
     */
    lateinit var sshUserError: MutableState<Boolean>

    /**
     * `sshPassword` the value of the SSH password
     */
    lateinit var sshPassword: MutableState<String>

    /**
     * `sshPasswordError` whether the [sshPassword] field is not valid
     */
    lateinit var sshPasswordError: MutableState<Boolean>

    /**
     * `json` the json instance used to ignore the unknown keys during the serialization
     */
    private val json = Json {
        ignoreUnknownKeys = true
    }

    /**
     * Method used to retrieve the information of the item to display
     */
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

    /**
     * Method used to insert a new item
     */
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

    /**
     * Method used to edit an existing item
     */
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

    /**
     * Method used to check the validity of the form data to insert or update an item
     *
     * @return the validity of the form as [Boolean]
     */
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