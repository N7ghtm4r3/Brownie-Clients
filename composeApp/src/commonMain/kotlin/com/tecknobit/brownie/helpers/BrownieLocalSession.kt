package com.tecknobit.brownie.helpers

import com.tecknobit.brownie.requester
import com.tecknobit.browniecore.JOIN_CODE_KEY
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Auto
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.HOST_ADDRESS_KEY
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.helpers.THEME_KEY
import com.tecknobit.kmprefs.KMPrefs

/**
 * The `BrownieLocalSession` class is useful to manage the local session data
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class BrownieLocalSession {

    /**
     * `kmpPrefs` the local preferences manager
     */
    private val kmpPrefs = KMPrefs(
        path = "Brownie"
    )

    /**
     * `hostAddress` the host address which the user communicate
     */
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

    /**
     * `sessionId` the current identifier of the session
     */
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

    /**
     * `joinCode` the join code of the session
     */
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

    /**
     * `language` the language of the current device
     */
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

    /**
     * `theme` the theme of the current device
     */
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

    /**
     * Method used to insert and store a new local session
     *
     * @param hostAddress The host address which the user communicate
     * @param sessionId The current identifier of the session
     * @param joinCode The join code of the session
     */
    fun insertNewSession(
        hostAddress: String,
        sessionId: String,
        joinCode: String,
    ) {
        this.hostAddress = hostAddress
        this.sessionId = sessionId
        requester.sessionId = sessionId
        this.joinCode = joinCode
    }

    /**
     * Method used to initialize the local session
     */
    private fun initLocalSession() {
        hostAddress = kmpPrefs.retrieveString(
            key = HOST_ADDRESS_KEY,
            defValue = ""
        )!!
        sessionId = kmpPrefs.retrieveString(
            key = IDENTIFIER_KEY
        )
        joinCode = kmpPrefs.retrieveString(
            key = JOIN_CODE_KEY,
            defValue = ""
        )!!
        language = kmpPrefs.retrieveString(
            key = LANGUAGE_KEY,
            defValue = DEFAULT_LANGUAGE
        )!!
        theme = ApplicationTheme.getInstance(
            kmpPrefs.retrieveString(
                key = THEME_KEY
            )
        )
    }

    /**
     * Method used to clear the current local user session
     */
    fun clear() {
        kmpPrefs.clearAll()
        initLocalSession()
    }

}