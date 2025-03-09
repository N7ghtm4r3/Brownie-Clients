package com.tecknobit.brownie.helpers

import com.tecknobit.ametistaengine.AmetistaEngine.Companion.ametistaEngine
import com.tecknobit.brownie.ui.screens.host.data.HostService
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost
import com.tecknobit.brownie.ui.screens.hosts.data.SavedHost.SavedHostImpl
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
import com.tecknobit.browniecore.STATUS_KEY
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
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.helpers.PASSWORD_KEY
import com.tecknobit.equinoxcore.helpers.SERVER_SECRET_KEY
import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.Companion.BASE_EQUINOX_ENDPOINT
import com.tecknobit.equinoxcore.network.RequestMethod.DELETE
import com.tecknobit.equinoxcore.network.RequestMethod.GET
import com.tecknobit.equinoxcore.network.RequestMethod.PATCH
import com.tecknobit.equinoxcore.network.RequestMethod.POST
import com.tecknobit.equinoxcore.network.RequestMethod.PUT
import com.tecknobit.equinoxcore.network.Requester
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_KEY
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

/**
 * The `BrownieRequester` class is useful to communicate with Brownie's backend
 *
 * @param host The host address where is running the backend
 * @param sessionId The identifier of the session
 * @param language The language of the device
 * @param debugMode Whether the requester is still in development and who is developing needs the log
 * of the requester's workflow, if it is enabled all the details of the requests sent and the errors
 * occurred will be printed in the console
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 */
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

    init {
        attachInterceptorOnRequest {
            ametistaEngine.notifyNetworkRequest()
        }
    }

    /**
     * Method used to change, during the runtime for example when the session changed, the host address to make the
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

    /**
     * Request to connect to a specific session
     *
     * @param serverSecret The secret of the server
     * @param joinCode The code used to join in the session
     * @param password The password which protects the session accesses
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/connect", method = PUT)
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

    /**
     * Request to create a new session
     *
     * @param serverSecret The secret of the server
     * @param password The password which protects the session accesses
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions", method = POST)
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

    /**
     * Request to delete an existing session
     *
     * @param password The password which protects the session accesses
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}", method = DELETE)
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

    /**
     * Method used to assemble an endpoint related to sessions
     *
     * @param subEndpoint The sub endpoint to execute a specific action
     *
     * @return the session endpoint as [String]
     */
    @Assembler
    private fun assembleSessionEndpoint(
        subEndpoint: String = "",
    ): String {
        return if (sessionId != null)
            "$SESSIONS_KEY/$sessionId$subEndpoint"
        else
            SESSIONS_KEY + subEndpoint
    }

    /**
     * Request to get the hosts related to the session
     *
     * @param page The page to request
     * @param keywords The keywords to filter the hosts to retrieve
     * @param statuses The statuses to filter the hosts to retrieve
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts", method = GET)
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

    /**
     * Request to get the current status of the specified hosts
     *
     * @param currentHosts The hosts to monitor their status
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/status", method = GET)
    suspend fun getHostsStatus(
        currentHosts: List<SavedHostImpl>?,
    ): JsonObject {
        val query = buildJsonObject {
            putJsonArray(HOSTS_KEY) {
                currentHosts?.forEach { host ->
                    add(host.id)
                }
            }
        }
        return execGet(
            endpoint = assembleHostEndpoint(
                subEndpoint = "/$STATUS_KEY"
            ),
            query = query
        )
    }

    /**
     * Request to register a new host
     *
     * @param hostName The name of the host
     * @param hostAddress The address of the host
     * @param sshUser The user used in the SSH connection
     * @param sshPassword The password used in the SSH connection
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts", method = POST)
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

    /**
     * Request to get a specific host
     *
     * @param hostId The identifier of the host
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/{host_id}", method = GET)
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

    /**
     * Request to edit an existing host
     *
     * @param hostName The name of the host
     * @param hostAddress The address of the host
     * @param sshUser The user used in the SSH connection
     * @param sshPassword The password used in the SSH connection
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/{host_id}", method = PATCH)
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

    /**
     * Method used to assemble the payload for the [registerHost] and [editHost] requests
     *
     * @param hostName The name of the host
     * @param hostAddress The address of the host
     * @param sshUser The user used in the SSH connection
     * @param sshPassword The password used in the SSH connection
     *
     * @return the payload as [JsonObject]
     */
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

    /**
     * Request to handle the status of a host
     *
     * @param host The host to handle its status
     * @param newStatus The status to set
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/(start or stop)",
        method = PATCH
    )
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

    /**
     * Request to reboot a host
     *
     * @param host The host to reboot
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/{host_id}/reboot", method = PATCH)
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

    /**
     * Request to get the host overview
     *
     * @param hostId The identifier of the host
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/{host_id}/overview", method = GET)
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

    /**
     * Request to unregister a host from the system
     *
     * @param host The host to unregister
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/{host_id}", method = DELETE)
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

    /**
     * Method used to assemble an endpoint related to host section
     *
     * @param hostId The identifier of the host
     * @param subEndpoint The sub endpoint to execute a specific action
     *
     * @return the host endpoint as [String]
     */
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

    /**
     * Request to add a service to a host
     *
     * @param hostId The identifier of the host
     * @param serviceName The name of the service
     * @param programArguments The arguments of the program
     * @param purgeNohupOutAfterReboot Whether the `nohup.out` file related to the service must be
     * deleted at each service start
     * @param autoRunAfterHostReboot Whether the service must be automatically restarted after the
     * host start or the host restart
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services", method = PUT)
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

    /**
     * Request to get a service
     *
     * @param hostId The identifier of the host
     * @param serviceId The identifier of the service
     */
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services/{service_id}",
        method = GET
    )
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

    /**
     * Request to edit an existing service
     *
     * @param hostId The identifier of the host
     * @param serviceName The name of the service
     * @param programArguments The arguments of the program
     * @param purgeNohupOutAfterReboot Whether the `nohup.out` file related to the service must be
     * deleted at each service start
     * @param autoRunAfterHostReboot Whether the service must be automatically restarted after the
     * host start or the host restart
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services/{service_id}",
        method = PATCH
    )
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

    /**
     * Request to handle the status of a service
     *
     * @param hostId The host identifier
     * @param service The service to handle its status
     * @param newStatus The status to set
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services/{service_id}/(start or stop)",
        method = PATCH
    )
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

    /**
     * Request to reboot a service
     *
     * @param hostId The host identifier
     * @param service The service to handle its status
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services/{service_id}/reboot",
        method = PATCH
    )
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

    /**
     * Method used to assemble the payload for the [addService] and [editService] requests
     *
     * @param serviceName The name of the service
     * @param programArguments The arguments of the program
     * @param purgeNohupOutAfterReboot Whether the `nohup.out` file related to the service must be
     * deleted at each service start
     * @param autoRunAfterHostReboot Whether the service must be automatically restarted after the
     * host start or the host restart
     *
     * @return the payload as [JsonObject]
     */
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

    /**
     * Request to get the services of a host
     *
     * @param page The page to request
     * @param keywords The keywords to filter the services to retrieve
     * @param statuses The statuses to filter the services to retrieve
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services", method = GET)
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

    /**
     * Request to get the current status of the specified services
     *
     * @param hostId The identifier of the host
     * @param currentServices The services to monitor their status
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services/status",
        method = GET
    )
    suspend fun getServicesStatus(
        hostId: String,
        currentServices: List<HostService>?,
    ): JsonObject {
        val query = buildJsonObject {
            putJsonArray(SERVICES_KEY) {
                currentServices?.forEach { host ->
                    add(host.id)
                }
            }
        }
        return execGet(
            endpoint = assembleServicesEndpoint(
                hostId = hostId,
                subEndpoint = "/$STATUS_KEY"
            ),
            query = query
        )
    }

    /**
     * Request to delete the service
     *
     * @param hostId The identifier of the host
     * @param serviceId The identifier of the service
     *
     * @return the response of the request as [JsonObject]
     */
    @Wrapper
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services/{service_id}",
        method = DELETE
    )
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

    /**
     * Request to delete the service
     *
     * @param hostId The identifier of the host
     * @param serviceId The identifier of the service
     * @param removeFromTheHost Whether the removing include also the removing from the filesystem
     * of the host
     *
     * @return the response of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/sessions/{session_id}/hosts/{host_id}/services/{service_id}",
        method = DELETE
    )
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

    /**
     * Method used to assemble an endpoint related to services
     *
     * @param hostId The identifier of the host
     * @param serviceId The identifier of the service
     * @param subEndpoint The sub endpoint to execute a specific action
     *
     * @return the service endpoint as [String]
     */
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

    /**
     * Method used to assemble the query with language parameter
     *
     * @return the language query as [JsonObject]
     */
    @Assembler
    private fun createLanguageQuery(): JsonObject {
        return buildJsonObject {
            put(LANGUAGE_KEY, language)
        }
    }

}