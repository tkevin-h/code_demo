package com.thavin.vintrace.domain.stock_details

import com.thavin.vintrace.domain.stock_details.model.StockDetails
import com.thavin.vintrace.util.ResourceResult
import kotlinx.coroutines.flow.Flow

interface StockDetailsRepository {

    suspend fun getStockDetails(filename: String): Flow<ResourceResult<StockDetails>>
}