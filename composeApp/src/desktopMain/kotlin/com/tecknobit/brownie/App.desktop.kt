package com.tecknobit.brownie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    /*var launchApp by remember { mutableStateOf(true) }
    BrownieTheme {
        UpdaterDialog(
            config = OctocatKDUConfig(
                locale = Locale.getDefault(),
                appName = stringResource(Res.string.app_name),
                currentVersion = stringResource(Res.string.app_version),
                onUpdateAvailable = { launchApp = false },
                dismissAction = { launchApp = true }
            )
        )
    }*/ // TODO: TO SET
    //if (launchApp)
    startSession()
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    // TODO: TO SET
    // Locale.setDefault(Locale.forLanguageTag(localUser.language))
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
}