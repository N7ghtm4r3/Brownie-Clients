package com.tecknobit.brownie.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun EventText(
    text: Resource,
    eventExtra: Any?,
) {
    Text(
        text = if (text is PluralStringResource) {
            val extra = eventExtra as Int
            pluralStringResource(
                resource = text,
                quantity = extra,
                extra
            )
        } else {
            if (eventExtra != null) {
                stringResource(
                    resource = text as StringResource,
                    eventExtra
                )
            } else
                stringResource(text as StringResource)
        }
    )
}