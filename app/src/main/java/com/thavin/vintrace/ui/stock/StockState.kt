package com.thavin.vintrace.ui.stock

data class StockState(
    val isLoading: Boolean = true,
    val stock: List<String> = emptyList()
)