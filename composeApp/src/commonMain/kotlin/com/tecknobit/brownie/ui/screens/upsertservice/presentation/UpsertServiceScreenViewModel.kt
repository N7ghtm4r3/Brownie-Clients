package com.tecknobit.brownie.ui.screens.upsertservice.presentation

import androidx.compose.runtime.MutableState
import com.tecknobit.brownie.helpers.KReviewer
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.browniecore.enums.ServiceStatus
import kotlin.random.Random

class UpsertServiceScreenViewModel(
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
        // TODO: MAKE THE REQUEST THEN
        _item.value = HostService(
            id = Random.nextLong().toString(),
            name = "Ametista-1.0.0.jar",
            pid = Random.nextLong(1000000),
            status = ServiceStatus.entries[Random.nextInt(ServiceStatus.entries.size)],
            configuration = HostService.ServiceConfiguration(
                id = Random.nextLong().toString(),
                purgeNohupOutAfterReboot = false
            )
        )
    }

    fun removeService(
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
        onSuccess()
    }

    fun deleteService(
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
        onSuccess()
    }

    override fun insert() {
        // TODO: MAKE THE REQUEST THEN
        val kReviewer = KReviewer()
        kReviewer.reviewInApp {
            navigator.goBack()
        }
    }

    override fun update() {
        // TODO: MAKE THE REQUEST THEN
        val kReviewer = KReviewer()
        kReviewer.reviewInApp {
            navigator.goBack()
        }
    }

}