package com.thavin.vintrace.data.stock

import android.content.Context

class StockServiceImpl(
    private val context: Context
) : StockService {

    private companion object {
        const val STOCK_ASSETS_PATH = "stock/"
    }

    override suspend fun getStock(): List<String> {
        val files = context.assets.list(STOCK_ASSETS_PATH) ?: return emptyList()

        return files.toList()
    }
}