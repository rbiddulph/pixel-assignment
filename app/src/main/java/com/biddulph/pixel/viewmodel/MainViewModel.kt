package com.biddulph.pixel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biddulph.pixel.service.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel (private val userService: UserService): ViewModel() {

    private val mutableState = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val state : StateFlow<MainViewState> = mutableState

    fun loadUsers(){
        // ensure we're in the loading state and request the latest users
        mutableState.value = MainViewState.Loading
        viewModelScope.launch {
            val response = userService.loadTopUsers()
            if (response.isSuccess){
                mutableState.value = MainViewState.Loaded
            }else{
                mutableState.value = MainViewState.Failed
            }
        }
    }

    fun toggleFollow(userId: Int){
        // request the service to flip the follow state between true/false
        viewModelScope.launch {
            userService.toggleFollow(userId)
        }
    }
}