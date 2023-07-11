package com.thavin.vintrace.data.stock

interface StockService {

    suspend fun getStock(): List<String>
}