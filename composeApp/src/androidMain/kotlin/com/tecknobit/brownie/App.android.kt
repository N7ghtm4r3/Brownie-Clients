package com.tecknobit.brownie

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tecknobit.brownie.MainActivity.Companion.appUpdateManager
import com.tecknobit.brownie.MainActivity.Companion.launcher
import com.tecknobit.equinoxcore.utilities.AppContext
import java.util.Locale

/**
 * Method used to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
        val isUpdateAvailable = info.updateAvailability() == UPDATE_AVAILABLE
        val isUpdateSupported = info.isImmediateUpdateAllowed
        if (isUpdateAvailable && isUpdateSupported) {
            appUpdateManager.startUpdateFlowForResult(
                info,
                launcher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        } else
            startSession()
    }.addOnFailureListener {
        startSession()
    }
}

/**
 * Method used to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    val locale = Locale.forLanguageTag(localSession.language)
    Locale.setDefault(locale)
    val context = AppContext.get()
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
}

/**
 * Method used to manage correctly the back navigation from the current screen
 *
 */
@Composable
actual fun CloseApplicationOnNavBack() {
    val context = LocalActivity.current!!
    BackHandler {
        context.finishAffinity()
    }
}