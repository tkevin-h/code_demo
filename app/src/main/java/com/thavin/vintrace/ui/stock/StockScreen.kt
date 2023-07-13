package com.thavin.vintrace.ui.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.vintrace.R
import com.thavin.vintrace.ui.components.ErrorScreen
import com.thavin.vintrace.ui.components.LoadingOverlay
import com.thavin.vintrace.ui.navigation.Routes
import com.thavin.vintrace.ui.stock.contract.StockEvent
import com.thavin.vintrace.ui.stock.contract.StockIntent
import com.thavin.vintrace.ui.theme.DimenCollapsedTopBarHeight
import com.thavin.vintrace.ui.theme.DimenMedium
import com.thavin.vintrace.ui.theme.DimenNano
import com.thavin.vintrace.ui.theme.DimenSmall
import com.thavin.vintrace.ui.theme.Green60
import com.thavin.vintrace.ui.theme.VintraceTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockScreen(stockOnClick: (String, String) -> Unit) {
    val stockViewModel = koinViewModel<StockViewModel>()
    val state = stockViewModel.state
        .collectAsState()
        .value

    when (state.event) {
        is StockEvent.Navigate -> {
            stockViewModel.processIntent(StockIntent.SetIdleEvent)
            stockOnClick(Routes.STOCK_DETAILS.route, state.event.stockId)
        }

        else -> {}
    }

    Scaffold {
        it.calculateTopPadding()

        with(state) {
            when {
                isLoading -> {
                    LoadingOverlay()
                }

                isError -> {
                    ErrorScreen(
                        message = errorMessage,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                else -> {
                    StockContents(
                        stock = state.stock,
                        stockOnClick = { stockId ->
                            stockViewModel.processIntent(StockIntent.StockOnClick(stockId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StockContents(
    modifier: Modifier = Modifier,
    stock: List<String>,
    stockOnClick: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyColumn {
            item { TopBar() }
            items(items = stock) {
                StockItem(
                    stockId = it,
                    stockOnClick = stockOnClick,
                )
            }
        }
    }
}

@Composable
private fun StockItem(
    modifier: Modifier = Modifier,
    stockId: String,
    stockOnClick: (String) -> Unit
) {

    Card(
        elevation = CardDefaults.cardElevation(DimenNano),
        shape = RoundedCornerShape(DimenNano),
        modifier = modifier
            .padding(DimenSmall)
            .clickable {
                stockOnClick(stockId)
            }
    ) {
        Text(
            text = stockId,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(DimenMedium)
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.test_tag_stock_id))
        )
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Green60)
            .fillMaxWidth()
            .height(DimenCollapsedTopBarHeight)
    )
}

@Preview(showSystemUi = true)
@Composable
fun ErrorScreenPreview() {
    VintraceTheme {
        StockContents(
            stock = listOf(
                "stock-item-1",
                "stock-item-2",
                "stock-item-3",
                "stock-item-4",
                "stock-item-5"
            ),
            stockOnClick = {}
        )
    }
}