package com.yakogdan.emhomework.db_network_pattern.task_log_interceptor

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface KinoApiService {

    @Headers("X-API-KEY: $API_KEY_MY")
    @GET("/api/v2.2/films/{id}")
    fun getFilmById(
        @Path("id") id: Int,
    ): Single<ItemDTO>

    companion object {
        private const val API_KEY_MY = "7560e292-123f-4ea3-98a5-f42d521bad24"
    }
}

object KinoApiFactory {

    private const val BASE_URL = "https://kinopoiskapiunofficial.tech/"

    private val loggingInterceptor = StatusCodeLoggingInterceptor()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val json: Json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: KinoApiService = retrofit.create()
}

@Serializable
data class ItemDTO(
    @SerialName("nameOriginal") val nameOriginal: String?,
)