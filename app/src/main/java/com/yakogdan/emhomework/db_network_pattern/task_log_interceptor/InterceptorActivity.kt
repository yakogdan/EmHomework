package com.yakogdan.emhomework.db_network_pattern.task_log_interceptor

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yakogdan.emhomework.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class InterceptorActivity : AppCompatActivity() {

    private val kinoApiService = KinoApiFactory.apiService

    private val btnGetData by lazy {
        findViewById<Button>(R.id.btnGetData)
    }

    private val tvInterceptor by lazy {
        findViewById<TextView>(R.id.tvInterceptor)
    }

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_interceptor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnGetData.setOnClickListener {

            val data = kinoApiService.getFilmById(id = 258687)
            val disposable = data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        tvInterceptor.text = it.nameOriginal ?: "Empty"
                    },
                    {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                )
            compositeDisposable.add(disposable)
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}