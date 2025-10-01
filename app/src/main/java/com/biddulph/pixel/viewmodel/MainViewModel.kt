package com.biddulph.pixel.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val mutableState = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val state : StateFlow<MainViewState> = mutableState


}