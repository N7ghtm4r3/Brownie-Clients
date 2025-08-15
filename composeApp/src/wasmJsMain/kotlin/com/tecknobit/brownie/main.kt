package com.tecknobit.brownie

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

/**
 * Method used to start the of `Brownie` webapp
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // AmetistaEngine.intake()
    ComposeViewport(document.body!!) {
        App()
    }
}