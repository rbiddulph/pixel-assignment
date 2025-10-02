package com.biddulph.pixel.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.biddulph.pixel.R
import com.biddulph.pixel.ui.theme.PixelTheme

@Composable
fun FailedScreen(errorMessage: String, modifier: Modifier = Modifier, onRetryClick: () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(R.string.failed).format(errorMessage),
                textAlign = TextAlign.Center,
            )
            Button(content = { Text(stringResource(R.string.retry)) },
                onClick = onRetryClick)
        }
    }
}

@Preview(showBackground = true, name = "Failed State Short")
@Composable
fun UserListPreviewFailedShortMessage() {
    PixelTheme {
        FailedScreen("Preview Error", onRetryClick = {})
    }
}

@Preview(showBackground = true, name = "Failed State")
@Composable
fun UserListPreviewFailedLongMessage() {
    PixelTheme {
        FailedScreen("Preview Error. This is an error that uses two lines on a small screen device", onRetryClick = {})
    }
}