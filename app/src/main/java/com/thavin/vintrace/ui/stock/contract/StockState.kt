package com.thavin.vintrace.ui.stock.contract

data class StockState(
    val stock: List<String> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = true,
    val event: StockEvent = StockEvent.Idle
)