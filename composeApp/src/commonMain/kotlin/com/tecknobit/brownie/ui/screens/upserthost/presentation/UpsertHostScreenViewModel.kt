package com.tecknobit.brownie.ui.screens.upserthost.presentation

import androidx.compose.runtime.MutableState
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isHostValid

class UpsertHostScreenViewModel(
    hostId: String?,
) : UpsertScreenViewModel<SavedHost>(
    itemId = hostId
) {

    lateinit var address: MutableState<String>

    lateinit var addressError: MutableState<Boolean>

    override fun retrieveItem() {
        if (itemId == null)
            return
        // TODO: MAKE THE REQUEST THEN
        _item.value = SavedHost.SavedHostImpl(
            id = itemId,
            name = "Prova",
            ipAddress = "122.11.11.1",
            status = HostStatus.ONLINE
        )
    }

    override fun insert() {
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
    }

    override fun update() {
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
    }

    override fun validForm(): Boolean {
        val validity = super.validForm()
        if (!validity)
            return false
        if (!isHostValid(address.value)) {
            addressError.value = true
            return false
        }
        return true
    }

}