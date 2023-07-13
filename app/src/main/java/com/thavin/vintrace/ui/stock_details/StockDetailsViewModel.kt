package com.thavin.vintrace.ui.stock_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thavin.vintrace.domain.stock_details.model.StockDetails
import com.thavin.vintrace.domain.stock_details.StockDetailsRepository
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsEvent
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsIntent
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsState
import com.thavin.vintrace.util.ResourceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockDetailsViewModel(
    private val stockDetailsRepository: StockDetailsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        const val STOCK_ID = "stockId"
    }

    private val _state = MutableStateFlow(StockDetailsState())
    val state = _state.asStateFlow()

    private val stockName: String = checkNotNull(savedStateHandle[STOCK_ID])

    init {
        processIntent(StockDetailsIntent.GetStockDetails(stockName))
    }

    fun processIntent(intent: StockDetailsIntent) {
        when (intent) {
            is StockDetailsIntent.GetStockDetails -> {
                viewModelScope.launch {
                    stockDetailsRepository.getStockDetails(intent.stockName)
                        .collect {
                            processResult(it)
                        }
                }
            }
            is StockDetailsIntent.ComponentOnClick -> {
                setState { copy(event = StockDetailsEvent.Navigate(intent.id)) }
            }
            is StockDetailsIntent.EditOnClick -> {
                setState { copy(event = StockDetailsEvent.ShowToast(intent.message)) }
            }
            is StockDetailsIntent.MoreActionsOnClick -> {
                setState { copy(event = StockDetailsEvent.ShowWebView) }
            }
            is StockDetailsIntent.SetIdleEvent -> {
                setState { copy(event = StockDetailsEvent.Idle) }
            }
            is StockDetailsIntent.BackOnClick -> {
                setState { copy(event = StockDetailsEvent.NavigateBack) }
            }
        }
    }

    private fun processResult(result: ResourceResult<StockDetails>) {
        when (result) {
            is ResourceResult.Loading -> {
                if (result.isLoading) {
                    setState { copy(isLoading = true) }
                } else {
                    setState { copy(isLoading = false) }
                }
            }

            is ResourceResult.Success -> {
                result.data?.let {
                    setState { copy(stockDetails = it) }
                }
            }

            is ResourceResult.Error -> {
                setState { copy(
                    isError = true,
                    errorMessage = result.message
                ) }
            }
        }
    }

    private fun setState(reducer: StockDetailsState.() -> StockDetailsState) =
        viewModelScope.launch {
            _state.emit(reducer(_state.value))
        }
}