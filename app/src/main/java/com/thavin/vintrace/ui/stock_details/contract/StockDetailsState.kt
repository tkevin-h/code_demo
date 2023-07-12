package com.thavin.vintrace.ui.stock_details.contract

import com.thavin.vintrace.domain.stock_details.model.StockDetails

data class StockDetailsState(
    val stockDetails: StockDetails = StockDetails(),
    val showWebView: Boolean = false,
    val event: StockDetailsEvent = StockDetailsEvent.Idle
)