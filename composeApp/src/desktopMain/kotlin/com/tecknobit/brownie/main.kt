package com.tecknobit.brownie

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.app_name
import brownie.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Method used to start the of `Brownie` desktop app
 */
fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
            state = WindowState(
                placement = WindowPlacement.Maximized
            ),
            icon = painterResource(Res.drawable.logo)
        ) {
            App()
        }
    }
}