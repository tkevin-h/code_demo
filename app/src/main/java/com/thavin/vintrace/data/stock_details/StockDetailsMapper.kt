package com.thavin.vintrace.data.stock_details

import com.thavin.vintrace.domain.stock_details.StockDetails

fun StockDetailsResponse.toStockDetails() =
    StockDetails(
        code = this.code
    )