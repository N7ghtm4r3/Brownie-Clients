package com.tecknobit.brownie.ui.screens.upsertservice.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.helpers.BrownieInputsValidator.isServiceNameValid
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class UpsertServiceScreenViewModel(
    private val serviceId: String?,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _service = MutableStateFlow<HostService?>(
        value = null
    )
    val service = _service.asStateFlow()

    lateinit var serviceName: MutableState<String>

    lateinit var serviceNameError: MutableState<Boolean>

    lateinit var programArguments: MutableState<String>

    lateinit var purgeNohupOutAfterReboot: MutableState<Boolean>

    lateinit var autoRunAfterHostReboot: MutableState<Boolean>

    fun retrieveService() {
        if (serviceId == null)
            return
        // TODO: MAKE THE REQUEST THEN
        _service.value = HostService(
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

    fun upsert() {
        if (!validForm())
            return
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
    }

    private fun validForm(): Boolean {
        if (!isServiceNameValid(serviceName.value)) {
            serviceNameError.value = true
            return false
        }
        return true
    }

}