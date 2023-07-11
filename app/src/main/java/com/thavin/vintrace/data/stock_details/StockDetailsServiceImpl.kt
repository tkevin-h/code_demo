package com.thavin.vintrace.data.stock_details

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class StockDetailsServiceImpl(
    private val context: Context
) : StockDetailsService {

    override suspend fun getStockDetails(fileName: String): StockDetailsResponse {
        val json = context.assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }

        return Json.decodeFromString(json)
    }
}