@file:OptIn(ExperimentalStdlibApi::class)

package com.tecknobit.brownie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.rubik
import brownie.composeapp.generated.resources.ubuntu_mono
import com.tecknobit.biometrik.rememberBiometrikState
import com.tecknobit.brownie.helpers.BrownieLocalSession
import com.tecknobit.brownie.helpers.BrownieRequester
import com.tecknobit.brownie.ui.screens.adminpanel.presenter.AdminPanelScreen
import com.tecknobit.brownie.ui.screens.connect.presenter.ConnectScreen
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.screens.splashscreen.Splashscreen
import com.tecknobit.brownie.ui.screens.upserthost.presenter.UpsertHostScreen
import com.tecknobit.brownie.ui.screens.upsertservice.presenter.UpsertServiceScreen
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.browniecore.HOST_IDENTIFIER_KEY
import com.tecknobit.equinoxcompose.session.screens.equinoxScreen
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
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
lateinit var navigator: NavHostController

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
@OptIn(ExperimentalComposeApi::class)
@Composable
fun App() {
    val biometrikState = rememberBiometrikState()
    bodyFontFamily = FontFamily(Font(Res.font.rubik))
    displayFontFamily = FontFamily(Font(Res.font.ubuntu_mono))
    navigator = rememberNavController()
    // TODO: TO USE UNIQUE THEME
    // BrownieTheme {
        NavHost(
            navController = navigator,
            startDestination = SPLASHSCREEN
        ) {
            composable(
                route = SPLASHSCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                BrownieTheme {
                    val splashscreen = equinoxScreen {
                        Splashscreen(
                            biometrikState = biometrikState
                        )
                    }
                    splashscreen.ShowContent()
                }
            }
            composable(
                route = CONNECT_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                BrownieTheme {
                    val connectScreen = equinoxScreen { ConnectScreen() }
                    connectScreen.ShowContent()
                }
            }
            composable(
                route = HOSTS_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                BrownieTheme {
                    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
                    val hostsScreen = equinoxScreen { HostsScreen() }
                    hostsScreen.ShowContent()
                    savedStateHandle?.remove<String>(IDENTIFIER_KEY)
                }
            }
            composable(
                route = ADMIN_CONTROL_PANEL_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                BrownieTheme {
                    val adminPanelScreen = equinoxScreen { AdminPanelScreen() }
                    adminPanelScreen.ShowContent()
                }
            }
            composable(
                route = UPSERT_HOST_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                BrownieTheme {
                    val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                    val hostId: String? = savedStateHandle[IDENTIFIER_KEY]
                    val upsertHostScreen = equinoxScreen {
                        UpsertHostScreen(
                            hostId = hostId
                        )
                    }
                    upsertHostScreen.ShowContent()
                    savedStateHandle.remove<String>(IDENTIFIER_KEY)
                }
            }
            composable(
                route = HOST_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                BrownieTheme {
                    val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                    val hostId: String = savedStateHandle[IDENTIFIER_KEY]!!
                    val hostScreen = equinoxScreen {
                        HostScreen(
                            hostId = hostId
                        )
                    }
                    hostScreen.ShowContent()
                }
            }
            composable(
                route = UPSERT_SERVICE_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                BrownieTheme {
                    val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                    val hostId: String = savedStateHandle[HOST_IDENTIFIER_KEY]!!
                    val hostName: String = savedStateHandle[NAME_KEY]!!
                    val serviceId: String? = savedStateHandle[IDENTIFIER_KEY]
                    val upsertServiceScreen = equinoxScreen {
                        UpsertServiceScreen(
                            hostId = hostId,
                            hostName = hostName,
                            serviceId = serviceId
                        )
                    }
                    upsertServiceScreen.ShowContent()
                    savedStateHandle.keys().forEach { savedStateHandle.remove<Any>(it) }
                }
            }
        }
    // }
    SessionFlowState.invokeOnUserDisconnected {
        localSession.clear()
        navigator.navigate(SPLASHSCREEN)
    }
}

/**
 * Method used to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
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
expect fun CloseApplicationOnNavBack()