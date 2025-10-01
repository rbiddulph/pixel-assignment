package com.biddulph.pixel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.biddulph.pixel.ui.theme.PixelTheme
import com.biddulph.pixel.viewmodel.MainViewModel
import com.biddulph.pixel.viewmodel.MainViewState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PixelTheme {
                val state = viewModel.state.collectAsStateWithLifecycle().value

                LaunchedEffect(Unit) {
                    //TODO load the user info
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(state,  modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    //TODO toggle user follow
    //TODO image loading from remote
}


@Composable
fun MainScreen(state: MainViewState, modifier: Modifier = Modifier) {
    when (state) {
        is MainViewState.Loading -> {
            Text("Loading State", modifier)

        }

        is MainViewState.Loaded -> {
            Text("Loaded list State", modifier)

        }

        is MainViewState.Failed -> {
            Text("Failed State", modifier)
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
        MainScreen(MainViewState.Loaded)
    }
}

@Preview(showBackground = true, name = "Failed State")
@Composable
fun UserListPreviewFailed() {
    PixelTheme {
        MainScreen(MainViewState.Failed)
    }
}
