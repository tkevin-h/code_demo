package com.thavin.vintrace.data.stock_details

import com.thavin.vintrace.domain.stock_details.StockDetailsRepository

class StockDetailsRepositoryImpl(
    private val stockDetailsService: StockDetailsService
) : StockDetailsRepository {

    override suspend fun getStockDetails(filename: String) =
        stockDetailsService.getStockDetails(filename).toStockDetails()
}