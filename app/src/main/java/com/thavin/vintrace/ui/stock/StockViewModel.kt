package com.thavin.vintrace.ui.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thavin.vintrace.domain.stock.StockRepository
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

            }
        }
    }

    private fun processResult(result: ResourceResult<List<String>>) {
        when (result) {
            is ResourceResult.Loading -> {

            }
            is ResourceResult.Success -> {
                result.data?.let {
                    setState { copy(stock = it) }
                }
            }
            is ResourceResult.Error -> {

            }
        }
    }

    private fun setState(reducer: StockState.() -> StockState) =
        viewModelScope.launch {
            _state.emit(reducer(_state.value))
        }
}