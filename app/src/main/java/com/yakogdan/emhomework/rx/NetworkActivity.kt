package com.yakogdan.emhomework.rx

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yakogdan.emhomework.databinding.ActivityNetworkBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

class NetworkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val movieId = 258687

        val apiService = ApiFactoryRx.apiService

        val single = apiService.getFilmById(movieId)

        val disposable = single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                /* onSuccess = */
                {
                    binding.tvTitle.text = it.nameOriginal
                },

                /* onError = */
                {
                    binding.tvTitle.text = "Error: ${it.message}"
                },
            )

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}


interface ApiServiceRx {

    @Headers("X-API-KEY: $API_KEY_MY")
    @GET("/api/v2.2/films/{id}")
    fun getFilmById(
        @Path("id") id: Int,
    ): Single<ItemDTO>

    companion object {
        private const val API_KEY_MY = "7560e292-123f-4ea3-98a5-f42d521bad24"
    }
}

object ApiFactoryRx {

    private const val BASE_URL = "https://kinopoiskapiunofficial.tech/"

    private val json: Json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val apiService: ApiServiceRx = retrofit.create()
}

@Serializable
data class ItemDTO(
    @SerialName("nameOriginal") val nameOriginal: String?,
)