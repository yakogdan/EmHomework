package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = BouquetDBO.Companion.TABLE_NAME)
data class BouquetDBO(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "bouquet_name")
    val bouquetName: String,
    @ColumnInfo(name = "bouquet_components")
    val bouquetComponents: List<Pair<FlowersDBO, Int>>,
) {
    companion object {
        const val TABLE_NAME = "bouquet_table"
    }
}
