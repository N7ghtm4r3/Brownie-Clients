package com.tecknobit.brownie

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.session.setUpSession

fun MainViewController() {
    AmetistaEngine.intake()
    ComposeUIViewController {
        setUpSession {
            localSession.clear()
            navigator.navigate(SPLASHSCREEN)
        }
        App()
    }
}