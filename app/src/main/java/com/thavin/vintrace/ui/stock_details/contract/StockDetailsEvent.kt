package com.thavin.vintrace.ui.stock_details.contract

sealed class StockDetailsEvent {
    object Idle: StockDetailsEvent()
    data class Navigate(val id: String) : StockDetailsEvent()
    object ShowWebView : StockDetailsEvent()
    data class ShowToast(val message: String) : StockDetailsEvent()
    object NavigateBack : StockDetailsEvent()
}
