package com.thavin.vintrace.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.thavin.vintrace.ui.theme.DimenNano
import com.thavin.vintrace.ui.theme.DimenXxLarge
import com.thavin.vintrace.ui.theme.Green60
import com.thavin.vintrace.ui.theme.VintraceTheme

@Composable
fun LoadingOverlay() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            strokeWidth = DimenNano,
            color = Green60,
            modifier = Modifier
                .size(DimenXxLarge)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoadingOverlayPreview() {
    VintraceTheme {
        LoadingOverlay()
    }
}