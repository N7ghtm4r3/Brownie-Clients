package com.tecknobit.brownie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.session.setUpSession
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider

class MainActivity : ComponentActivity() {

    companion object {

        /**
         * `appUpdateManager` the manager to check if there is an update available
         */
        lateinit var appUpdateManager: AppUpdateManager

        /**
         * `launcher` the result registered for [appUpdateManager] and the action to execute if fails
         */
        lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>

    }

    init {
        launcher = registerForActivityResult(StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK)
                startSession()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextActivityProvider.setCurrentActivity(this)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        AmetistaEngine.intake()
        setContent {
            enableEdgeToEdge()
            setUpSession {
                localSession.clear()
                navigator.navigate(SPLASHSCREEN)
            }
            App()
        }
    }

}