package com.thavin.vintrace.ui.stock_details

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.core.graphics.toColorInt
import com.thavin.vintrace.R
import com.thavin.vintrace.domain.stock_details.model.StockComponents
import com.thavin.vintrace.ui.components.CollapsedTopBar
import com.thavin.vintrace.ui.components.ErrorScreen
import com.thavin.vintrace.ui.components.ExpandedTopBar
import com.thavin.vintrace.ui.components.LoadingOverlay
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsEvent
import com.thavin.vintrace.ui.stock_details.contract.StockDetailsIntent
import com.thavin.vintrace.ui.stock_details.mapper.numberFormat
import com.thavin.vintrace.ui.stock_details.mapper.toResources
import com.thavin.vintrace.ui.theme.Cyan59
import com.thavin.vintrace.ui.theme.DimenCollapsedTopBarHeight
import com.thavin.vintrace.ui.theme.DimenExpandedTopBarHeight
import com.thavin.vintrace.ui.theme.DimenLarge
import com.thavin.vintrace.ui.theme.DimenListBottomSpacer
import com.thavin.vintrace.ui.theme.DimenMedium
import com.thavin.vintrace.ui.theme.DimenMicro
import com.thavin.vintrace.ui.theme.DimenNano
import com.thavin.vintrace.ui.theme.DimenSmall
import com.thavin.vintrace.ui.theme.DimenZero
import com.thavin.vintrace.ui.theme.Green60
import com.thavin.vintrace.ui.theme.Grey21
import com.thavin.vintrace.ui.theme.Grey31
import com.thavin.vintrace.ui.theme.Grey56
import com.thavin.vintrace.ui.theme.Grey63
import com.thavin.vintrace.ui.theme.Grey76
import com.thavin.vintrace.ui.theme.Grey88
import com.thavin.vintrace.ui.theme.Typography
import com.thavin.vintrace.ui.theme.VintraceTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockDetailsScreen(
    componentOnClick: (String) -> Unit,
    backOnClick: () -> Unit,
    moreActionsOnClick: () -> Unit
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

        is StockDetailsEvent.ShowWebView -> {
            stockDetailsViewModel.processIntent(StockDetailsIntent.SetIdleEvent)
            moreActionsOnClick()
        }

        is StockDetailsEvent.NavigateBack -> {
            stockDetailsViewModel.processIntent(StockDetailsIntent.SetIdleEvent)
            backOnClick()
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
                    with(stockDetails) {
                        StockDetailsContent(
                            images = images.toResources(),
                            code = code,
                            description = description,
                            secondaryDescription = secondaryDescription,
                            beverageColor = color,
                            beverageDescription = beverageDescription,
                            ownerName = ownerName,
                            unitName = unitName,
                            onHand = stockLevels.onHand,
                            committed = stockLevels.committed,
                            inProduction = stockLevels.inProduction,
                            available = stockLevels.available,
                            components = stockComponents,
                            componentOnClick = { id ->
                                stockDetailsViewModel.processIntent(
                                    StockDetailsIntent.ComponentOnClick(
                                        id
                                    )
                                )
                            },
                            backOnClick = { stockDetailsViewModel.processIntent(StockDetailsIntent.BackOnClick) },
                            editOnClick = { message ->
                                stockDetailsViewModel.processIntent(
                                    StockDetailsIntent.EditOnClick(
                                        message
                                    )
                                )
                            },
                            moreActionsOnClick = {
                                stockDetailsViewModel.processIntent(StockDetailsIntent.MoreActionsOnClick)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StockDetailsContent(
    images: List<Int>,
    code: String,
    description: String,
    secondaryDescription: String?,
    beverageColor: String?,
    beverageDescription: String?,
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

    Box {
        CollapsedTopBar(
            isCollapsed = isCollapsed,
            backOnClick = backOnClick,
            editOnClick = editOnClick,
            moreActionsOnClick = moreActionsOnClick,
            title = code,
            modifier = Modifier.zIndex(2f)
        )

        LazyColumn(state = lazyListState) {
            item {
                ExpandedTopBar(
                    headerImages = images,
                    backOnClick = backOnClick,
                    editOnClick = editOnClick,
                    moreActionsOnClick = moreActionsOnClick
                )
            }

            item {
                StockInformation(
                    code = code,
                    description = description,
                    secondaryDescription = secondaryDescription,
                    beverageColor = beverageColor,
                    beverageDescription = beverageDescription,
                    ownerName = ownerName,
                    unitName = unitName
                )
            }

            item {
                Spacer(modifier = Modifier.height(DimenLarge))

                LevelsInformation(
                    onHand = onHand,
                    committed = committed,
                    inProduction = inProduction,
                    available = available,
                    editOnClick = editOnClick
                )

                Spacer(modifier = Modifier.height(DimenSmall))
            }

            if (components.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(DimenLarge))

                    ComponentsInformation(
                        components = components,
                        componentOnClick = componentOnClick
                    )

                    Spacer(modifier = Modifier.height(DimenListBottomSpacer))
                }
            }
        }
    }
}

@Composable
private fun StockInformation(
    modifier: Modifier = Modifier,
    code: String,
    description: String,
    secondaryDescription: String?,
    beverageColor: String?,
    beverageDescription: String?,
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

            Text(
                text = code,
                style = Typography.titleLarge,
                modifier = Modifier
                    .testTag(stringResource(id = R.string.test_tag_code))
            )

            Spacer(modifier = Modifier.height(DimenNano))

            Text(
                text = description,
                style = Typography.bodyMedium,
                modifier = Modifier
                    .testTag(stringResource(id = R.string.test_tag_description))
            )

            Spacer(modifier = Modifier.height(DimenNano))

            secondaryDescription?.let {
                Text(
                    text = it,
                    style = Typography.labelMedium,
                    modifier = Modifier
                        .testTag(stringResource(id = R.string.test_tag_secondary_description))
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = DimenSmall, bottom = DimenSmall)
            ) {
                beverageColor?.let {
                    if (it.isNotBlank()) {
                        Canvas(
                            onDraw = {
                                drawCircle(color = Color(beverageColor.toColorInt()))
                            },
                            modifier = Modifier
                                .size(DimenSmall)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(DimenMicro))

                beverageDescription?.let {
                    Text(
                        text = it,
                        style = Typography.bodySmall,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_beverage_description))

                    )
                }
            }

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = stringResource(id = R.string.accessibility_owner_icon),
                    tint = Green60
                )

                Spacer(modifier = Modifier.width(DimenMicro))

                Text(
                    text = ownerName,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .testTag(stringResource(id = R.string.test_tag_owner_name))
                )
            }

            Spacer(modifier = Modifier.height(DimenNano))

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.ic_measuring_cup),
                    contentDescription = stringResource(id = R.string.accessibility_measuring_cup_icon),
                    tint = Green60
                )

                Spacer(modifier = Modifier.width(DimenMicro))

                Text(
                    text = unitName,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .testTag(stringResource(id = R.string.test_tag_unit_name))
                )
            }
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
    editOnClick: (String) -> Unit
) {
    val editToastMessage = stringResource(id = R.string.edit_button_toast)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DimenMedium, end = DimenMedium)
        ) {
            Text(
                text = stringResource(id = R.string.levels_title),
                style = Typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag(stringResource(id = R.string.test_tag_level_title))
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { editOnClick(editToastMessage) }
            ) {
                Text(
                    text = stringResource(id = R.string.edit_button),
                    modifier = Modifier
                        .testTag(stringResource(id = R.string.test_tag_edit_button))
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(DimenMicro),
            elevation = CardDefaults.cardElevation(DimenNano),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DimenMicro, end = DimenMicro)
        ) {
            Column(
                modifier = Modifier
                    .padding(DimenSmall)
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.levels_on_hand_title),
                        style = Typography.bodyMedium,
                        color = Grey56,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_on_hand_title))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = onHand.numberFormat(),
                        style = Typography.bodyMedium,
                        color = Grey21,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_on_hand_amount))
                    )
                }

                Spacer(modifier = Modifier.height(DimenSmall))

                Divider(
                    color = Grey88,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DimenMicro, end = DimenMicro)
                )

                Spacer(modifier = Modifier.height(DimenSmall))

                Row {
                    Text(
                        text = stringResource(id = R.string.levels_committed_title),
                        style = Typography.bodyMedium,
                        color = Grey56,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_committed_title))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = committed.numberFormat(),
                        style = Typography.bodyMedium,
                        color = Grey21,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_committed_amount))
                    )
                }

                Spacer(modifier = Modifier.height(DimenSmall))

                Divider(
                    color = Grey88,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DimenMicro, end = DimenMicro)
                )

                Spacer(modifier = Modifier.height(DimenSmall))

                Row {
                    Text(
                        text = stringResource(id = R.string.levels_in_production_title),
                        style = Typography.bodyMedium,
                        color = Grey56,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_in_production_title))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = inProduction.numberFormat(),
                        style = Typography.bodyMedium,
                        color = Grey21,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_in_production_amount))
                    )

                }

                Spacer(modifier = Modifier.height(DimenSmall))

                Divider(
                    color = Grey88,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DimenMicro, end = DimenMicro)
                )

                Spacer(modifier = Modifier.height(DimenSmall))

                Row {
                    val availableTextColor =
                        when {
                            available > 0 -> Cyan59
                            available < 0 -> Color.Red
                            else -> Grey21
                        }

                    Text(
                        text = stringResource(id = R.string.levels_available_title),
                        style = Typography.bodyMedium,
                        color = Grey56,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_available_title))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = available.numberFormat(),
                        style = Typography.bodyMedium,
                        color = availableTextColor,
                        modifier = Modifier
                            .testTag(stringResource(id = R.string.test_tag_available_amount))
                    )
                }
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

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DimenMedium, end = DimenMedium)
        ) {
            Text(
                text = stringResource(id = R.string.components_title),
                style = Typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag(stringResource(id = R.string.test_tag_components_title))
            )

            Spacer(modifier = Modifier.width(DimenNano))

            Text(
                text = "(${components.size})",
                style = Typography.labelLarge,
                color = Grey63,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag(stringResource(id = R.string.test_tag_components_amount))
            )
        }

        Spacer(modifier = Modifier.height(DimenSmall))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(DimenMicro),
            elevation = CardDefaults.cardElevation(DimenNano),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DimenMicro, end = DimenMicro)
        ) {
            Column(
                modifier = Modifier
                    .padding(DimenSmall)
            ) {
                components.forEachIndexed { index, stockComponents ->
                    Row(
                        modifier = Modifier
                            .clickable {
                                componentOnClick(stockComponents.id)
                            }
                    ) {
                        Column {
                            Text(
                                text = stockComponents.code,
                                style = Typography.bodyMedium,
                                color = Green60,
                                modifier = Modifier
                                    .testTag(stringResource(id = R.string.test_tag_components_code))
                            )

                            Spacer(modifier = Modifier.height(DimenNano))

                            Text(
                                text = stockComponents.description,
                                style = Typography.bodySmall,
                                color = Grey56,
                                modifier = Modifier
                                    .testTag(stringResource(id = R.string.test_tag_components_description))
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = stockComponents.quantity,
                            style = Typography.bodyMedium,
                            color = Grey31,
                            modifier = Modifier
                                .testTag(stringResource(id = R.string.test_tag_components_unit))
                        )

                        Spacer(modifier = Modifier.width(DimenMicro))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_forward),
                            contentDescription = stringResource(id = R.string.accessibility_icon_forward),
                            tint = Grey76
                        )
                    }

                    if (index < components.size - 1) {
                        Spacer(modifier = Modifier.height(DimenSmall))

                        Divider(
                            color = Grey88,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = DimenMicro, end = DimenMicro)
                        )

                        Spacer(modifier = Modifier.height(DimenSmall))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun StockDetailsContentPreview() {
    VintraceTheme {
        StockDetailsContent(
            images = listOf(R.drawable.img_generic),
            code = "CHRD/EU/2016",
            description = "Standard EU export bottle",
            secondaryDescription = "Bottle - 750 ml",
            beverageColor = "#DAC88B",
            beverageDescription = "2016 Chardonnay YV/MP",
            ownerName = "vintrace Winery",
            unitName = "Single bottle (x1)",
            onHand = 68000,
            committed = 20000,
            inProduction = 35000,
            available = 68000,
            components = listOf(
                StockComponents(
                    id = "/stock-items/2",
                    code = "16YAVCRD01/BLK",
                    description = "2016 Chardonnay YV/MP",
                    quantity = "12"
                ),
            ),
            componentOnClick = {},
            backOnClick = {},
            editOnClick = {},
            moreActionsOnClick = {}
        )
    }
}