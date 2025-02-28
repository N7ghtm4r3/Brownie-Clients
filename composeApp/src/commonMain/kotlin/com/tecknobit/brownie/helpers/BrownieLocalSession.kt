package com.tecknobit.brownie.helpers

import com.tecknobit.browniecore.JOIN_CODE_KEY
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Auto
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.HOST_ADDRESS_KEY
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.helpers.THEME_KEY
import com.tecknobit.kmprefs.KMPrefs
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BrownieLocalSession {

    private val kmpPrefs = KMPrefs(
        path = "Brownie"
    )

    var hostAddress: String = ""
        set(value) {
            if (field != value) {
                kmpPrefs.storeString(
                    key = HOST_ADDRESS_KEY,
                    value = value
                )
                field = value
            }
        }

    var sessionId: String? = null
        set(value) {
            if (field != value) {
                kmpPrefs.storeString(
                    key = IDENTIFIER_KEY,
                    value = value
                )
                field = value
            }
        }

    var joinCode: String = ""
        set(value) {
            if (field != value) {
                kmpPrefs.storeString(
                    key = JOIN_CODE_KEY,
                    value = value
                )
                field = value
            }
        }

    var language: String = ""
        set(value) {
            if (field != value) {
                kmpPrefs.storeString(
                    key = LANGUAGE_KEY,
                    value = value
                )
                field = value
            }
        }

    var theme: ApplicationTheme = Auto
        set(value) {
            if (field != value) {
                kmpPrefs.storeString(
                    key = THEME_KEY,
                    value = value.name
                )
                field = value
            }
        }

    init {
        initLocalSession()
    }

    @OptIn(ExperimentalUuidApi::class) // TODO: TO REMOVE
    private fun initLocalSession() {
        hostAddress = kmpPrefs.retrieveString(
            key = HOST_ADDRESS_KEY,
            defValue = ""
        )!!
        joinCode = kmpPrefs.retrieveString(
            key = JOIN_CODE_KEY,
            defValue = Uuid.random().toHexString() // TODO: TO USE "" INSTEAD 
        )!!
        language = kmpPrefs.retrieveString(
            key = LANGUAGE_KEY,
            defValue = ""
        )!!
        theme = ApplicationTheme.getInstance(
            kmpPrefs.retrieveString(
                key = THEME_KEY
            )
        )
    }

    /**
     * Method to clear the current local user session
     */
    fun clear() {
        kmpPrefs.clearAll()
        initLocalSession()
    }

}