package com.thavin.vintrace.ui.stock.contract

data class StockState(
    val event: StockEvent = StockEvent.Idle,
    val isLoading: Boolean = true,
    val stock: List<String> = emptyList()
)