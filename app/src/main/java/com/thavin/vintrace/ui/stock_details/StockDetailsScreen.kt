package com.thavin.vintrace.ui.stock_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.thavin.vintrace.R
import com.thavin.vintrace.ui.theme.DimenCollapsedTopBarHeight
import com.thavin.vintrace.ui.theme.DimenExpandedTopBarHeight
import com.thavin.vintrace.ui.theme.DimenExtraLarge
import com.thavin.vintrace.ui.theme.DimenLarge
import com.thavin.vintrace.ui.theme.DimenMedium
import com.thavin.vintrace.ui.theme.DimenMicro
import com.thavin.vintrace.ui.theme.DimenNano
import com.thavin.vintrace.ui.theme.DimenSmall
import com.thavin.vintrace.ui.theme.DimenTopBarPadding
import com.thavin.vintrace.ui.theme.DimenXxLarge
import com.thavin.vintrace.ui.theme.DimenZero
import com.thavin.vintrace.ui.theme.Green60
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockDetailsScreen() {
    val stockDetailsViewModel = koinViewModel<StockDetailsViewModel>()
    val state by stockDetailsViewModel.state
        .collectAsState()

    Scaffold(

    ) {
        it.calculateTopPadding()

        with (state.stockDetails) {
            StockDetailsContent(
                images = images,
                code = code,
                description = description,
                secondaryDescription = secondaryDescription,
                color = color,
                beverageDescription = beverageDescription,
                ownerName = ownerName,
                unitName = unitName
            )
        }
    }
}

@Composable
private fun StockDetailsContent(
    images: List<String>,
    code: String,
    description: String,
    secondaryDescription: String,
    color: String,
    beverageDescription: String,
    ownerName: String,
    unitName: String
) {
    val lazyListState = rememberLazyListState()
    val overlapHeight = with(LocalDensity.current) {
        DimenExpandedTopBarHeight.toPx() - DimenCollapsedTopBarHeight.toPx()
    }
    val isCollapsed: Boolean by remember {
        derivedStateOf {
            val isFirstItemVisible = lazyListState.firstVisibleItemScrollOffset > overlapHeight
            isFirstItemVisible || lazyListState.firstVisibleItemIndex > 0
        }
    }

    val imageResources = images.map {
        when (it) {
            ImageTypes.WINE1.endpoint -> { R.drawable.img_wine_flowers }
            ImageTypes.WINE2.endpoint -> { R.drawable.img_wine_grapes }
            ImageTypes.WINE3.endpoint -> { R.drawable.img_wine_strawberry }
            ImageTypes.WINE4.endpoint -> { R.drawable.img_wine_table }
            else -> { R.drawable.img_generic }
        }
    }

    Box {
        CollapsedTopBar(modifier = Modifier.zIndex(2f), isCollapsed = isCollapsed)

        LazyColumn(state = lazyListState) {
            item { ExpandedTopBar(headerImages = imageResources) }

            item {
                StockHeader(
                    code = code,
                    description = description,
                    secondaryDescription = secondaryDescription,
                    color = color,
                    beverageDescription = beverageDescription,
                    ownerName = ownerName,
                    unitName = unitName
                )
                Spacer(modifier = Modifier.height(DimenSmall))
            }
//
//            item { TestItem() }
//            item { TestItem() }
//            item { TestItem() }
//            item { TestItem() }
//            item { TestItem() }
//            item { TestItem() }
//            item { TestItem() }
//            item { TestItem() }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExpandedTopBar(headerImages: List<Int>) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        headerImages.size
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(DimenExpandedTopBarHeight)
    ) {
        HorizontalPager(state = pagerState) {
            Image(
                painter = painterResource(id = headerImages[it]),
                contentDescription = stringResource(id = R.string.accessibility_header_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        NavBar(
            modifier = Modifier.padding(
                top = DimenExtraLarge,
                start = DimenMicro,
                end = DimenMicro
            )
        )
    }
}

@Composable
private fun CollapsedTopBar(
    modifier: Modifier = Modifier,
    isCollapsed: Boolean
) {
    val backgroundColor: Color by animateColorAsState(
        if (isCollapsed) {
            Green60
        } else {
            Color.Transparent
        }
    )

    Box(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(DimenCollapsedTopBarHeight)
    ) {
        AnimatedVisibility(visible = isCollapsed) {
            NavBar(
                contentColor = Color.White,
                alpha = 0.25f,
                modifier = Modifier.padding(
                    top = DimenTopBarPadding,
                    start = DimenMicro,
                    end = DimenMicro
                )
            )
        }
    }
}

@Composable
private fun StockHeader(
    modifier: Modifier = Modifier,
    code: String,
    description: String,
    secondaryDescription: String,
    color: String,
    beverageDescription: String,
    ownerName: String,
    unitName: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(
            topStart = DimenZero,
            topEnd = DimenZero,
            bottomStart = DimenSmall,
            bottomEnd = DimenSmall
        ),
        elevation = CardDefaults.cardElevation(DimenNano),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                top = DimenSmall,
                start = DimenMedium,
                end = DimenMedium,
                bottom = DimenMedium
            )
        ) {

            Text(text = code)
            Text(text = description)
            Text(text = secondaryDescription)
            Text(text = color)
            Text(text = beverageDescription)
            Text(text = ownerName)
            Text(text = unitName)
        }
    }
}

@Composable
private fun TestItem(
) {
    Card(
        elevation = CardDefaults.cardElevation(DimenNano),
        shape = RoundedCornerShape(DimenNano),
    ) {
        Text(
            text = "testItem",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(DimenXxLarge)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun NavBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    contentColor: Color = Green60,
    alpha: Float = 1f,
    title: String = stringResource(id = R.string.empty_String),
) {

    Row(
        modifier = modifier
    ) {
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = containerColor.copy(alpha = alpha)),
            contentPadding = PaddingValues(DimenZero),
            modifier = Modifier
                .size(DimenLarge)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_back),
                contentDescription = stringResource(id = R.string.accessibility_back_button),
                tint = contentColor
            )
        }

        Text(text = title)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(DimenMicro),
            colors = ButtonDefaults.buttonColors(containerColor = containerColor.copy(alpha = alpha)),
            modifier = Modifier
                .height(DimenLarge)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.accessibility_back_button),
                tint = contentColor
            )

            Spacer(modifier = Modifier.width(DimenMicro))

            Text(
                text = stringResource(id = R.string.edit_button),
                color = contentColor
            )
        }

        Spacer(modifier = Modifier.width(DimenMicro))

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = containerColor.copy(alpha = alpha)),
            contentPadding = PaddingValues(DimenZero),
            modifier = Modifier
                .size(DimenLarge)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_action_more),
                contentDescription = stringResource(id = R.string.accessibility_back_button),
                tint = contentColor
            )
        }
    }
}