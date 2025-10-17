package com.yakogdan.emhomework.db_network_pattern.task_flower_shop.usecase

import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.FlowersDAO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.BouquetDBO

class BuyBouquetUseCase(
    private val flowersDAO: FlowersDAO,
) {

    suspend fun invoke(bouquet: BouquetDBO): Boolean =
        flowersDAO.buyBouquet(bouquetComponents = bouquet.bouquetComponents)
}