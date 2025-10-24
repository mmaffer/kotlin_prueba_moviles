package com.example.metrolima.data.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class MockAlertsInterceptor(context: Context) : Interceptor {
    private val alertsJson: String = context.assets
        .open(ASSET_NAME)
        .bufferedReader()
        .use { it.readText() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.url.toUri().toString()
        return if (uri.endsWith("alerts")) {
            Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(alertsJson.toResponseBody(JSON_MEDIA_TYPE))
                .build()
        } else {
            chain.proceed(request)
        }
    }

    companion object {
        private const val ASSET_NAME = "alerts.json"
        private val JSON_MEDIA_TYPE = "application/json".toMediaType()
    }
}
