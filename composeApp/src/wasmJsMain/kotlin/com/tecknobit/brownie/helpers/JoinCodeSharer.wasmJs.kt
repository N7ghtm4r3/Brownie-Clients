package com.tecknobit.brownie.helpers

import brownie.composeapp.generated.resources.Res
import brownie.composeapp.generated.resources.join_code_copied
import com.tecknobit.brownie.localSession
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcompose.utilities.copyOnClipboard

/**
 * Method used to share the join code
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