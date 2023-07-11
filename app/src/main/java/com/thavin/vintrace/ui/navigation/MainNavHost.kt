package com.thavin.vintrace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thavin.vintrace.ui.stock.StockIntent
import com.thavin.vintrace.ui.stock.StockScreen
import com.thavin.vintrace.ui.stock.StockViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainNavHost(
    navHostController: NavHostController
) {

    NavHost(navController = navHostController, startDestination = Routes.STOCK.route) {

        composable(route = Routes.STOCK.route) {
            val stockViewModel = koinViewModel<StockViewModel>()
            val state by stockViewModel.state
                .collectAsState()

            StockScreen(
                state = state,
                stockOnClick = { stockViewModel.processIntent(StockIntent.StockOnClick(it)) }
            )
        }
    }
}