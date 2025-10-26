package com.yakogdan.emhomework.db_network_pattern.db_test

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AuthorDBO.TABLE_NAME)
data class AuthorDBO(

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "authorId")
    val authorId: Long,

    @ColumnInfo(name = "name")
    val name: String,
) {

    companion object {

        const val TABLE_NAME = "authors"
    }
}