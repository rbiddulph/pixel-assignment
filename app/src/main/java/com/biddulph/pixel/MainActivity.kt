package com.biddulph.pixel

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biddulph.pixel.data.User
import com.biddulph.pixel.data.UserRemote
import com.biddulph.pixel.data.UserRemoteResponse
import com.biddulph.pixel.request.StackOverflowCall
import com.biddulph.pixel.service.UserService
import com.biddulph.pixel.service.UserServiceImpl
import com.biddulph.pixel.storage.FollowerStorage
import com.biddulph.pixel.ui.theme.PixelTheme
import com.biddulph.pixel.viewmodel.MainViewModel
import com.biddulph.pixel.viewmodel.MainViewModelFactory
import com.biddulph.pixel.viewmodel.MainViewState

class MainActivity : ComponentActivity() {

    //TODO move to real API
    class FakeCall : StackOverflowCall {
        override suspend fun fetchTopUsers(): UserRemoteResponse {
            val listOfUsers = listOf<UserRemote>(
                UserRemote(user_id = 1, display_name = "A", reputation = 100, profile_image = "https://..."),
                UserRemote(user_id = 2, display_name = "B", reputation = 200, profile_image = "https://...")
            )
            return UserRemoteResponse(listOfUsers)
        }
    }

    //TODO move to real store
    class FakeStore : FollowerStorage {

        private val store = emptySet<Int>().toMutableSet()
        override suspend fun getFollowedUserIds(): List<Int> {
            return store.toList()
        }

        override suspend fun toggleFollowedStateForUser(userId: Int) {
            if (store.contains(userId)) {
                store.remove(userId)
            } else {
                store.add(userId)
            }
        }
    }

    private val service: UserService by lazy {
        UserServiceImpl(remoteCall = FakeCall(), localStorage = FakeStore())//TODO replace fakes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            //TODO this will toggle user follow status and refresh (without data fetch)
                        },
                        onRetryClick = {
                            //TODO this will try to load user info again
                        })
                }
            }
        }
    }
}


@Composable
fun MainScreen(state: MainViewState, modifier: Modifier = Modifier, onToggleFollowClick: () -> Unit = {}, onRetryClick: () -> Unit = {}) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text("Loading...")
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(errorMessage)
            Button(content = { Text("Retry") },
                onClick = onRetryClick)
        }
    }
}

@Composable
fun UserListScreen(
    users: List<User>,
    modifier: Modifier = Modifier,
    onToggleFollowClick: () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn (
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(users) { user ->
                UserListItem(user)
            }
        }
    }
}

@Composable
fun UserListItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(user.name)
            user.profileImage?.let { Text(it) } //TODO profile image loading
            if (user.followed) { //TODO toggle user follow state
                Text("Followed")
            } else {
                Text("Unfollowed")
            }
            Text("${user.reputation}")
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
        MainScreen(MainViewState.Failed("Preview Error"))
    }
}
