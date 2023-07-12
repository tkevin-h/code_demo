package com.thavin.vintrace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thavin.vintrace.ui.stock.StockScreen
import com.thavin.vintrace.ui.stock_details.StockDetailsScreen

@Composable
fun MainNavHost(
    navHostController: NavHostController
) {

    NavHost(navController = navHostController, startDestination = Routes.STOCK.route) {

        composable(route = Routes.STOCK.route) {
            StockScreen(
                stockOnClick = { route, stockId ->
                    navHostController.navigate("$route/$stockId")
                }
            )
        }

        composable(
            route = Routes.STOCK_DETAILS.route + "/{stockId}",
            arguments = listOf(navArgument("stockId") { type = NavType.StringType })
        ) {
            StockDetailsScreen()
        }
    }
}