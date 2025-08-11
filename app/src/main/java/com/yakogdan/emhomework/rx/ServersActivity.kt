package com.yakogdan.emhomework.rx

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yakogdan.emhomework.R
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

class ServersActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_servers)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // A) Если 1 из запросов падает, то все равно выводить
        val disposableA = Single.zip(
            /* source1 = */ getCardsFromServer1().onErrorReturnItem(emptyList()),
            /* source2 = */ getCardsFromServer2().onErrorReturnItem(emptyList()),
            /* zipper = */ BiFunction<List<String>, List<String>, List<String>> { list1, list2 ->
                list1 + list2
            }
        ).subscribe(
            /* onSuccess = */ { Log.d("myTag", "Zip cards A: $it") },
            /* onError = */ { Log.d("myTag", "Zip cards A error") },
        )
        compositeDisposable.add(disposableA)


        // Б) Если 1 из запросов падает, то не выводить ничего
        val disposableB = Single.zip(
            /* source1 = */ getCardsFromServer1(),
            /* source2 = */ getCardsFromServer2(),
            /* zipper = */ BiFunction<List<String>, List<String>, List<String>> { list1, list2 ->
                list1 + list2
            }
        ).subscribe(
            /* onSuccess = */ { Log.d("myTag", "Zip cards B: $it") },
            /* onError = */ { Log.d("myTag", "Zip cards B error") },
        )
        compositeDisposable.add(disposableB)
    }

    fun getCardsFromServer1(): Single<List<String>> {
        return Single.just(listOf("Server1 Card1", "Server1 Card2")).delay(1, TimeUnit.SECONDS)
//        return Single.error(IllegalArgumentException("Ошибка на сервере 1"))
    }

    fun getCardsFromServer2(): Single<List<String>> {
//        return Single.just(listOf("Server2 Card1", "Server2 Card2")).delay(1, TimeUnit.SECONDS)
        return Single.error(IllegalArgumentException("Ошибка на сервере 2"))
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}