package com.thavin.vintrace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thavin.vintrace.R
import com.thavin.vintrace.ui.stock.StockScreen
import com.thavin.vintrace.ui.stock_details.StockDetailsScreen
import com.thavin.vintrace.ui.components.WebView

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.STOCK.route
    ) {

        composable(route = Routes.STOCK.route) {
            StockScreen(
                stockOnClick = { route, stockId ->
                    navController.navigate("$route/$stockId")
                }
            )
        }

        composable(
            route = Routes.STOCK_DETAILS.route + "/{stockId}",
            arguments = listOf(navArgument("stockId") { type = NavType.StringType })
        ) {
            StockDetailsScreen(
                componentOnClick = {
                    navController.navigate(Routes.STOCK_DETAILS.route + "/$it")
                },
                backOnClick = { navController.popBackStack() },
                moreActionsOnClick = { navController.navigate(Routes.MORE_ACTIONS_WEB_VIEW.route) }
            )
        }

        composable(route = Routes.MORE_ACTIONS_WEB_VIEW.route) {
            val url = stringResource(id = R.string.vintrace_home_url)
            WebView(url = url)
        }
    }
}