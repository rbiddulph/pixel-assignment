package com.biddulph.pixel.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.biddulph.pixel.ui.listitem.UserListItem
import com.biddulph.pixel.data.User
import com.biddulph.pixel.ui.theme.PixelTheme

@Composable
fun UserListScreen(
    users: List<User>,
    modifier: Modifier = Modifier,
    onToggleFollowClick: (Int) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(users) { user ->
                UserListItem(user, onToggleFollowClick)
            }
        }
    }
}


@Preview(showBackground = true, name = "Regular List")
@Composable
fun UserListPreviewLoaded() {
    PixelTheme {
        val previewUsers = listOf(
            User(1, "Alice", 100, "https://", false),
            User(2, "Bob", 200, "https://", true),
            User(3, "Chad", 300, "https://", false),
        )
        UserListScreen(users = previewUsers, onToggleFollowClick = {})
    }
}