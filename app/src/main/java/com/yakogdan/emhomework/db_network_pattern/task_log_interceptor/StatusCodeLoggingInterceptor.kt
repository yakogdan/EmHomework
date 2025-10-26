package com.yakogdan.emhomework.db_network_pattern.task_log_interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class StatusCodeLoggingInterceptor(
    private val tag: String = "HTTP_STATUS_CODE",
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e(
                /* tag = */ tag,
                /* msg = */ "HTTP FAILED: $e"
            )
            throw e
        }

        Log.i(
            /* tag = */ tag,
            /* msg = */ response.code.toString(),
        )

        return response
    }
}