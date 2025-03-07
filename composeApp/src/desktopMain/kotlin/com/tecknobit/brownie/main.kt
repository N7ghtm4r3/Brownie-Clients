package com.tecknobit.brownie

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.app_name
import brownie.composeapp.generated.resources.logo
import com.tecknobit.equinoxcompose.session.setUpSession
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = WindowState(
            placement = WindowPlacement.Maximized
        ),
        icon = painterResource(Res.drawable.logo)
    ) {
        setUpSession {
            localSession.clear()
            navigator.navigate(SPLASHSCREEN)
        }
        App()
    }
}