package com.yakogdan.emhomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

class RecyclerActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    val subject = PublishSubject.create<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recycler)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val numbers = (0..20).toList()

        val adapter = NumberAdapter(numbers = numbers) { position ->
            subject.onNext(position)
        }

        recyclerView.adapter = adapter

        val disposable = subject.subscribe {
            Toast.makeText(this@RecyclerActivity, it.toString(), Toast.LENGTH_SHORT).show()
        }

        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}

class NumberAdapter(
    private val numbers: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

    inner class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.tvCard)

        fun bind(number: Int, position: Int) {
            textView.text = number.toString()
            itemView.setOnClickListener {
                onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_number, parent, false)
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bind(numbers[position], position)
    }

    override fun getItemCount(): Int = numbers.size
}