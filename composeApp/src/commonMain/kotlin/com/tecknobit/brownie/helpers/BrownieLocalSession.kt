package com.tecknobit.brownie.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import com.tecknobit.brownie.BrownieConfig.LOCAL_STORAGE_PATH
import com.tecknobit.brownie.requester
import com.tecknobit.browniecore.JOIN_CODE_KEY
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Auto
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.HOST_ADDRESS_KEY
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.SUPPORTED_LANGUAGES
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.helpers.THEME_KEY
import com.tecknobit.kmprefs.KMPrefs

/**
 * The `BrownieLocalSession` class is useful to manage the local session data
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class BrownieLocalSession {

    protected companion object {

        /**
         * `sensitiveKeys` The keys related to the properties considered sensitive that require to be safeguarded
         *
         * @since 1.0.4
         */
        val sensitiveKeys = setOf(HOST_ADDRESS_KEY, IDENTIFIER_KEY, JOIN_CODE_KEY)

    }

    /**
     * `preferencesManager` the local preferences manager
     */
    protected val preferencesManager = KMPrefs(
        path = LOCAL_STORAGE_PATH
    )

    /**
     * `stateStore` the state store instance used to dynamically keep in memory the observable properties
     * of the local session
     *
     * @since 1.0.4
     */
    protected val stateStore: BrownieLocalSessionStateStore = BrownieLocalSessionStateStore(
        allowedKeys = setOf(THEME_KEY)
    )

    /**
     * `hostAddress` the host address which the user communicate
     */
    var hostAddress: String = ""
        private set

    /**
     * `sessionId` the current identifier of the session
     */
    var sessionId: String? = null
        private set

    /**
     * `joinCode` the join code of the session
     */
    var joinCode: String = ""
        private set

    /**
     * `language` the language of the user
     */
    var language: String = ""
        private set

    /**
     * `theme` the theme of the user
     */
    var theme: ApplicationTheme = Auto
        private set

    init {
        // TODO: TO REMOVE THIS IN NEXT VERSION
        try {
            initLocalSession()
        } catch (e: Exception) {
            clear()
        }
    }

    /**
     * Method used to initialize the local session
     */
    private fun initLocalSession() {
        setNullSafePreference<String>(
            key = HOST_ADDRESS_KEY,
            defPrefValue = "",
            prefInit = { hostAddress ->
                this.hostAddress = hostAddress
            }
        )
        setPreference<String>(
            key = IDENTIFIER_KEY,
            prefInit = { sessionId ->
                this.sessionId = sessionId
            }
        )
        setNullSafePreference<String>(
            key = JOIN_CODE_KEY,
            defPrefValue = "",
            prefInit = { joinCode ->
                this.joinCode = joinCode
            }
        )
        val currentLocaleLanguage = Locale.current.language
        setNullSafePreference<String>(
            key = LANGUAGE_KEY,
            defPrefValue = if (SUPPORTED_LANGUAGES.containsKey(currentLocaleLanguage))
                currentLocaleLanguage
            else
                DEFAULT_LANGUAGE,
            prefInit = { language ->
                this.language = language
            }
        )
        setNullSafePreference(
            key = THEME_KEY,
            defPrefValue = Auto,
            prefInit = { theme ->
                this.theme = theme
            }
        )
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
        initHostAddress(
            hostAddress = hostAddress
        )
        initSessionId(
            sessionId = sessionId
        )
        initJoinCode(
            joinCode = joinCode
        )
        val currentLocaleLanguage = Locale.current.language
        initLanguage(
            language = if (SUPPORTED_LANGUAGES.containsKey(currentLocaleLanguage))
                currentLocaleLanguage
            else
                DEFAULT_LANGUAGE
        )
        initTheme(
            theme = Auto
        )
    }

    /**
     * Method to initialize the [hostAddress] property and locally save its value with the [savePreference] method
     *
     * @param hostAddress The host address with which the user communicates
     *
     * @since 1.0.4
     */
    private fun initHostAddress(
        hostAddress: String,
    ) {
        this.hostAddress = hostAddress
        savePreference(
            key = HOST_ADDRESS_KEY,
            value = hostAddress
        )
    }

    /**
     * Method to initialize the [sessionId] property and locally save its value with the [savePreference] method
     *
     * @param sessionId The current identifier of the session
     *
     * @since 1.0.4
     */
    private fun initSessionId(
        sessionId: String,
    ) {
        this.sessionId = sessionId
        requester.sessionId = sessionId
        savePreference(
            key = IDENTIFIER_KEY,
            value = sessionId
        )
    }

    /**
     * Method to initialize the [joinCode] property and locally save its value with the [savePreference] method
     *
     * @param joinCode The join code of the session
     *
     * @since 1.0.4
     */
    private fun initJoinCode(
        joinCode: String,
    ) {
        this.joinCode = joinCode
        savePreference(
            key = JOIN_CODE_KEY,
            value = joinCode
        )
    }

    /**
     * Method to initialize the [language] property and locally save its value with the [savePreference] method
     *
     * @param language The language of the user
     *
     * @since 1.0.4
     */
    fun initLanguage(
        language: String,
    ) {
        this.language = language
        savePreference(
            key = LANGUAGE_KEY,
            value = language
        )
    }

    /**
     * Method to initialize the [theme] property and locally save its value with the [savePreference] method
     *
     * @param theme The theme chosen by the user
     *
     * @since 1.0.4
     */
    fun initTheme(
        theme: ApplicationTheme,
    ) {
        this.theme = Auto
        savePreference(
            key = THEME_KEY,
            value = theme
        )
    }

    /**
     * Method to locally save a preference.
     *
     * If the [key] is present in the [sensitiveKeys] set, before saving it, will be encrypted.
     * Similar to the [sensitiveKeys], if the [key] is present in the observable keys set will be
     * handled by the [BrownieLocalSessionStateStore]
     *
     * @param key The representative key of a preference
     * @param value The value of the preference to save
     *
     * @param T The type of the preference to save
     *
     * @since 1.0.4
     */
    protected inline fun <reified T> savePreference(
        key: String,
        value: T?,
    ) {
        preferencesManager.store(
            key = key,
            value = value,
            isSensitive = sensitiveKeys.contains(key)
        )
        stateStore.store(
            key = key,
            property = value
        )
    }

    /**
     * Method to locally retrieve a `non-nullable` preference and to initialize the related property
     *
     * If the [key] is present in the [sensitiveKeys] set, before initialize the property, will be decrypted
     *
     * @param key The representative key of a preference
     * @param defPrefValue The default value to use if the real one is not saved yet
     * @param prefInit The initialization routine to perform to use the retrieved value and to use it to initialize a property
     *
     * @param T The type of the preference to retrieve
     *
     * @since 1.0.4
     */
    @Wrapper
    protected inline fun <reified T> setNullSafePreference(
        key: String,
        defPrefValue: T,
        crossinline prefInit: (T) -> Unit,
    ) {
        setPreference(
            key = key,
            defPrefValue = defPrefValue,
            prefInit = { prefInit(it!!) }
        )
    }

    /**
     * Method to locally retrieve a preference and to initialize the related property
     *
     * If the [key] is present in the [sensitiveKeys] set, before initialize the property, will be decrypted
     *
     * @param key The representative key of a preference
     * @param defPrefValue The default value to use if the real one is not saved yet
     * @param prefInit The initialization routine to perform to use the retrieved value and to use it to initialize a property
     *
     * @param T The type of the preference to retrieve
     *
     * @since 1.0.4
     */
    protected inline fun <reified T> setPreference(
        key: String,
        defPrefValue: T? = null,
        crossinline prefInit: (T?) -> Unit,
    ) {
        preferencesManager.consumeRetrieval(
            key = key,
            defValue = defPrefValue,
            isSensitive = sensitiveKeys.contains(key),
            consume = { retrieval ->
                prefInit(retrieval)
                stateStore.store(
                    key = key,
                    property = retrieval
                )
            }
        )
    }

    /**
     * Method used to clear the current local user session
     */
    fun clear() {
        preferencesManager.clearAll()
        initLocalSession()
    }

    /**
     * Method used to observe a property of the local user
     *
     * @param key The associated key to the property to observe
     *
     * @param T The type of the property to observe
     *
     * @return the observable property as [State] of [T]
     *
     * @throws IllegalArgumentException when the requested property is not stored by the [stateStore]
     *
     * @since 1.0.4
     */
    @Composable
    fun <T> observe(
        key: String,
    ): State<T> {
        @Suppress("UNCHECKED_CAST")
        val observable: State<T>? = remember { stateStore.retrieve(key) as State<T>? }
        require(observable != null) { "Cannot observe a null property" }
        return observable
    }

    /**
     * The `EquinoxLocalUserStateStore` class allows to dynamically store properties that need to be observable in order
     * to properly react to changes
     *
     * @property allowedKeys The keys which are allowed to be stored
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     * @since 1.0.4
     */
    protected class BrownieLocalSessionStateStore(
        private val allowedKeys: Set<String> = emptySet(),
    ) {

        /**
         * `stateStore` container map of the observable properties
         */
        private val stateStore: MutableMap<String, MutableState<Any?>> = mutableMapOf()

        /**
         * Method used to store an observable property whether its key is present in the [allowedKeys], otherwise the storing
         * will be skipped. When the property has been previously stored will be updated its value
         *
         * @param key The key of the observable property to store
         * @param property The value of the observable property to store
         */
        fun <T> store(
            key: String,
            property: T?,
        ) {
            if (!allowedKeys.contains(key))
                return
            val storedProperty = stateStore.getOrPut(
                key = key,
                defaultValue = { mutableStateOf(property) }
            )
            storedProperty.value = property
        }

        /**
         * Method used to retrieve an observable property from the [stateStore] map
         *
         * @param key The key of the observable property to retrieve
         *
         * @return the observable property as nullable [State] of nullable [Any]
         */
        fun retrieve(
            key: String,
        ): State<Any?>? {
            return stateStore[key]
        }

    }

}