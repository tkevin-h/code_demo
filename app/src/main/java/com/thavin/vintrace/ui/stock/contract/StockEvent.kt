package com.thavin.vintrace.ui.stock.contract

sealed class StockEvent {
    object Idle: StockEvent()
    data class Navigate(val stockId: String) : StockEvent()
}
