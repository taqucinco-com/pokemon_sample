package com.taqucinco.pokemon_sample.error

sealed class HttpError: Error() {
    // HTTP通信のステータス異常
    data class Status(
        val endpoint: String,
        val method: String,
        val code: Int,
        val headers: Map<String, String>,
        val body: ByteArray,
    ) : HttpError()
}
