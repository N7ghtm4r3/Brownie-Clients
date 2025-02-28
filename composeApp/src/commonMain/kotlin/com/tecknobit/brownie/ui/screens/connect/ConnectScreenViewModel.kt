package com.tecknobit.brownie.ui.screens.connect

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.brownie.HOSTS_SCREEN
import com.tecknobit.brownie.navigator
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

class ConnectScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `host` the value of the host to reach
     */
    lateinit var host: MutableState<String>

    /**
     * `hostError` whether the [host] field is not valid
     */
    lateinit var hostError: MutableState<Boolean>

    /**
     * `serverSecret` the value of the server secret
     */
    lateinit var serverSecret: MutableState<String>

    /**
     * `serverSecretError` whether the [serverSecret] field is not valid
     */
    lateinit var serverSecretError: MutableState<Boolean>

    fun connect() {
        // TODO: TO ENABLE VALIDATION
        /*if(!isHostValid(host.value)) {
            hostError.value = true
            return
        }
        if(!isServerSecretValid(serverSecret.value)) {
            serverSecretError.value = true
            return
        }*/
        // TODO: MAKE THE REQUEST THEN
        navigator.navigate(HOSTS_SCREEN)
    }

}