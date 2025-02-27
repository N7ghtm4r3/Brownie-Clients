@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalComposeApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class
)

package com.tecknobit.brownie.ui.screens.adminpanel.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.admin_panel
import brownie.composeapp.generated.resources.change_language
import brownie.composeapp.generated.resources.change_theme
import brownie.composeapp.generated.resources.delete_session
import brownie.composeapp.generated.resources.local_settings
import brownie.composeapp.generated.resources.logout
import com.tecknobit.brownie.SPLASHSCREEN
import com.tecknobit.brownie.localUser
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.components.DeleteSession
import com.tecknobit.brownie.ui.components.Logout
import com.tecknobit.brownie.ui.screens.adminpanel.presentation.AdminPanelScreenViewModel
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.brownie.ui.theme.red
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.components.stepper.Step
import com.tecknobit.equinoxcompose.components.stepper.StepContent
import com.tecknobit.equinoxcompose.components.stepper.Stepper
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
import org.jetbrains.compose.resources.stringResource

class AdminPanelScreen : EquinoxScreen<AdminPanelScreenViewModel>(
    viewModel = AdminPanelScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        BrownieTheme {
            Scaffold(
                modifier = Modifier
                    .navigationBarsPadding(),
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        navigationIcon = {
                            IconButton(
                                onClick = { navigator.goBack() }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = null
                                )
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(Res.string.admin_panel),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = viewModel.snackbarHostState!!
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            top = it.calculateTopPadding()
                        )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(
                                max = 1000.dp
                            )
                            .padding(
                                all = 16.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Settings()
                        ButtonsSection()
                    }
                }
            }
        }
    }

    /**
     * The settings section to customize the [localUser] experience
     */
    @Composable
    @NonRestartableComposable
    private fun Settings() {
        SectionTitle(
            title = Res.string.local_settings
        )
        val steps = remember {
            arrayOf(
                Step(
                    stepIcon = Icons.Default.Language,
                    title = Res.string.change_language,
                    content = { ChangeLanguage() },
                    dismissAction = { visible ->
                        visible.value = false
                        viewModel.language.value = localUser.language
                    },
                    confirmAction = { visible ->
                        viewModel.changeLanguage(
                            onSuccess = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
                    }
                ),
                Step(
                    stepIcon = Icons.Default.Palette,
                    title = Res.string.change_theme,
                    content = { ChangeTheme() },
                    dismissAction = { visible ->
                        visible.value = false
                        viewModel.theme.value = localUser.theme
                    },
                    confirmAction = { visible ->
                        viewModel.changeTheme(
                            onChange = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
                    }
                )
            )
        }
        Stepper(
            steps = steps
        )
    }

    /**
     * Section to change the [localUser]'s language
     */
    @StepContent(
        number = 1
    )
    @Composable
    @NonRestartableComposable
    private fun ChangeLanguage() {
        Column(
            modifier = Modifier
                .selectableGroup()
        ) {
            LANGUAGES_SUPPORTED.entries.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = viewModel.language.value == entry.key,
                        onClick = { viewModel.language.value = entry.key }
                    )
                    Text(
                        text = entry.value
                    )
                }
            }
        }
    }

    /**
     * Section to change the [localUser]'s theme
     */
    @StepContent(
        number = 2
    )
    @Composable
    @NonRestartableComposable
    private fun ChangeTheme() {
        Column(
            modifier = Modifier
                .selectableGroup()
        ) {
            ApplicationTheme.entries.forEach { entry ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = viewModel.theme.value == entry,
                        onClick = { viewModel.theme.value = entry }
                    )
                    Text(
                        text = entry.name
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ButtonsSection() {
        ResponsiveContent(
            onExpandedSizeClass = {
                ExpandedButtons()
            },
            onMediumSizeClass = { ExpandedButtons() },
            onCompactSizeClass = { CompactButtons() }
        )
    }

    @Composable
    @NonRestartableComposable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    private fun ExpandedButtons() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ControlButtons(
                modifier = Modifier
                    .padding(
                        start = 10.dp
                    )
            )
        }
    }

    @Composable
    @CompactClassComponent
    @NonRestartableComposable
    private fun CompactButtons() {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ControlButtons(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
            )
        }
    }

    @Composable
    @CompactClassComponent
    private fun ControlButtons(
        modifier: Modifier = Modifier,
    ) {
        val logout = remember { mutableStateOf(false) }
        Button(
            modifier = modifier,
            shape = CardDefaults.shape,
            onClick = { logout.value = true }
        ) {
            Text(
                text = stringResource(Res.string.logout)
            )
        }
        Logout(
            show = logout,
            viewModel = viewModel
        )
        val deleteSession = remember { mutableStateOf(false) }
        Button(
            modifier = modifier,
            shape = CardDefaults.shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = red()
            ),
            onClick = { deleteSession.value = true }
        ) {
            ChameleonText(
                text = stringResource(Res.string.delete_session),
                backgroundColor = red()
            )
        }
        DeleteSession(
            show = deleteSession,
            viewModel = viewModel
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.language = remember { mutableStateOf(localUser.language) }
        viewModel.theme = remember { mutableStateOf(localUser.theme) }
    }

}