package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.converter.BouquetConverters
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.BouquetsDAO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.FlowersDAO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.BouquetDBO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.FlowersDBO

@Database(
    entities = [
        FlowersDBO::class,
        BouquetDBO::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(BouquetConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun flowersDao(): FlowersDAO
    abstract fun bouquetDao(): BouquetsDAO


    companion object {

        private const val DB_NAME = "flowers.db"

        fun getInstance(context: Context): AppDatabase =
            Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = DB_NAME
            ).build()
    }
}