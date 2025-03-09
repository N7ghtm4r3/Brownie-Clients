@file:OptIn(ExperimentalResourceApi::class)

package com.tecknobit.brownie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.rubik
import brownie.composeapp.generated.resources.ubuntu_mono
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.ametistaengine.AmetistaEngine.Companion.FILES_AMETISTA_CONFIG_PATHNAME
import com.tecknobit.brownie.helpers.BrownieLocalSession
import com.tecknobit.brownie.helpers.BrownieRequester
import com.tecknobit.brownie.ui.screens.adminpanel.presenter.AdminPanelScreen
import com.tecknobit.brownie.ui.screens.connect.presenter.ConnectScreen
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.screens.splashscreen.Splashscreen
import com.tecknobit.brownie.ui.screens.upserthost.presenter.UpsertHostScreen
import com.tecknobit.brownie.ui.screens.upsertservice.presenter.UpsertServiceScreen
import com.tecknobit.browniecore.HOST_IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

/**
 * `bodyFontFamily` the Brownie's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * `displayFontFamily` the Brownie's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 *`requester` the instance to manage the requests with the backend
 */
lateinit var requester: BrownieRequester

/**
 *`localSession` the helper to manage the local session stored locally in
 * the device
 */
val localSession = BrownieLocalSession()

/**
 * `navigator` the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: Navigator

/**
 * `SPLASHSCREEN` route to navigate to the [com.tecknobit.brownie.ui.screens.splashscreen.Splashscreen]
 */
const val SPLASHSCREEN = "Splashscreen"

/**
 * `CONNECT_SCREEN` route to navigate to the [com.tecknobit.brownie.ui.screens.connect.ConnectScreen]
 */
const val CONNECT_SCREEN = "ConnectScreen"

/**
 * `ADMIN_CONTROL_PANEL_SCREEN` route to navigate to the [com.tecknobit.brownie.ui.screens.adminpanel.presenter.AdminPanelScreen]
 */
const val ADMIN_CONTROL_PANEL_SCREEN = "AdminControlPanelScreen"

/**
 * `HostsScreen` route to navigate to the [com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen]
 */
const val HOSTS_SCREEN = "HostsScreen"

/**
 * `UPSERT_HOST_SCREEN` route to navigate to the [com.tecknobit.brownie.ui.screens.upserthost.presenter.UpsertHostScreen]
 */
const val UPSERT_HOST_SCREEN = "UpsertHostScreen"

/**
 * `HOST_SCREEN` route to navigate to the [com.tecknobit.brownie.ui.screens.host.presenter.HostScreen]
 */
const val HOST_SCREEN = "HostScreen"

/**
 * `UPSERT_SERVICE_SCREEN` route to navigate to the [com.tecknobit.brownie.ui.screens.upsertservice.presenter.UpsertServiceScreen]
 */
const val UPSERT_SERVICE_SCREEN = "UpsertServiceScreen"

/**
 * Method used to start the `Brownie`'s application
 */
@Composable
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.rubik))
    displayFontFamily = FontFamily(Font(Res.font.ubuntu_mono))
    InitAmetista()
    PreComposeApp {
        navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = SPLASHSCREEN
        ) {
            scene(
                route = SPLASHSCREEN
            ) {
                Splashscreen().ShowContent()
            }
            scene(
                route = CONNECT_SCREEN
            ) {
                ConnectScreen().ShowContent()
            }
            scene(
                route = HOSTS_SCREEN
            ) {
                HostsScreen().ShowContent()
            }
            scene(
                route = ADMIN_CONTROL_PANEL_SCREEN
            ) {
                AdminPanelScreen().ShowContent()
            }
            scene(
                route = "$UPSERT_HOST_SCREEN/{$IDENTIFIER_KEY}?"
            ) { backStackEntry ->
                val hostId = backStackEntry.path<String>(IDENTIFIER_KEY)
                UpsertHostScreen(
                    hostId = hostId
                ).ShowContent()
            }
            scene(
                route = "$HOST_SCREEN/{$IDENTIFIER_KEY}"
            ) { backStackEntry ->
                val hostId = backStackEntry.path<String>(IDENTIFIER_KEY)!!
                HostScreen(
                    hostId = hostId
                ).ShowContent()
            }
            scene(
                route = "$UPSERT_SERVICE_SCREEN/{$HOST_IDENTIFIER_KEY}/{$NAME_KEY}/{$IDENTIFIER_KEY}?"
            ) { backStackEntry ->
                val hostId = backStackEntry.path<String>(HOST_IDENTIFIER_KEY)!!
                val hostName = backStackEntry.path<String>(NAME_KEY)!!
                val serviceId = backStackEntry.path<String>(IDENTIFIER_KEY)
                UpsertServiceScreen(
                    hostId = hostId,
                    hostName = hostName,
                    serviceId = serviceId
                ).ShowContent()
            }
        }
    }
}

/**
 * Method used to initialize the Ametista system
 */
@Composable
private fun InitAmetista() {
    LaunchedEffect(Unit) {
        val ametistaEngine = AmetistaEngine.ametistaEngine
        ametistaEngine.fireUp(
            configData = Res.readBytes(FILES_AMETISTA_CONFIG_PATHNAME),
            host = AmetistaConfig.HOST,
            serverSecret = AmetistaConfig.SERVER_SECRET!!,
            applicationId = AmetistaConfig.APPLICATION_IDENTIFIER!!,
            bypassSslValidation = AmetistaConfig.BYPASS_SSL_VALIDATION,
            debugMode = false
        )
    }
}

/**
 * Method used to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method used to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = BrownieRequester(
        host = localSession.hostAddress,
        sessionId = localSession.sessionId,
        language = localSession.language
    )
    val route = if (localSession.sessionId != null)
        HOSTS_SCREEN
    else
        CONNECT_SCREEN
    setUserLanguage()
    navigator.navigate(route)
}

/**
 * Method used to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Method used to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()