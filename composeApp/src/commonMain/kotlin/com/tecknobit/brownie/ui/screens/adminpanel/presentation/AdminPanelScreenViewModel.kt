package com.tecknobit.brownie.ui.screens.adminpanel.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecknobit.brownie.localSession
import com.tecknobit.brownie.requester
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.session.Retriever
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.equinoxcore.network.sendRequest
import kotlinx.coroutines.launch

/**
 * The `AdminPanelScreenViewModel` class is the support class used by the
 * [com.tecknobit.brownie.ui.screens.adminpanel.presenter.AdminPanelScreen] screen to manage the
 * current session data and to customize his/her language and theme
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ViewModel
 * @see Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 *
 */
class AdminPanelScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `language` the language of the user
     */
    lateinit var language: MutableState<String>

    /**
     * `theme` the theme of the user
     */
    lateinit var theme: MutableState<ApplicationTheme>

    /**
     * `password` the value of the password
     */
    lateinit var password: MutableState<String>

    /**
     * `passwordError` whether the [password] field is not valid
     */
    lateinit var passwordError: MutableState<Boolean>

    /**
     * Method used to execute the language change
     *
     * @param onSuccess The action to execute if the request has been successful
     */
    fun changeLanguage(
        onSuccess: (() -> Unit)? = null,
    ) {
        localSession.language = language.value
        onSuccess?.invoke()
    }

    /**
     * Method used to execute the theme change
     *
     * @param onChange The action to execute when the theme changed
     */
    fun changeTheme(
        onChange: (() -> Unit)? = null,
    ) {
        localSession.theme = theme.value
        onChange?.invoke()
    }

    /**
     * Method used to clear the current [localSession] session
     *
     * @param onClear The action to execute when the session has been cleaned
     */
    fun clearSession(
        onClear: (() -> Unit)? = null,
    ) {
        localSession.clear()
        onClear?.invoke()
    }

    /**
     * Method used to delete the current [localSession] session
     *
     * @param onClear The callback to execute when the session has been cleaned
     */
    fun deleteSession(
        onClear: (() -> Unit)? = null,
    ) {
        if (!isPasswordValid(password.value)) {
            passwordError.value = true
            return
        }
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteSession(
                        password = password.value
                    )
                },
                onSuccess = {
                    clearSession(
                        onClear = onClear
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}