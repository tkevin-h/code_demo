package com.thavin.vintrace.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.vintrace.R
import com.thavin.vintrace.ui.theme.DimenCollapsedTopBarHeight
import com.thavin.vintrace.ui.theme.DimenExpandedTopBarHeight
import com.thavin.vintrace.ui.theme.DimenExtraLarge
import com.thavin.vintrace.ui.theme.DimenLarge
import com.thavin.vintrace.ui.theme.DimenMicro
import com.thavin.vintrace.ui.theme.DimenSmall
import com.thavin.vintrace.ui.theme.DimenTopBarPadding
import com.thavin.vintrace.ui.theme.DimenZero
import com.thavin.vintrace.ui.theme.Green60
import com.thavin.vintrace.ui.theme.Typography
import com.thavin.vintrace.ui.theme.VintraceTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedTopBar(
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
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        NavBar(
            backOnClick = backOnClick,
            editOnClick = editOnClick,
            moreActionsOnClick = moreActionsOnClick,
            modifier = Modifier
                .padding(
                    top = DimenExtraLarge,
                    start = DimenMicro,
                    end = DimenMicro
                )
        )
    }
}

@Composable
fun CollapsedTopBar(
    modifier: Modifier = Modifier,
    isCollapsed: Boolean,
    backOnClick: () -> Unit,
    editOnClick: (String) -> Unit,
    moreActionsOnClick: () -> Unit,
    title: String
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
                title = title,
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
                .testTag(stringResource(id = R.string.test_tag_nav_back_button))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_back),
                contentDescription = stringResource(id = R.string.accessibility_back_button),
                tint = contentColor
            )
        }

        Spacer(modifier = Modifier.width(DimenExtraLarge))

        Text(
            text = title,
            style = Typography.titleSmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .testTag(stringResource(id = R.string.test_tag_nav_title))
        )

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
                .testTag(stringResource(id = R.string.test_tag_nav_edit_button))
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
                .testTag(stringResource(id = R.string.test_tag_nav_more_actions_button))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_more_actions),
                contentDescription = stringResource(id = R.string.accessibility_more_actions_button),
                tint = contentColor
            )
        }
    }
}

@Preview
@Composable
fun ExpandedTopBarPreview() {
    VintraceTheme {
        ExpandedTopBar(
            headerImages = listOf(R.drawable.img_generic),
            backOnClick = {},
            editOnClick = {},
            moreActionsOnClick = {}
        )
    }
}

@Preview
@Composable
fun CollapsedTopBarPreview() {
    VintraceTheme {
        CollapsedTopBar(
            isCollapsed = true,
            backOnClick = {},
            editOnClick = {},
            moreActionsOnClick = {},
            title = "CHRD/EU/2016"
        )
    }
}