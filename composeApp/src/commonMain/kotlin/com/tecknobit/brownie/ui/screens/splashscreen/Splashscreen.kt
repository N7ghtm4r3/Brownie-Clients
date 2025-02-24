package com.tecknobit.brownie.ui.screens.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.app_name
import com.tecknobit.brownie.CheckForUpdatesAndLaunch
import com.tecknobit.brownie.bodyFontFamily
import com.tecknobit.brownie.displayFontFamily
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import org.jetbrains.compose.resources.stringResource

class Splashscreen : EquinoxNoModelScreen() {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        BrownieTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = stringResource(Res.string.app_name),
                        fontFamily = displayFontFamily,
                        fontSize = 40.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(
                            bottom = 25.dp
                        ),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "by Tecknobit",
                        fontFamily = bodyFontFamily,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        CheckForUpdatesAndLaunch()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}