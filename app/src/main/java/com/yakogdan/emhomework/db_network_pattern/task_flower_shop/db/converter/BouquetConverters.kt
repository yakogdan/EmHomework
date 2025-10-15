package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.converter

import androidx.room.TypeConverter
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.FlowersDBO
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object BouquetConverters {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    private val pairSerializer = PairSerializer(
        serializer<FlowersDBO>(),
        Int.serializer(),
    )
    private val listOfPairsSerializer = ListSerializer(pairSerializer)

    @TypeConverter
    @JvmStatic
    fun bouquetComponentsToString(value: List<Pair<FlowersDBO, Int>>?): String? {
        if (value == null) return null
        return json.encodeToString(
            serializer = listOfPairsSerializer,
            value = value,
        )
    }

    @TypeConverter
    @JvmStatic
    fun stringToBouquetComponents(value: String?): List<Pair<FlowersDBO, Int>>? {
        if (value.isNullOrBlank()) return null
        return json.decodeFromString(
            deserializer = listOfPairsSerializer,
            string = value,
        )
    }
}