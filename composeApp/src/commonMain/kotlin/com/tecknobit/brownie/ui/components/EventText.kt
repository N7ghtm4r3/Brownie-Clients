package com.tecknobit.brownie.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcore.json.treatsAsInt
import com.tecknobit.equinoxcore.json.treatsAsString
import kotlinx.serialization.json.JsonElement
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Custom [Text] used to represent the extra information related to an event
 *
 * @param text The resource text
 * @param eventExtra The extra information related to an event to display
 */
@Composable
fun EventText(
    text: Resource,
    eventExtra: JsonElement?,
) {
    Text(
        text = if (text is PluralStringResource) {
            val extra = eventExtra.treatsAsInt()
            pluralStringResource(
                resource = text,
                quantity = extra,
                extra
            )
        } else {
            if (eventExtra != null) {
                stringResource(
                    resource = text as StringResource,
                    eventExtra.treatsAsString()
                )
            } else
                stringResource(text as StringResource)
        }
    )
}