package com.thavin.vintrace.ui.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.thavin.vintrace.ui.theme.DimenMedium
import com.thavin.vintrace.ui.theme.DimenNano
import com.thavin.vintrace.ui.theme.DimenSmall

@Composable
fun StockScreen(
    state: StockState,
    stockOnClick: (String) -> Unit
) {

    Scaffold {
        it.calculateTopPadding()

        StockContents(
            stock = state.stock,
            stockOnClick = stockOnClick
        )
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
        LazyColumn(
            modifier = Modifier
                .padding(DimenSmall)
        ) {
            items(items = stock) {
                StockItem(
                    stockName = it,
                    stockOnClick = stockOnClick,
                )

                Spacer(modifier = Modifier.height(DimenSmall))
            }
        }
    }
}

@Composable
private fun StockItem(
    modifier: Modifier = Modifier,
    stockName: String,
    stockOnClick: (String) -> Unit
) {

    Card(
        elevation = CardDefaults.cardElevation(DimenNano),
        shape = RoundedCornerShape(DimenNano),
        modifier = modifier.clickable {
            stockOnClick(stockName)
        }
    ) {
        Text(
            text = stockName,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(DimenMedium)
                .fillMaxWidth()
        )
    }
}