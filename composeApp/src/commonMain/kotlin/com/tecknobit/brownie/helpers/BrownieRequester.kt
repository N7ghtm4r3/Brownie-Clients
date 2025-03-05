package com.tecknobit.brownie.helpers

import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.browniecore.HOSTS_KEY
import com.tecknobit.browniecore.JOIN_CODE_KEY
import com.tecknobit.browniecore.KEYWORDS_KEY
import com.tecknobit.browniecore.SESSIONS_KEY
import com.tecknobit.browniecore.STATUSES_KEY
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.helpers.BrownieEndpoints.CONNECT_ENDPOINT
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.helpers.PASSWORD_KEY
import com.tecknobit.equinoxcore.helpers.SERVER_SECRET_KEY
import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.Companion.BASE_EQUINOX_ENDPOINT
import com.tecknobit.equinoxcore.network.Requester
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_KEY
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

class BrownieRequester(
    host: String,
    private val sessionId: String?,
    debugMode: Boolean = false,
) : Requester(
    host = host,
    connectionErrorMessage = "Server is temporarily unavailable",
    debugMode = debugMode,
    byPassSSLValidation = true
) {

    /**
     * Method to change, during the runtime for example when the session changed, the host address to make the
     * requests
     *
     * @param host The new host address to use
     */
    override fun changeHost(
        host: String,
    ) {
        super.changeHost(
            host = "$host$BASE_EQUINOX_ENDPOINT"
        )
    }

    suspend fun connectToSession(
        serverSecret: String,
        joinCode: String,
        password: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(SERVER_SECRET_KEY, serverSecret)
            put(JOIN_CODE_KEY, joinCode)
            put(PASSWORD_KEY, password)
        }
        return execPut(
            endpoint = assembleSessionEndpoint(
                subEndpoint = CONNECT_ENDPOINT
            ),
            payload = payload
        )
    }

    suspend fun createSession(
        serverSecret: String,
        password: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(SERVER_SECRET_KEY, serverSecret)
            put(PASSWORD_KEY, password)
        }
        return execPost(
            endpoint = assembleSessionEndpoint(),
            payload = payload
        )
    }

    suspend fun deleteSession(
        password: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(PASSWORD_KEY, password)
        }
        return execDelete(
            endpoint = assembleSessionEndpoint(),
            payload = payload
        )
    }

    suspend fun getHosts(
        page: Int,
        keywords: String,
        statuses: List<HostStatus>,
    ): JsonObject {
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            if (keywords.isNotEmpty())
                put(KEYWORDS_KEY, keywords)
            putJsonArray(STATUSES_KEY) {
                statuses.forEach { status ->
                    add(status.name)
                }
            }
        }
        return execGet(
            endpoint = assembleHostEndpoint(),
            query = query
        )
    }

    suspend fun unregisterHost(
        host: SavedHost,
    ): JsonObject {
        return execDelete(
            endpoint = assembleHostEndpoint(
                hostId = host.id
            )
        )
    }

    @Assembler
    private fun assembleHostEndpoint(
        hostId: String = "",
        subEndpoint: String = "",
    ): String {
        var hostEndpoint = assembleSessionEndpoint(
            subEndpoint = "/$HOSTS_KEY"
        )
        if (hostId.isNotEmpty())
            hostEndpoint += "/$hostId"
        return hostEndpoint + subEndpoint
    }

    @Assembler
    private fun assembleSessionEndpoint(
        subEndpoint: String = "",
    ): String {
        return if (sessionId != null)
            "$SESSIONS_KEY/$sessionId$subEndpoint"
        else
            SESSIONS_KEY + subEndpoint
    }

}