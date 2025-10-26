package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = FlowersDBO.Companion.TABLE_NAME)
data class FlowersDBO(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "remaining_quantity")
    val remainingQuantity: Int = 0,
    @ColumnInfo(name = "country_of_origin")
    val countryOfOrigin: String = "unknown",
) {
    companion object {
        const val TABLE_NAME = "flowers_table"
    }
}