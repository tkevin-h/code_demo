package com.thavin.vintrace.ui.stock_details.contract

import com.thavin.vintrace.R

data class StockDetailsState(
    val code: String = "",
    val headerImages: List<Int> = listOf(
        R.drawable.img_wine_strawberry,
        R.drawable.img_wine_flowers,
        R.drawable.img_wine_grapes,
        R.drawable.img_generic
    )
)