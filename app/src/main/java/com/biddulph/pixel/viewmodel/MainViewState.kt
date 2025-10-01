package com.biddulph.pixel.viewmodel

sealed class MainViewState {
    object Loading : MainViewState()
    object Loaded : MainViewState()
    object Failed : MainViewState()
}