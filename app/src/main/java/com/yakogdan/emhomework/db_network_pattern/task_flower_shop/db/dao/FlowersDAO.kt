package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.FlowersDBO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.FlowersDBO.Companion.TABLE_NAME

@Dao
interface FlowersDAO {

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getFlowers(): List<FlowersDBO>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id")
    suspend fun getFlower(id: Int): FlowersDBO

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFlowers(flowers: List<FlowersDBO>)

    @Query(
        "UPDATE $TABLE_NAME " +
                "SET remaining_quantity = remaining_quantity - :flowersQuantity " +
                "WHERE id = :flowerId AND remaining_quantity >= :flowersQuantity"
    )
    suspend fun removeFlowers(flowerId: Int, flowersQuantity: Int): Int

    @Transaction
    suspend fun buyBouquet(bouquetComponents: List<Pair<FlowersDBO, Int>>): Boolean {

        for ((flower, flowersQuantity) in bouquetComponents) {

            val numberChanges = removeFlowers(
                flowerId = flower.id,
                flowersQuantity = flowersQuantity,
            )

            if (numberChanges == 0) {
                return false
            }
        }

        return true
    }
}