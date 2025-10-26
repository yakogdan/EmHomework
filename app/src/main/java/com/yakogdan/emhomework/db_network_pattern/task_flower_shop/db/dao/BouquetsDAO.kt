package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.BouquetDBO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.BouquetDBO.Companion.TABLE_NAME

@Dao
interface BouquetsDAO {

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getBouquets(): List<BouquetDBO>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getBouquet(id: Int): BouquetDBO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBouquets(bouquets: List<BouquetDBO>)
}