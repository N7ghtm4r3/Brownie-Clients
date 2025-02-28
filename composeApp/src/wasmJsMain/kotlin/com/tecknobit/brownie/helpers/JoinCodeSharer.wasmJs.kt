package com.tecknobit.brownie.helpers

import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.join_code_copied
import com.tecknobit.brownie.localSession
import com.tecknobit.equinoxcompose.utilities.copyOnClipboard
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

/**
 * Method to share the join code
 *
 * @param viewModel The support viewmodel of the screen
 */
actual fun shareJoinCode(
    viewModel: EquinoxViewModel,
) {
    copyOnClipboard(
        content = localSession.joinCode,
        onCopy = {
            viewModel.showSnackbarMessage(
                message = Res.string.join_code_copied
            )
        }
    )
}