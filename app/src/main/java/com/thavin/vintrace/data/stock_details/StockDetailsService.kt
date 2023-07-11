package com.thavin.vintrace.data.stock_details

interface StockDetailsService {

    suspend fun getStockDetails(fileName: String): StockDetailsResponse
}