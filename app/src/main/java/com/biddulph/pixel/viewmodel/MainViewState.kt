package com.biddulph.pixel.viewmodel

import com.biddulph.pixel.data.User

sealed class MainViewState {
    object Loading : MainViewState()
    data class Loaded(val users: List<User>) : MainViewState()
    // failed represents IO issues or zero users
    data class Failed(val error: String) : MainViewState()
}