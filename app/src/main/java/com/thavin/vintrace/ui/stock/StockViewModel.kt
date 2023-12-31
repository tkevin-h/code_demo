package com.thavin.vintrace.ui.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thavin.vintrace.domain.stock.StockRepository
import com.thavin.vintrace.ui.stock.contract.StockEvent
import com.thavin.vintrace.ui.stock.contract.StockIntent
import com.thavin.vintrace.ui.stock.contract.StockState
import com.thavin.vintrace.util.ResourceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StockViewModel(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StockState())
    val state = _state.asStateFlow()

    init {
        processIntent(StockIntent.GetStock)
    }

    fun processIntent(intent: StockIntent) {
        when (intent) {
            is StockIntent.GetStock -> {
                viewModelScope.launch {
                    stockRepository.getStock()
                        .collect {
                            processResult(it)
                        }
                }
            }
            is StockIntent.StockOnClick -> {
                setState { copy(event = StockEvent.Navigate(intent.stockId)) }
            }
            is StockIntent.SetIdleEvent -> {
                setState { copy(event = StockEvent.Idle) }
            }
        }
    }

    private fun processResult(result: ResourceResult<List<String>>) {
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
                    setState { copy(stock = it) }
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

    private fun setState(reducer: StockState.() -> StockState) =
        viewModelScope.launch {
            _state.emit(reducer(_state.value))
        }
}