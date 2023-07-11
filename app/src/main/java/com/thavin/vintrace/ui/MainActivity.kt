package com.thavin.vintrace.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.thavin.vintrace.ui.navigation.MainNavHost
import com.thavin.vintrace.ui.theme.VintraceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VintraceTheme {
                MainNavHost(navHostController = rememberNavController())
            }
        }
    }
}