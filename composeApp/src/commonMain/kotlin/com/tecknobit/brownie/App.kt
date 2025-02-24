package com.tecknobit.brownie

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.rubik
import brownie.composeapp.generated.resources.ubuntu_mono
import com.tecknobit.brownie.ui.screens.splashscreen.Splashscreen
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
 * `navigator` the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: Navigator

const val SPLASH_SCREEN = "Splashscreen"

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
        }
    }
}