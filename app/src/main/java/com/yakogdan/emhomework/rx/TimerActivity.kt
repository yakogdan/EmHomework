package com.yakogdan.emhomework.rx

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yakogdan.emhomework.databinding.ActivityTimerBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnStartTimer.setOnClickListener {
            val disposable = startTimer(seconds = 5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { seconds ->
                    binding.tvTimer.text = if (seconds > 0) {
                        seconds.toString()
                    } else {
                        "Время вышло!"
                    }
                }

            compositeDisposable.add(disposable)
        }
    }

    fun startTimer(seconds: Long) = Observable
        .intervalRange(
            /* start = */ 0,
            /* count = */ seconds + 1,
            /* initialDelay = */ 0,
            /* period = */ 1,
            /* unit = */ TimeUnit.SECONDS
        )
        .map { seconds - it }


    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}