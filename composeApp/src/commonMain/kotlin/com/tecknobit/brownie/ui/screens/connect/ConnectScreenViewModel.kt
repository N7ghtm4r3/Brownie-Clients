package com.tecknobit.brownie.ui.screens.connect

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.HOSTS_SCREEN
import com.tecknobit.brownie.localSession
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.requester
import com.tecknobit.browniecore.JOIN_CODE_KEY
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isHostValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isServerSecretValid
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

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
        if (!isHostValid(host.value)) {
            hostError.value = true
            return
        }
        if(!isServerSecretValid(serverSecret.value)) {
            serverSecretError.value = true
            return
        }
        if (connecting.value && joinCode.value.isEmpty()) {
            joinCodeError.value = true
            return
        }
        if(!isPasswordValid(password.value)) {
            passwordError.value = true
            return
        }
        requester.changeHost(
            host = host.value
        )
        if (connecting.value)
            connectToSession()
        else
            createSession()
    }

    private fun connectToSession() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    connectToSession(
                        serverSecret = serverSecret.value,
                        joinCode = joinCode.value,
                        password = password.value
                    )
                },
                onSuccess = { response ->
                    launchApp(
                        joinCode = joinCode.value,
                        responseData = response.toResponseData()
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    private fun createSession() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    createSession(
                        serverSecret = serverSecret.value,
                        password = password.value
                    )
                },
                onSuccess = { response ->
                    val responseData = response.toResponseData()
                    launchApp(
                        joinCode = responseData[JOIN_CODE_KEY].treatsAsString(),
                        responseData = responseData
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    private fun launchApp(
        joinCode: String,
        responseData: JsonObject,
    ) {
        localSession.insertNewSession(
            hostAddress = host.value,
            sessionId = responseData[IDENTIFIER_KEY].treatsAsString(),
            joinCode = joinCode
        )
        navigator.navigate(HOSTS_SCREEN)
    }

}