package com.thavin.vintrace.ui.stock.contract

sealed class StockIntent {
    object GetStock : StockIntent()
    object SetIdleEvent : StockIntent()
    data class StockOnClick(val stockId: String) : StockIntent()
}
