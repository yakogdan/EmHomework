package com.yakogdan.emhomework

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yakogdan.emhomework.databinding.ActivityMainTimerBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivityTimer : AppCompatActivity() {

    private lateinit var binding: ActivityMainTimerBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainTimerBinding.inflate(layoutInflater)
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

    //    fun startTimer1(seconds: Int, textView: TextView) {
//        if (seconds < 0) return
//
//        val disposable = Observable.timer(1, TimeUnit.SECONDS)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                val remaining = seconds - 1
//                textView.text = remaining.toString()
//
//                if (remaining > 0) {
//                    startTimer(seconds = remaining, textView = textView)
//                } else {
//                    textView.text = "Время вышло!"
//                }
//            }
//
//        compositeDisposable.add(disposable)
//    }
}