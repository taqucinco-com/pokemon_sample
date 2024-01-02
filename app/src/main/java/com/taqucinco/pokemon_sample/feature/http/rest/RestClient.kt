package com.taqucinco.pokemon_sample.feature.http.rest

import com.taqucinco.pokemon_sample.error.HttpError
import com.taqucinco.pokemon_sample.extension.buildQueryParameters
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Callback
import javax.inject.Inject
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume

class RestClient @Inject constructor(
    private val okHttpClient: okhttp3.Call.Factory, // OkHttpClient
): RestClientInterface {
    override suspend fun get(
        endpoint: String,
        queryParams: Map<String, Any>,
        headers: Map<String, String>
    ): RestClientInterface.Response  = suspendCancellableCoroutine { continuous ->
        val reqBuilder = Request.Builder().url(endpoint + (queryParams.buildQueryParameters()))
        headers
            .map { Pair(it.key, it.value) }
            .forEach { reqBuilder.addHeader(it.first, it.second) }

        val call = okHttpClient.newCall(reqBuilder.build())
        try {
            call.enqueue(object : Callback {
                override fun onResponse(call: okhttp3.Call, response: Response) {
                    // byte streamのアクセスは一度しかできないためコピーする
                    val bytes = response.body?.bytes() ?: ByteArray(0)
                    val resume = RestClientInterface.Response(
                        status = response.code,
                        headers = response.headers.toMap(),
                        data = bytes
                    )
                    when (response.code) {
                        in 200 until 400 -> {
                            if (continuous.isActive) continuous.resume(resume)
                        }
                        else -> {
                            val httpError = HttpError.Status(
                                endpoint = endpoint,
                                method = call.request().method,
                                code = response.code,
                                headers = response.headers.toMap(),
                                body = bytes
                            )
                            continuous.cancel(httpError)
                        }
                    }
                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    continuous.cancel(e)
                }
            })
        } catch (e: Throwable) {
            continuous.cancel(e)
        }
    }

    override suspend fun <T> post(
        endpoint: String,
        body: T,
        headers: Map<String, String>
    ): RestClientInterface.Response {
        TODO("Not yet implemented")
    }
}