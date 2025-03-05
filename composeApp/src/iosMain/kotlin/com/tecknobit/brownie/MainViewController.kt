package com.tecknobit.brownie

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.equinoxcompose.session.setUpSession

fun MainViewController() = ComposeUIViewController {
    setUpSession {
        localSession.clear()
        navigator.navigate(SPLASHSCREEN)
    }
    App()
}