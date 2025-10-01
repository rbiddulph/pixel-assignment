package com.biddulph.pixel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biddulph.pixel.data.User
import com.biddulph.pixel.request.StackOverflowCallImpl
import com.biddulph.pixel.service.UserService
import com.biddulph.pixel.service.UserServiceImpl
import com.biddulph.pixel.storage.FollowerStorageImpl
import com.biddulph.pixel.ui.theme.PixelTheme
import com.biddulph.pixel.viewmodel.MainViewModel
import com.biddulph.pixel.viewmodel.MainViewModelFactory
import com.biddulph.pixel.viewmodel.MainViewState

class MainActivity : ComponentActivity() {

    private lateinit var service: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // stack overflow call
        val apiRequest = StackOverflowCallImpl()
        val storage = FollowerStorageImpl(applicationContext)
        service = UserServiceImpl(remoteCall = apiRequest, localStorage = storage)

        val viewModel: MainViewModel = ViewModelProvider(this, MainViewModelFactory(service))[MainViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            PixelTheme {
                val state = viewModel.state.collectAsStateWithLifecycle().value

                LaunchedEffect(Unit) {
                    // load the user info from view model
                    viewModel.loadUsers()
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(state, modifier = Modifier.padding(innerPadding),
                        onToggleFollowClick = {
                            // this will toggle user follow status and refresh (without data fetch)
                            viewModel.toggleFollow(it)
                        },
                        onRetryClick = {
                            // this will try to load user info again
                            viewModel.loadUsers()
                        })
                }
            }
        }
    }
}


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

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CircularProgressIndicator()
            Text("Loading...",
                textAlign = TextAlign.Center,
            )
        }
    }
}

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
            Text(text = errorMessage,
                textAlign = TextAlign.Center,
            )
            Button(content = { Text("Retry") },
                onClick = onRetryClick)
        }
    }
}

@Composable
fun UserListScreen(
    users: List<User>,
    modifier: Modifier = Modifier,
    onToggleFollowClick: (Int) -> Unit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn (
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(users) { user ->
                UserListItem(user, onToggleFollowClick)
            }
        }
    }
}

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
                Image(painter = painterResource(id = R.drawable.profile_placeholder),//TODO we want to download from user.profileImage
                    contentDescription = user.name,
                    modifier = Modifier
                        .requiredSize(40.dp)
                        .align(Alignment.Center))
                if (user.followed) { // indicate that the user is followed with an icon
                    Image(painter = painterResource(id = R.drawable.user_followed),
                        contentDescription = "${user.name} is followed",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.TopEnd))
                }
            }
            Column(modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)) {
                Text(text = user.name, //TODO Christian C. Salvadó character encoding
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp)
                Text("${user.reputation}")
            }
            val followedText = if (user.followed) "Unfollow" else "Follow"
            Button(content = { Text(followedText) },
                onClick = { onToggleFollowClick(user.id) })
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun UserListPreviewLoading() {
    PixelTheme {
        MainScreen(MainViewState.Loading)
    }
}

@Preview(showBackground = true, name = "Loaded State")
@Composable
fun UserListPreviewLoaded() {
    PixelTheme {
        val previewUsers = listOf(
            User(1, "Alice", 100, "https://", false),
            User(2, "Bob", 200, "https://", true),
            User(3, "Chad", 300, "https://", false),
        )
        MainScreen(MainViewState.Loaded(users = previewUsers))
    }
}

@Preview(showBackground = true, name = "Failed State")
@Composable
fun UserListPreviewFailed() {
    PixelTheme {
        MainScreen(MainViewState.Failed("Preview Error: This is an error that uses two lines on a small screen device"))
    }
}
