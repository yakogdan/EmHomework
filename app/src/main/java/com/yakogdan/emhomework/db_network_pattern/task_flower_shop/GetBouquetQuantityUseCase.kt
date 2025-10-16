package com.yakogdan.emhomework.db_network_pattern.task_flower_shop

import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.BouquetsDAO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.FlowersDAO

class GetBouquetQuantityUseCase(
    private val bouquetsDAO: BouquetsDAO,
    private val flowersDAO: FlowersDAO,
) {

    suspend fun invoke(bouquetId: Int): Int {
        val bouquet = bouquetsDAO.getBouquet(id = bouquetId)
        val enoughFor = mutableListOf<Int>()

        bouquet.bouquetComponents.forEach {
            val remainingQuantity = flowersDAO.getFlower(id = it.first.id).remainingQuantity
            enoughFor.add(remainingQuantity / it.second)
        }

        return enoughFor.min()
    }
}