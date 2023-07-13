package com.thavin.vintrace.ui.stock_details.contract

import com.thavin.vintrace.domain.stock_details.model.StockDetails

data class StockDetailsState(
    val stockDetails: StockDetails = StockDetails(),
    val isError: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val event: StockDetailsEvent = StockDetailsEvent.Idle
)