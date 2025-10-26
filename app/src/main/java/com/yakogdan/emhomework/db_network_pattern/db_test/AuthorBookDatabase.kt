package com.yakogdan.emhomework.db_network_pattern.db_test

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AuthorDBO::class,
        BookDBO::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AuthorBookDatabase : RoomDatabase() {

    abstract fun authorBookDAO(): AuthorBookDAO

    companion object {

        private const val DB_NAME = "AuthorBook"

        fun getInstance(context: Context): AuthorBookDatabase =
            Room
                .databaseBuilder(
                    context = context,
                    klass = AuthorBookDatabase::class.java,
                    name = DB_NAME,
                )
                .build()
    }
}