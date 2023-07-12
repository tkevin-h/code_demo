package com.thavin.vintrace.domain.stock_details.model

data class StockLevels(
    val onHand: Int = 0,
    val committed: Int = 0,
    val inProduction: Int = 0,
    val available: Int = 0,
)
