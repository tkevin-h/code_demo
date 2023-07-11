package com.thavin.vintrace.domain.stock

import com.thavin.vintrace.util.ResourceResult
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getStock(): Flow<ResourceResult<List<String>>>
}