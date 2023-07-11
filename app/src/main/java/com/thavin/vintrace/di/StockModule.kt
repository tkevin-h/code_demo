package com.thavin.vintrace.di

import com.thavin.vintrace.data.stock.StockRepositoryImpl
import com.thavin.vintrace.data.stock.StockService
import com.thavin.vintrace.data.stock.StockServiceImpl
import com.thavin.vintrace.domain.stock.StockRepository
import com.thavin.vintrace.ui.stock.StockViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val StockModule = module {
    single<StockService> { StockServiceImpl(androidContext()) }

    single<StockRepository> { StockRepositoryImpl(get()) }

    viewModel { StockViewModel(get()) }
}