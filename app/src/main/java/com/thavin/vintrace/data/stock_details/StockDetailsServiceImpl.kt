package com.thavin.vintrace.data.stock_details

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class StockDetailsServiceImpl(
    private val context: Context,
    private val json: Json
) : StockDetailsService {

    private companion object {
        const val STOCK_ASSETS_PATH = "stock/"
    }

    override suspend fun getStockDetails(fileName: String): StockDetailsResponse {
        val jsonFile = context.assets.open(STOCK_ASSETS_PATH + fileName)
            .bufferedReader()
            .use { it.readText() }

        return json.decodeFromString(jsonFile)
    }
}