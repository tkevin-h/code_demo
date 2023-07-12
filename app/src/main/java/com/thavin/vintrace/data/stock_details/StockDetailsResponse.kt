package com.thavin.vintrace.data.stock_details

import kotlinx.serialization.Serializable

@Serializable
data class StockDetailsResponse(
    val code: String
)
