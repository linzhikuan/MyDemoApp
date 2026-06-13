package com.lzk.common.bean.device

sealed class ConnectionState {
    object Init : ConnectionState()

    object Connecting : ConnectionState()

    data class Connected(
        val ip: String,
        val port: Int,
    ) : ConnectionState()

    data class Disconnected(
        val reason: String?,
    ) : ConnectionState()

    data class Error(
        val message: String,
    ) : ConnectionState()
}
