package com.tecknobit.brownie.helpers

import com.tecknobit.brownie.localSession
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

/**
 * Method to share the join code
 *
 * @param viewModel The support viewmodel of the screen
 */
actual fun shareJoinCode(
    viewModel: EquinoxViewModel,
) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val activityViewController = UIActivityViewController(localSession.joinCode, null)
    viewController?.presentViewController(activityViewController, true, null)
}