package com.yakogdan.emhomework.db_network_pattern.db_test

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yakogdan.emhomework.databinding.ActivityAuthorBookBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthorBookActivity : AppCompatActivity() {

    val authors = listOf(
        AuthorDBO(authorId = 1L, name = "George Orwell"),
        AuthorDBO(authorId = 2L, name = "Jane Austen"),
        AuthorDBO(authorId = 3L, name = "Fyodor Dostoevsky"),
        AuthorDBO(authorId = 4L, name = "Haruki Murakami")
    )

    val books = listOf(
        BookDBO(bookId = 1L, title = "1984", authorOwnerId = 1L),
        BookDBO(bookId = 2L, title = "Animal Farm", authorOwnerId = 1L),

        BookDBO(bookId = 3L, title = "Pride and Prejudice", authorOwnerId = 2L),
        BookDBO(bookId = 4L, title = "Sense and Sensibility", authorOwnerId = 2L),

        BookDBO(bookId = 5L, title = "Crime and Punishment", authorOwnerId = 3L),
        BookDBO(bookId = 6L, title = "The Brothers Karamazov", authorOwnerId = 3L),

        BookDBO(bookId = 7L, title = "Norwegian Wood", authorOwnerId = 4L),
        BookDBO(bookId = 8L, title = "Kafka on the Shore", authorOwnerId = 4L)
    )

    private var _binding: ActivityAuthorBookBinding? = null
    private val binding get() = _binding!!

    val authorBookDatabase: AuthorBookDatabase by lazy {
        AuthorBookDatabase.getInstance(context = applicationContext)
    }

    val authorBookDAO: AuthorBookDAO by lazy {
        authorBookDatabase.authorBookDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityAuthorBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom,
            )
            insets
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val data = authorBookDAO.getAuthorWithBooks(4)

            val testText = "${data?.author?.name}: ${data?.books?.map { it.title }}"

            withContext(Dispatchers.Main) {
                binding.tvAuthorBook.text = testText
            }
        }

        binding.btnAddAuthor.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                authorBookDAO.addAuthors(authors = authors)
            }
        }

        binding.btnAddBook.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                authorBookDAO.addBooks(books = books)
            }
        }

        binding.btnDeleteAuthor.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                authorBookDAO.deleteAuthor(id = 3L)
            }
        }
    }
}