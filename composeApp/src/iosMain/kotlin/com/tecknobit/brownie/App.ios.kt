package com.tecknobit.brownie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.delay

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    LaunchedEffect(Unit) {
        delay(1000)
        startSession()
    }
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    // TODO: TO SET
    /*val locale = NSLocale(
        localeIdentifier = localSession.language
    )
    NSUserDefaults.standardUserDefaults.setObject(
        value = locale,
        forKey = "AppleLanguages"
    )
    NSUserDefaults.standardUserDefaults.synchronize()*/
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
}