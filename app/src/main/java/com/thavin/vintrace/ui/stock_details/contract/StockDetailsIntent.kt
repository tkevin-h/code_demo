package com.thavin.vintrace.ui.stock_details.contract

sealed class StockDetailsIntent {

    data class GetStockDetails(val stockName: String) : StockDetailsIntent()
}