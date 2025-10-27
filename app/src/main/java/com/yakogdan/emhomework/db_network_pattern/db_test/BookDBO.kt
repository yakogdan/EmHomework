package com.yakogdan.emhomework.db_network_pattern.db_test

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = BookDBO.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = AuthorDBO::class,
            parentColumns = ["authorId"],
            childColumns = ["authorOwnerId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index(value = ["authorOwnerId"])
    ]
)
data class BookDBO(
    @PrimaryKey
    val bookId: Long,
    val title: String,
    val authorOwnerId: Long
) {

    companion object {

        const val TABLE_NAME = "books"
    }
}