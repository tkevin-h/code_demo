package com.thavin.vintrace.domain.stock_details

interface StockDetailsRepository {

    suspend fun getStockDetails(filename: String): StockDetails
}