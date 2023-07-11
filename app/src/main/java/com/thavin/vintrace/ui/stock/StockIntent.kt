package com.thavin.vintrace.ui.stock

sealed class StockIntent {
    object GetStock : StockIntent()
    data class StockOnClick(val stockName: String) : StockIntent()
}
