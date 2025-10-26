package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2,
    exportSchema = false,
)
@TypeConverters(BouquetConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun flowersDao(): FlowersDAO
    abstract fun bouquetDao(): BouquetsDAO


    companion object {

        private const val DB_NAME = "flowers.db"

        fun getInstance(context: Context): AppDatabase =
            Room
                .databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DB_NAME,
                )
                .addMigrations(MIGRATION_1_2)
                .build()

        private val MIGRATION_1_2 = object : Migration(startVersion = 1, endVersion = 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    ALTER TABLE ${FlowersDBO.TABLE_NAME}
                    ADD COLUMN country_of_origin TEXT NOT NULL DEFAULT 'unknown'
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    ALTER TABLE ${BouquetDBO.TABLE_NAME}
                    ADD COLUMN wrapping TEXT NOT NULL DEFAULT 'none'
                    """.trimIndent()
                )
            }
        }
    }
}