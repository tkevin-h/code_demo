package com.thavin.vintrace.di

import com.thavin.vintrace.data.stock_details.StockDetailsRepositoryImpl
import com.thavin.vintrace.data.stock_details.StockDetailsService
import com.thavin.vintrace.data.stock_details.StockDetailsServiceImpl
import com.thavin.vintrace.domain.stock_details.StockDetailsRepository
import com.thavin.vintrace.ui.stock_details.StockDetailsViewModel
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val StockDetailsModule = module {
    single<StockDetailsService> { StockDetailsServiceImpl(androidContext(), get()) }

    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single<StockDetailsRepository> { StockDetailsRepositoryImpl(get()) }

    viewModel { StockDetailsViewModel(get(), get()) }
}