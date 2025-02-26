package com.tecknobit.brownie.ui.screens.upsertservice

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

class UpsertServiceScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var programArguments: MutableState<String>

    lateinit var purgeNohupOutAfterReboot: MutableState<Boolean>

}