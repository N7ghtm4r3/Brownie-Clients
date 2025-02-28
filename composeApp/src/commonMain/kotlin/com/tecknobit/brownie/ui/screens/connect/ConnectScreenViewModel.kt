package com.tecknobit.brownie.ui.screens.connect

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.brownie.HOSTS_SCREEN
import com.tecknobit.brownie.navigator
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

class ConnectScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var connecting: MutableState<Boolean>

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

    /**
     * `joinCode` the value of the join code
     */
    lateinit var joinCode: MutableState<String>

    /**
     * `joinCodeError` whether the [joinCode] field is not valid
     */
    lateinit var joinCodeError: MutableState<Boolean>

    /**
     * `password` the value of the password
     */
    lateinit var password: MutableState<String>

    /**
     * `passwordError` whether the [password] field is not valid
     */
    lateinit var passwordError: MutableState<Boolean>

    fun connect() {
        // TODO: TO ENABLE VALIDATION
        /*if(!isHostValid(host.value)) {
            hostError.value = true
            return
        }
        if(!isServerSecretValid(serverSecret.value)) {
            serverSecretError.value = true
            return
        }
        if(joinCode.value.isEmpty()) {
            joinCodeError.value = true
            return
        }
        if(!isPasswordValid(password.value)) {
            passwordError.value = true
            return
        }
        */
        // TODO: MAKE THE REQUEST THEN
        navigator.navigate(HOSTS_SCREEN)
    }

}