package com.tecknobit.brownie.helpers

import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.browniecore.AUTO_RUN_AFTER_HOST_REBOOT_KEY
import com.tecknobit.browniecore.HOSTS_KEY
import com.tecknobit.browniecore.HOST_ADDRESS_KEY
import com.tecknobit.browniecore.JOIN_CODE_KEY
import com.tecknobit.browniecore.KEYWORDS_KEY
import com.tecknobit.browniecore.PROGRAM_ARGUMENTS_KEY
import com.tecknobit.browniecore.PURGE_NOHUP_OUT_AFTER_REBOOT_KEY
import com.tecknobit.browniecore.REMOVE_FROM_THE_HOST_KEY
import com.tecknobit.browniecore.SERVICES_KEY
import com.tecknobit.browniecore.SESSIONS_KEY
import com.tecknobit.browniecore.SSH_PASSWORD_KEY
import com.tecknobit.browniecore.SSH_USER_KEY
import com.tecknobit.browniecore.STATUSES_KEY
import com.tecknobit.browniecore.enums.HostStatus
import com.tecknobit.browniecore.enums.HostStatus.ONLINE
import com.tecknobit.browniecore.enums.ServiceStatus
import com.tecknobit.browniecore.enums.ServiceStatus.RUNNING
import com.tecknobit.browniecore.helpers.BrownieEndpoints.CONNECT_ENDPOINT
import com.tecknobit.browniecore.helpers.BrownieEndpoints.OVERVIEW_ENDPOINT
import com.tecknobit.browniecore.helpers.BrownieEndpoints.REBOOT_ENDPOINT
import com.tecknobit.browniecore.helpers.BrownieEndpoints.START_ENDPOINT
import com.tecknobit.browniecore.helpers.BrownieEndpoints.STOP_ENDPOINT
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
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
    var sessionId: String?,
    private val language: String,
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

    @Assembler
    private fun assembleSessionEndpoint(
        subEndpoint: String = "",
    ): String {
        return if (sessionId != null)
            "$SESSIONS_KEY/$sessionId$subEndpoint"
        else
            SESSIONS_KEY + subEndpoint
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
            put(LANGUAGE_KEY, language)
        }
        return execGet(
            endpoint = assembleHostEndpoint(),
            query = query
        )
    }

    suspend fun getHost(
        hostId: String,
    ): JsonObject {
        return execGet(
            endpoint = assembleHostEndpoint(
                hostId = hostId,
            ),
            query = createLanguageQuery()
        )
    }

    suspend fun registerHost(
        hostName: String,
        hostAddress: String,
        sshUser: String,
        sshPassword: String,
    ): JsonObject {
        val payload = createUpsertHostPayload(
            hostName = hostName,
            hostAddress = hostAddress,
            sshUser = sshUser,
            sshPassword = sshPassword
        )
        return execPost(
            endpoint = assembleHostEndpoint(),
            payload = payload
        )
    }

    suspend fun editHost(
        hostId: String,
        hostName: String,
        hostAddress: String,
        sshUser: String,
        sshPassword: String,
    ): JsonObject {
        val payload = createUpsertHostPayload(
            hostName = hostName,
            hostAddress = hostAddress,
            sshUser = sshUser,
            sshPassword = sshPassword
        )
        return execPatch(
            endpoint = assembleHostEndpoint(
                hostId = hostId
            ),
            payload = payload
        )
    }

    @Assembler
    private fun createUpsertHostPayload(
        hostName: String,
        hostAddress: String,
        sshUser: String,
        sshPassword: String,
    ): JsonObject {
        return buildJsonObject {
            put(NAME_KEY, hostName)
            put(HOST_ADDRESS_KEY, hostAddress)
            if (sshUser.isNotBlank()) {
                put(SSH_USER_KEY, sshUser)
                put(SSH_PASSWORD_KEY, sshPassword)
            }
        }
    }

    suspend fun handleHostStatus(
        host: SavedHost,
        newStatus: HostStatus,
    ): JsonObject {
        return execPatch(
            endpoint = assembleHostEndpoint(
                hostId = host.id,
                subEndpoint = if (newStatus == ONLINE)
                    START_ENDPOINT
                else
                    STOP_ENDPOINT
            ),
            query = createLanguageQuery()
        )
    }

    suspend fun rebootHost(
        host: SavedHost,
    ): JsonObject {
        return execPatch(
            endpoint = assembleHostEndpoint(
                hostId = host.id,
                subEndpoint = REBOOT_ENDPOINT
            ),
            query = createLanguageQuery()
        )
    }

    suspend fun getHostOverview(
        hostId: String,
    ): JsonObject {
        return execGet(
            endpoint = assembleHostEndpoint(
                hostId = hostId,
                subEndpoint = OVERVIEW_ENDPOINT
            ),
            query = createLanguageQuery()
        )
    }

    suspend fun unregisterHost(
        host: SavedHost,
    ): JsonObject {
        return execDelete(
            endpoint = assembleHostEndpoint(
                hostId = host.id
            ),
            query = createLanguageQuery()
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

    suspend fun addService(
        hostId: String,
        serviceName: String,
        programArguments: String,
        purgeNohupOutAfterReboot: Boolean,
        autoRunAfterHostReboot: Boolean,
    ): JsonObject {
        val payload = createServicePayload(
            serviceName = serviceName,
            programArguments = programArguments,
            purgeNohupOutAfterReboot = purgeNohupOutAfterReboot,
            autoRunAfterHostReboot = autoRunAfterHostReboot
        )
        return execPut(
            endpoint = assembleServicesEndpoint(
                hostId = hostId
            ),
            query = createLanguageQuery(),
            payload = payload
        )
    }

    suspend fun getService(
        hostId: String,
        serviceId: String,
    ): JsonObject {
        return execGet(
            endpoint = assembleServicesEndpoint(
                hostId = hostId,
                serviceId = serviceId,
            ),
            query = createLanguageQuery(),
        )
    }

    suspend fun editService(
        hostId: String,
        serviceId: String,
        serviceName: String,
        programArguments: String,
        purgeNohupOutAfterReboot: Boolean,
        autoRunAfterHostReboot: Boolean,
    ): JsonObject {
        val payload = createServicePayload(
            serviceName = serviceName,
            programArguments = programArguments,
            purgeNohupOutAfterReboot = purgeNohupOutAfterReboot,
            autoRunAfterHostReboot = autoRunAfterHostReboot
        )
        return execPatch(
            endpoint = assembleServicesEndpoint(
                hostId = hostId,
                serviceId = serviceId,
            ),
            query = createLanguageQuery(),
            payload = payload
        )
    }

    suspend fun handleServiceStatus(
        hostId: String,
        service: HostService,
        newStatus: ServiceStatus,
    ): JsonObject {
        return execPatch(
            endpoint = assembleServicesEndpoint(
                hostId = hostId,
                serviceId = service.id,
                subEndpoint = if (newStatus == RUNNING)
                    START_ENDPOINT
                else
                    STOP_ENDPOINT
            ),
            query = createLanguageQuery()
        )
    }

    suspend fun rebootService(
        hostId: String,
        service: HostService,
    ): JsonObject {
        return execPatch(
            endpoint = assembleServicesEndpoint(
                hostId = hostId,
                serviceId = service.id,
                subEndpoint = REBOOT_ENDPOINT
            ),
            query = createLanguageQuery()
        )
    }

    @Assembler
    private fun createServicePayload(
        serviceName: String,
        programArguments: String,
        purgeNohupOutAfterReboot: Boolean,
        autoRunAfterHostReboot: Boolean,
    ): JsonObject {
        return buildJsonObject {
            put(NAME_KEY, serviceName)
            put(PROGRAM_ARGUMENTS_KEY, programArguments)
            put(PURGE_NOHUP_OUT_AFTER_REBOOT_KEY, purgeNohupOutAfterReboot)
            put(AUTO_RUN_AFTER_HOST_REBOOT_KEY, autoRunAfterHostReboot)
        }
    }

    suspend fun getServices(
        hostId: String,
        page: Int,
        keywords: String,
        statuses: List<ServiceStatus>,
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
            put(LANGUAGE_KEY, language)
        }
        return execGet(
            endpoint = assembleServicesEndpoint(
                hostId = hostId
            ),
            query = query
        )
    }

    @Wrapper
    suspend fun deleteService(
        hostId: String,
        serviceId: String,
    ): JsonObject {
        return removeService(
            hostId = hostId,
            serviceId = serviceId,
            removeFromTheHost = true
        )
    }

    suspend fun removeService(
        hostId: String,
        serviceId: String,
        removeFromTheHost: Boolean = false,
    ): JsonObject {
        val query = buildJsonObject {
            put(LANGUAGE_KEY, language)
            put(REMOVE_FROM_THE_HOST_KEY, removeFromTheHost)
        }
        return execDelete(
            endpoint = assembleServicesEndpoint(
                hostId = hostId,
                serviceId = serviceId
            ),
            query = query
        )
    }

    @Assembler
    private fun assembleServicesEndpoint(
        hostId: String,
        serviceId: String = "",
        subEndpoint: String = "",
    ): String {
        var serviceEndpoint = assembleHostEndpoint(
            hostId = hostId,
            subEndpoint = "/$SERVICES_KEY"
        )
        if (serviceId.isNotEmpty())
            serviceEndpoint += "/$serviceId"
        return serviceEndpoint + subEndpoint
    }

    private fun createLanguageQuery(): JsonObject {
        return buildJsonObject {
            put(LANGUAGE_KEY, language)
        }
    }

}