package com.tecknobit.brownie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.rubik
import brownie.composeapp.generated.resources.ubuntu_mono
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.screens.splashscreen.Splashscreen
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
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

const val SPLASH_SCREEN = "Splashscreen"

const val HOSTS_SCREEN = "HostsScreen"

@Composable
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.rubik))
    displayFontFamily = FontFamily(Font(Res.font.ubuntu_mono))
    PreComposeApp {
        navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = SPLASH_SCREEN
        ) {
            scene(
                route = SPLASH_SCREEN
            ) {
                Splashscreen().ShowContent()
            }
            scene(
                route = HOSTS_SCREEN
            ) {
                HostsScreen().ShowContent()
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
    navigator.navigate(HOSTS_SCREEN)
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