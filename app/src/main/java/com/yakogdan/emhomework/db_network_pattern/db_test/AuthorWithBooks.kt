package com.yakogdan.emhomework.db_network_pattern.db_test

import androidx.room.Embedded
import androidx.room.Relation

data class AuthorWithBooks(
    @Embedded val author: AuthorDBO,
    @Relation(
        parentColumn = "authorId",
        entityColumn = "authorOwnerId"
    )
    val books: List<BookDBO>
)