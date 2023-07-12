package com.thavin.vintrace.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thavin.vintrace.ui.navigation.MainNavHost
import com.thavin.vintrace.ui.theme.VintraceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VintraceTheme {
                val systemUiController = rememberSystemUiController()

                WindowCompat.setDecorFitsSystemWindows(window, false)

                SideEffect {
                    systemUiController.setStatusBarColor(Color.Transparent)
                }

                MainNavHost(navHostController = rememberNavController())
            }
        }
    }
}