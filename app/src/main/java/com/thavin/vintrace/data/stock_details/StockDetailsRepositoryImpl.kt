package com.thavin.vintrace.data.stock_details

import com.thavin.vintrace.domain.stock_details.model.StockDetails
import com.thavin.vintrace.domain.stock_details.StockDetailsRepository
import com.thavin.vintrace.util.ResourceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class StockDetailsRepositoryImpl(
    private val stockDetailsService: StockDetailsService
) : StockDetailsRepository {

    private companion object {
        const val ERROR_MESSAGE = "Something has gone wrong."
    }

    override suspend fun getStockDetails(filename: String): Flow<ResourceResult<StockDetails>> =
        withContext(Dispatchers.IO) {
            flow {
                emit(ResourceResult.Loading())

                try {
                    val stockDetails = stockDetailsService.getStockDetails(filename).toStockDetails()
                    emit(ResourceResult.Loading(isLoading = false))
                    emit(ResourceResult.Success(stockDetails))
                } catch (e: Exception) {
                    emit(ResourceResult.Loading(isLoading = false))
                    e.message?.let {
                        emit(ResourceResult.Error(message = it))
                    } ?: emit(ResourceResult.Error(message = ERROR_MESSAGE))
                }
            }
        }
}