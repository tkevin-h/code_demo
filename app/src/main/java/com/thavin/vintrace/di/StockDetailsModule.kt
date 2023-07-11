package com.thavin.vintrace.di

import com.thavin.vintrace.data.stock_details.StockDetailsRepositoryImpl
import com.thavin.vintrace.data.stock_details.StockDetailsService
import com.thavin.vintrace.data.stock_details.StockDetailsServiceImpl
import com.thavin.vintrace.domain.stock_details.StockDetailsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val StockDetailsModule = module {
    single<StockDetailsService> { StockDetailsServiceImpl(androidContext()) }

    single<StockDetailsRepository> { StockDetailsRepositoryImpl(get()) }
}