package com.tecknobit.brownie.helpers

import android.content.Intent
import com.tecknobit.brownie.localSession
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider

/**
 * `INTENT_TYPE` the type of the intent to apply to correctly share the link
 */
private const val INTENT_TYPE = "text/plain"

/**
 * Method used to share the join code
 *
 * @param viewModel The support viewmodel of the screen
 */
actual fun shareJoinCode(
    viewModel: EquinoxViewModel,
) {
    val intent = Intent()
    intent.type = INTENT_TYPE
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, localSession.joinCode)
    ContextActivityProvider.getCurrentActivity()?.startActivity(Intent.createChooser(intent, null))
}