package com.yakogdan.emhomework

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ConflictActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conflict)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val disposable = Observable.timer(10, TimeUnit.MILLISECONDS, Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .map {
                Log.d(
                    "HAHAHA",
                    "mapThread = ${Thread.currentThread().name}"
                ) // 2 Schedulers.newThread()
            }
            .doOnSubscribe {
                Log.d(
                    "HAHAHA",
                    "onSubscribeThread = ${Thread.currentThread().name}"
                ) // 1 Schedulers.computation()
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.single())
            .flatMap {
                Log.d(
                    "HAHAHA",
                    "flatMapThread = ${Thread.currentThread().name}"
                ) // 3 Schedulers.single()
                Observable.just(it)
                    .subscribeOn(Schedulers.io())
            }
            .subscribe {
                Log.d(
                    "HAHAHA",
                    "subscribeThread = ${Thread.currentThread().name}"
                ) // 4 Schedulers.io()
            }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}