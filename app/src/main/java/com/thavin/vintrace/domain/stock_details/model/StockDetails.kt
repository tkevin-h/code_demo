package com.thavin.vintrace.domain.stock_details.model

data class StockDetails(
    val images: List<String> = emptyList(),
    val code: String = "",
    val description: String = "",
    val secondaryDescription: String? = null,
    val color: String? = null,
    val beverageDescription: String? = null,
    val ownerName: String = "",
    val unitName: String = "",
    val stockLevels: StockLevels = StockLevels(),
    val stockComponents: List<StockComponents> = emptyList()
)
