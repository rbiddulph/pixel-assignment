package com.biddulph.pixel.ui.listitem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.biddulph.pixel.R
import com.biddulph.pixel.data.User
import com.biddulph.pixel.ui.theme.PixelTheme
import com.biddulph.pixel.ui.view.ProfileImage

@Composable
fun UserListItem(user: User, onToggleFollowClick: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.requiredSize(64.dp)) {
                ProfileImage(url = user.profileImage, // ProfileImage wraps an Image and swaps in a remote image if available
                    contentDescription = user.name,
                    modifier = Modifier
                        .requiredSize(40.dp)
                        .align(Alignment.Center))
                if (user.followed) { // indicate that the user is followed with an icon
                    Image(painter = painterResource(id = R.drawable.user_followed),
                        contentDescription = stringResource(R.string.is_followed, user.name),
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd))
                }
            }
            Column(modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = user.name,
                    style = MaterialTheme.typography.titleLarge)
                Text(text = stringResource(R.string.d_reputation).format(user.reputation),
                    style = MaterialTheme.typography.labelSmall)
            }
            val followedText = if (user.followed) stringResource(R.string.unfollow) else stringResource(R.string.follow)
            if (user.followed){
                OutlinedButton(content = { Text(followedText) },
                    onClick = { onToggleFollowClick(user.id) })
            }else{
                Button(content = { Text(followedText) },
                    onClick = { onToggleFollowClick(user.id) })
            }
        }
    }
}

@Preview(showBackground = true, name = "User List Item Unfollowed")
@Composable
fun UserListItemPreviewFollowed() {
    PixelTheme {
        UserListItem (User(1, "Alice", 100, "https://", false), {})
    }
}

@Preview(showBackground = true, name = "User List Item Followed")
@Composable
fun UserListItemPreviewUnfollowed() {
    PixelTheme {
        UserListItem (User(1, "Alice", 100, "https://", true), {})
    }
}

@Preview(showBackground = true, name = "User List Item Edge Cases")
@Composable
fun UserListItemPreviewEdgeCases() {
    PixelTheme {
        UserListItem (User(1, "Alice with a very long surname", 1000000000, "https://", true), {})
    }
}