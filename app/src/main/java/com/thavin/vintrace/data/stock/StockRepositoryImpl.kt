package com.thavin.vintrace.data.stock

import com.thavin.vintrace.domain.stock.StockRepository
import com.thavin.vintrace.util.ResourceResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class StockRepositoryImpl(
    private val stockService: StockService
) : StockRepository {

    private companion object {
        const val ERROR_MESSAGE = "Something has gone wrong."
    }

    override suspend fun getStock(): Flow<ResourceResult<List<String>>> =
        withContext(Dispatchers.IO) {
            flow {
                emit(ResourceResult.Loading())

                try {
                    val stock = stockService.getStock()
                    emit(ResourceResult.Loading(isLoading = false))
                    emit(ResourceResult.Success(stock))
                } catch (e: Exception) {
                    emit(ResourceResult.Loading(isLoading = false))
                    e.message?.let {
                        emit(ResourceResult.Error(message = it))
                    } ?: emit(ResourceResult.Error(message = ERROR_MESSAGE))
                }
            }
        }
}