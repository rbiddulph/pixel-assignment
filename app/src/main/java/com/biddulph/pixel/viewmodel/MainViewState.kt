package com.biddulph.pixel.viewmodel

sealed class MainViewState {
    object Loading : MainViewState()
    object Loaded : MainViewState()
    // failed represents IO issues or zero users
    object Failed : MainViewState()
}