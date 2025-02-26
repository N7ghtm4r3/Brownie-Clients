package com.tecknobit.brownie.ui.screens.upsertservice.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import brownie.composeapp.generated.resources.Res.string
import brownie.composeapp.generated.resources.add_service
import brownie.composeapp.generated.resources.edit_service
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.upsertservice.components.ProgramArgumentsSection
import com.tecknobit.brownie.ui.screens.upsertservice.components.ServiceNameSection
import com.tecknobit.brownie.ui.screens.upsertservice.components.ServiceSettings
import com.tecknobit.brownie.ui.screens.upsertservice.presentation.UpsertServiceScreenViewModel
import com.tecknobit.brownie.ui.theme.AppTypography
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
class UpsertServiceScreen(
    private val hostName: String,
    serviceId: String?,
) : EquinoxScreen<UpsertServiceScreenViewModel>(
    viewModel = UpsertServiceScreenViewModel(
        serviceId = serviceId
    )
) {

    private val isEditing = serviceId != null

    private lateinit var service: State<HostService?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        BrownieTheme {
            ManagedContent(
                viewModel = viewModel,
                initialDelay = 500,
                loadingRoutine = { service.value != null },
                content = {
                    CollectStatesAfterLoading()
                    Scaffold(
                        modifier = Modifier
                            .navigationBarsPadding(),
                        topBar = {
                            MediumTopAppBar(
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
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
                                    Column {
                                        Text(
                                            text = hostName,
                                            style = AppTypography.labelMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = stringResource(
                                                resource = if (isEditing)
                                                    string.edit_service
                                                else
                                                    string.add_service
                                            ),
                                            style = AppTypography.headlineSmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
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
                                    top = it.calculateTopPadding() + 16.dp,
                                    bottom = 16.dp
                                )
                                .padding(
                                    horizontal = 16.dp
                                )
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Form()
                        }
                    }
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Form() {
        Column(
            modifier = Modifier
                .widthIn(
                    max = 1000.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            ServiceNameSection(
                viewModel = viewModel
            )
            ProgramArgumentsSection(
                viewModel = viewModel
            )
            ServiceSettings(
                viewModel = viewModel
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveService()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        service = viewModel.service.collectAsState()
        viewModel.serviceNameError = remember { mutableStateOf(false) }
    }

    @Composable
    override fun CollectStatesAfterLoading() {
        viewModel.serviceName = remember {
            mutableStateOf(
                if (isEditing)
                    service.value!!.name
                else
                    ""
            )
        }
        viewModel.programArguments = remember {
            mutableStateOf(
                if (isEditing)
                    service.value!!.configuration.programArguments
                else
                    ""
            )
        }
        viewModel.purgeNohupOutAfterReboot = remember {
            mutableStateOf(
                if (isEditing)
                    service.value!!.configuration.purgeNohupOutAfterReboot
                else
                    true
            )
        }
        viewModel.autoRunAfterHostReboot = remember {
            mutableStateOf(
                if (isEditing)
                    service.value!!.configuration.autoRunAfterHostReboot
                else
                    true
            )
        }
    }

}