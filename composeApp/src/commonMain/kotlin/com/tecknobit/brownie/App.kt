package com.tecknobit.brownie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.rubik
import brownie.composeapp.generated.resources.ubuntu_mono
import com.tecknobit.brownie.ui.screens.adminpanel.presenter.AdminPanelScreen
import com.tecknobit.brownie.ui.screens.connect.ConnectScreen
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.screens.splashscreen.Splashscreen
import com.tecknobit.brownie.ui.screens.upserthost.presenter.UpsertHostScreen
import com.tecknobit.brownie.ui.screens.upsertservice.presenter.UpsertServiceScreen
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
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
 *`localUser` the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = EquinoxLocalUser(
    localStoragePath = "Brownie"
)

/**
 * `navigator` the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: Navigator

const val SPLASHSCREEN = "Splashscreen"

const val CONNECT_SCREEN = "ConnectScreen"

const val ADMIN_CONTROL_PANEL_SCREEN = "AdminControlPanelScreen"

const val HOSTS_SCREEN = "HostsScreen"

const val UPSERT_HOST_SCREEN = "UpsertHostScreen"

const val HOST_SCREEN = "HostScreen"

const val UPSERT_SERVICE_SCREEN = "UpsertServiceScreen"

@Composable
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.rubik))
    displayFontFamily = FontFamily(Font(Res.font.ubuntu_mono))
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
                route = "$UPSERT_SERVICE_SCREEN/{$NAME_KEY}/{$IDENTIFIER_KEY}?"
            ) { backStackEntry ->
                val hostName = backStackEntry.path<String>(NAME_KEY)!!
                val serviceId = backStackEntry.path<String>(IDENTIFIER_KEY)
                UpsertServiceScreen(
                    hostName = hostName,
                    serviceId = serviceId
                ).ShowContent()
            }
        }
    }
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    // TODO: TO SET
    /*requester = RefyRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    val route =
        if (!localUser.userId.isNullOrBlank()) { // TODO: TO USE localUser.isAuthenticated INSTEAD
            MainScope().launch {
                requester.sendRequest(
                    request = {
                        getDynamicAccountData()
                    },
                    onSuccess = { response ->
                        localUser.updateDynamicAccountData(
                            dynamicData = response.toResponseData()
                        )
                    },
                    onFailure = {}
                )
            }
            HOME_SCREEN
        } else
            AUTH_SCREEN*/
    setUserLanguage()
    // TODO: MAKE THE REAL NAVIGATION
    navigator.navigate(CONNECT_SCREEN)
}

/**
 * Method to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()