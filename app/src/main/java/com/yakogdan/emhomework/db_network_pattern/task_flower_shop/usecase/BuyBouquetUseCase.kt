package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.usecase

import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.FlowersDAO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.BouquetDBO

class BuyBouquetUseCase(
    private val flowersDAO: FlowersDAO,
) {

    suspend fun invoke(bouquet: BouquetDBO) {
        bouquet.bouquetComponents.forEach { pair ->
            flowersDAO.removeFlowers(
                flowerId = pair.first.id,
                flowersQuantity = pair.second,
            )
        }
    }
}