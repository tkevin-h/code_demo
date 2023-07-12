package com.thavin.vintrace.ui.stock_details.contract

sealed class StockDetailsIntent {

    data class GetStockDetails(val stockName: String) : StockDetailsIntent()
    data class ComponentOnClick(val id: String) : StockDetailsIntent()
    data class EditOnClick(val message: String) : StockDetailsIntent()
    object SetIdleEvent : StockDetailsIntent()
}