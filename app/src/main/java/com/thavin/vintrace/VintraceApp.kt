package com.thavin.vintrace

import android.app.Application
import com.thavin.vintrace.di.StockDetailsModule
import com.thavin.vintrace.di.StockModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VintraceApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@VintraceApp)
            modules(
                StockModule,
                StockDetailsModule
            )
        }
    }
}