package com.tecknobit.brownie

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.tecknobit.equinoxcompose.session.setUpSession
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        setUpSession {
            localSession.clear()
            navigator.navigate(SPLASHSCREEN)
        }
        App()
    }
}