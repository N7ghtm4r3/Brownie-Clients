package com.tecknobit.brownie

import OctocatKDUConfig
import UpdaterDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.app_name
import brownie.composeapp.generated.resources.app_version
import com.tecknobit.brownie.ui.theme.BrownieTheme
import org.jetbrains.compose.resources.stringResource
import java.util.Locale

/**
 * Method used to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    var launchApp by remember { mutableStateOf(true) }
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
    }
    if (launchApp)
        startSession()
}

/**
 * Method used to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    Locale.setDefault(Locale.forLanguageTag(localSession.language))
}

/**
 * Method used to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
}