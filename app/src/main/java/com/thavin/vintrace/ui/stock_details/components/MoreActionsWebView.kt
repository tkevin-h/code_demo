package com.thavin.vintrace.ui.stock_details.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.thavin.vintrace.R

@Composable
fun MoreActionsWebView() {
    val url = stringResource(id = R.string.vintrace_home_url)

    AndroidView(factory = {
        WebView(it).apply {
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
}