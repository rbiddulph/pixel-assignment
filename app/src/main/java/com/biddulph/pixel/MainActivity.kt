package com.biddulph.pixel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biddulph.pixel.request.StackOverflowCallImpl
import com.biddulph.pixel.service.UserService
import com.biddulph.pixel.service.UserServiceImpl
import com.biddulph.pixel.storage.FollowerStorageImpl
import com.biddulph.pixel.ui.screen.MainScreen
import com.biddulph.pixel.ui.theme.PixelTheme
import com.biddulph.pixel.viewmodel.MainViewModel
import com.biddulph.pixel.viewmodel.MainViewModelFactory

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