package com.thavin.vintrace.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.vintrace.R
import com.thavin.vintrace.ui.theme.DimenMicro
import com.thavin.vintrace.ui.theme.DimenSmall
import com.thavin.vintrace.ui.theme.DimenXxLarge
import com.thavin.vintrace.ui.theme.Green60
import com.thavin.vintrace.ui.theme.VintraceTheme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String?
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = stringResource(id = R.string.accessibility_error_icon),
                tint = Green60,
                modifier = Modifier
                    .size(DimenXxLarge)
            )

            Spacer(modifier = Modifier.height(DimenSmall))

            message?.let {
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(DimenMicro)
                        .testTag(stringResource(id = R.string.test_tag_error_message))
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ErrorScreenPreview() {
    VintraceTheme {
        ErrorScreen(
            message = "There was an error"
        )
    }
}