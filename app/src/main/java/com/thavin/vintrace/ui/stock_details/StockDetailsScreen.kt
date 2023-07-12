package com.thavin.vintrace.ui.stock_details

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.thavin.vintrace.R
import com.thavin.vintrace.domain.stock_details.model.StockComponents
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsEvent
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsIntent
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
fun StockDetailsScreen(
    componentOnClick: (String) -> Unit,
    backOnClick: () -> Unit
) {
    val stockDetailsViewModel = koinViewModel<StockDetailsViewModel>()
    val state = stockDetailsViewModel.state
        .collectAsState()
        .value

    when (state.event) {
        is StockDetailsEvent.Navigate -> {
            stockDetailsViewModel.processIntent(StockDetailsIntent.SetIdleEvent)
            componentOnClick(state.event.id)
        }

        is StockDetailsEvent.ShowToast -> {
            stockDetailsViewModel.processIntent(StockDetailsIntent.SetIdleEvent)
            Toast.makeText(LocalContext.current, state.event.message, Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

    Scaffold {
        it.calculateTopPadding()

        with(state.stockDetails) {
            StockDetailsContent(
                images = images,
                code = code,
                description = description,
                secondaryDescription = secondaryDescription,
                color = color,
                beverageDescription = beverageDescription,
                ownerName = ownerName,
                unitName = unitName,
                onHand = stockLevels.onHand,
                committed = stockLevels.committed,
                inProduction = stockLevels.inProduction,
                available = stockLevels.available,
                components = stockComponents,
                componentOnClick = { id ->
                    stockDetailsViewModel.processIntent(StockDetailsIntent.ComponentOnClick(id))
                },
                backOnClick = backOnClick,
                editOnClick = { message ->
                    stockDetailsViewModel.processIntent(StockDetailsIntent.EditOnClick(message))
                },
                moreActionsOnClick = {}
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
    unitName: String,
    onHand: Int,
    committed: Int,
    inProduction: Int,
    available: Int,
    components: List<StockComponents>,
    componentOnClick: (String) -> Unit,
    backOnClick: () -> Unit,
    editOnClick: (String) -> Unit,
    moreActionsOnClick: () -> Unit
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

    val imageResources = if (images.isEmpty()) {
        listOf(R.drawable.img_generic)
    } else {
        images.map {
            when (it) {
                ImageTypes.WINE1.endpoint -> {
                    R.drawable.img_wine_flowers
                }

                ImageTypes.WINE2.endpoint -> {
                    R.drawable.img_wine_grapes
                }

                ImageTypes.WINE3.endpoint -> {
                    R.drawable.img_wine_strawberry
                }

                ImageTypes.WINE4.endpoint -> {
                    R.drawable.img_wine_table
                }

                else -> {
                    R.drawable.img_generic
                }
            }
        }
    }

    val editToastMessage = stringResource(id = R.string.edit_button_toast)

    Box {
        CollapsedTopBar(
            modifier = Modifier.zIndex(2f),
            isCollapsed = isCollapsed,
            backOnClick = backOnClick,
            editOnClick = editOnClick,
            moreActionsOnClick = moreActionsOnClick
        )

        LazyColumn(state = lazyListState) {
            item {
                ExpandedTopBar(
                    headerImages = imageResources,
                    backOnClick = backOnClick,
                    editOnClick = editOnClick,
                    moreActionsOnClick = moreActionsOnClick
                )
            }

            item {
                AnimatedVisibility(visible = !isCollapsed) {
                    StockInformation(
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

            item {
                Spacer(modifier = Modifier.height(DimenLarge))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.levels_title),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { editOnClick(editToastMessage) }
                    ) {
                        Text(text = stringResource(id = R.string.edit_button))
                    }
                }
            }

            item {
                LevelsInformation(
                    onHand = onHand,
                    committed = committed,
                    inProduction = inProduction,
                    available = available
                )

                Spacer(modifier = Modifier.height(DimenSmall))
            }

            if (components.isNotEmpty()) {
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.components_title),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(DimenSmall))
                        Text(
                            text = "(${components.size})",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

                item {
                    ComponentsInformation(
                        components = components,
                        componentOnClick = componentOnClick
                    )
                    Spacer(modifier = Modifier.height(DimenXxLarge))
                }
            }

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
private fun ExpandedTopBar(
    headerImages: List<Int>,
    backOnClick: () -> Unit,
    editOnClick: (String) -> Unit,
    moreActionsOnClick: () -> Unit
) {

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
            backOnClick = backOnClick,
            editOnClick = editOnClick,
            moreActionsOnClick = moreActionsOnClick,
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
    isCollapsed: Boolean,
    backOnClick: () -> Unit,
    editOnClick: (String) -> Unit,
    moreActionsOnClick: () -> Unit
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
                backOnClick = backOnClick,
                editOnClick = editOnClick,
                moreActionsOnClick = moreActionsOnClick,
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
private fun StockInformation(
    modifier: Modifier = Modifier,
    code: String,
    description: String,
    secondaryDescription: String,
    color: String,
    beverageDescription: String,
    ownerName: String,
    unitName: String,
    alpha: Float = 1f
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
            .alpha(alpha)
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
private fun LevelsInformation(
    modifier: Modifier = Modifier,
    onHand: Int,
    committed: Int,
    inProduction: Int,
    available: Int,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(DimenSmall),
        elevation = CardDefaults.cardElevation(DimenNano),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(DimenSmall)
        ) {
            Row {
                Text(text = stringResource(id = R.string.levels_on_hand_title))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = onHand.toString())
            }

            Row {
                Text(text = stringResource(id = R.string.levels_committed_title))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = committed.toString())
            }

            Row {
                Text(text = stringResource(id = R.string.levels_in_production_title))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = inProduction.toString())
            }

            Row {
                Text(text = stringResource(id = R.string.levels_available_title))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = available.toString())
            }
        }
    }
}

@Composable
private fun ComponentsInformation(
    modifier: Modifier = Modifier,
    components: List<StockComponents>,
    componentOnClick: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(DimenSmall),
        elevation = CardDefaults.cardElevation(DimenNano),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(DimenSmall)
        ) {
            for (item in components) {
                Row(
                    modifier = Modifier.clickable {
                        componentOnClick(item.id)
                    }
                ) {
                    Column {
                        Text(text = item.code)
                        Text(text = item.description)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = item.quantity)
                }
            }
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
    backOnClick: () -> Unit,
    editOnClick: (String) -> Unit,
    moreActionsOnClick: () -> Unit
) {
    val toastMessage = stringResource(id = R.string.edit_button_toast)

    Row(
        modifier = modifier
    ) {
        Button(
            onClick = { backOnClick() },
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
            onClick = { editOnClick(toastMessage) },
            contentPadding = PaddingValues(
                start = DimenSmall,
                end = DimenSmall,
                top = DimenMicro,
                bottom = DimenMicro
            ),
            colors = ButtonDefaults.buttonColors(containerColor = containerColor.copy(alpha = alpha)),
            modifier = Modifier
                .height(DimenLarge)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(id = R.string.accessibility_edit_button),
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
            onClick = { moreActionsOnClick() },
            colors = ButtonDefaults.buttonColors(containerColor = containerColor.copy(alpha = alpha)),
            contentPadding = PaddingValues(DimenZero),
            modifier = Modifier
                .size(DimenLarge)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more_actions),
                contentDescription = stringResource(id = R.string.accessibility_more_actions_button),
                tint = contentColor
            )
        }
    }
}