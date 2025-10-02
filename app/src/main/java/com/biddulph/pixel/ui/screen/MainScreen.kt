package com.biddulph.pixel.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.biddulph.pixel.data.User
import com.biddulph.pixel.ui.theme.PixelTheme
import com.biddulph.pixel.viewmodel.MainViewState

@Composable
fun MainScreen(state: MainViewState, modifier: Modifier = Modifier, onToggleFollowClick: (Int) -> Unit = {}, onRetryClick: () -> Unit = {}) {
    when (state) {
        is MainViewState.Loading -> {
            LoadingScreen(modifier)

        }

        is MainViewState.Loaded -> {
            UserListScreen(state.users, modifier, onToggleFollowClick)

        }

        is MainViewState.Failed -> {
            FailedScreen(state.error, modifier, onRetryClick)
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun MainScreenPreviewLoading() {
    PixelTheme {
        MainScreen(MainViewState.Loading)
    }
}

@Preview(showBackground = true, name = "Failed State")
@Composable
fun MainScreenPreviewFailed() {
    PixelTheme {
        MainScreen(MainViewState.Failed("Preview Error"))
    }
}

@Preview(showBackground = true, name = "Loaded State")
@Composable
fun MainScreenPreviewLoaded() {
    PixelTheme {
        val previewUsers = listOf(
            User(1, "Alice", 100, "https://", false),
            User(2, "Bob", 200, "https://", true),
            User(3, "Chad", 300, "https://", false),
        )
        MainScreen(MainViewState.Loaded(users = previewUsers))
    }
}

