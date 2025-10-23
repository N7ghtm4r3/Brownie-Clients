package com.tecknobit.brownie.helpers

import androidx.navigation.NavHostController
import com.tecknobit.brownie.ui.screens.adminpanel.presenter.AdminPanelScreen
import com.tecknobit.brownie.ui.screens.connect.presenter.ConnectScreen
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.host.data.SavedHostOverview
import com.tecknobit.brownie.ui.screens.host.presenter.HostScreen
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
import com.tecknobit.brownie.ui.screens.hosts.presenter.HostsScreen
import com.tecknobit.brownie.ui.screens.splashscreen.Splashscreen
import com.tecknobit.brownie.ui.screens.upserthost.presenter.UpsertHostScreen
import com.tecknobit.browniecore.HOST_IDENTIFIER_KEY
import com.tecknobit.equinoxcompose.annotations.DestinationScreen
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY

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
 * Method used to navigate to the [Splashscreen]
 */
@DestinationScreen(Splashscreen::class)
fun navToSplashscreen() {
    navigator.navigate(SPLASHSCREEN)
}

@DestinationScreen(HostsScreen::class)
fun navToHostsScreen() {
    navigator.navigate(HOSTS_SCREEN)
}

@DestinationScreen(HostScreen::class)
fun navToHostScreen(
    host: SavedHostImpl,
) {
    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.let {
        savedStateHandle[IDENTIFIER_KEY] = host.id
        navigator.navigate(HOST_SCREEN)
    }
}

@DestinationScreen(UpsertHostScreen::class)
fun navToUpsertHostScreen(
    host: SavedHostImpl? = null,
) {
    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.let {
        host?.let {
            savedStateHandle[IDENTIFIER_KEY] = host.id
        }
        navigator.navigate(UPSERT_HOST_SCREEN)
    }
}

@DestinationScreen(UpsertHostScreen::class)
fun navToUpsertServiceScreen(
    savedHostOverview: SavedHostOverview,
    service: HostService? = null,
) {
    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.let {
        savedStateHandle[HOST_IDENTIFIER_KEY] = savedHostOverview.id
        savedStateHandle[NAME_KEY] = savedHostOverview.name
        service?.let {
            savedStateHandle[IDENTIFIER_KEY] = service.id
        }
    }
    navigator.navigate(UPSERT_SERVICE_SCREEN)
}

@DestinationScreen(AdminPanelScreen::class)
fun navToAdminPanelScreen() {
    navigator.navigate(ADMIN_CONTROL_PANEL_SCREEN)
}