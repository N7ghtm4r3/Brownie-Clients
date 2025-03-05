package com.tecknobit.brownie.helpers

import com.tecknobit.equinoxcore.network.Requester

class BrownieRequester(
    host: String,
    private val sessionId: String?,
    debugMode: Boolean = false,
) : Requester(
    host = host,
    connectionErrorMessage = "Server is temporarily unavailable",
    debugMode = debugMode,
    byPassSSLValidation = true
)