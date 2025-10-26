package com.yakogdan.emhomework.db_network_pattern.db_test

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface AuthorBookDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAuthors(authors: List<AuthorDBO>)

    @Query("SELECT * FROM ${AuthorDBO.TABLE_NAME}")
    suspend fun getAuthors(): List<AuthorDBO>

    @Query("SELECT * FROM ${AuthorDBO.TABLE_NAME} WHERE authorId = :id")
    suspend fun getAuthor(id: Int): AuthorDBO

    @Query("DELETE FROM ${AuthorDBO.TABLE_NAME} WHERE authorId = :id")
    suspend fun deleteAuthor(id: Long): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBooks(books: List<BookDBO>)

    @Query("SELECT * FROM ${BookDBO.TABLE_NAME}")
    suspend fun getBooks(): List<BookDBO>

    @Query("SELECT * FROM ${BookDBO.TABLE_NAME} WHERE bookId = :id")
    suspend fun getBook(id: Int): BookDBO


    @Transaction
    @Query("SELECT * FROM ${AuthorDBO.TABLE_NAME} WHERE authorId = :id")
    suspend fun getAuthorWithBooks(id: Long): AuthorWithBooks?
}