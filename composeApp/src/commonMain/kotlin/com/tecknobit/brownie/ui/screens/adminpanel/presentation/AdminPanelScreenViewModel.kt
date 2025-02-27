package com.tecknobit.brownie.ui.screens.adminpanel.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.brownie.localUser
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

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
     * Method to execute the language change
     *
     * @param onSuccess The action to execute if the request has been successful
     */
    fun changeLanguage(
        onSuccess: (() -> Unit)? = null,
    ) {
        localUser.language = language.value
        onSuccess?.invoke()
    }

    /**
     * Method to execute the theme change
     *
     * @param onChange The action to execute when the theme changed
     */
    fun changeTheme(
        onChange: (() -> Unit)? = null,
    ) {
        localUser.theme = theme.value
        onChange?.invoke()
    }

    /**
     * Method to clear the current [localUser] session
     *
     * @param onClear The action to execute when the session has been cleaned
     */
    fun clearSession(
        onClear: (() -> Unit)? = null,
    ) {
        localUser.clear()
        // TODO: TO SET
        /*requester.setUserCredentials(
            userId = null,
            userToken = null
        )*/
        onClear?.invoke()
    }

    fun deleteSession(
        onClear: (() -> Unit)? = null,
    ) {
        // TODO: TO MAKE THE REQUEST THEN
        clearSession(
            onClear = onClear
        )
    }

}