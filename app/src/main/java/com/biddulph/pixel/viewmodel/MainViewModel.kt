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
                mutableState.value = MainViewState.Loaded(response.getOrThrow())
            }else{
                mutableState.value = MainViewState.Failed(response.exceptionOrNull()?.message ?: "Null error")
            }
        }
    }

    fun toggleFollow(userId: Int){
        // request the service to flip the follow state between true/false in the storage
        viewModelScope.launch {
            val result = userService.toggleFollow(userId)

            if (result.isSuccess && state.value is MainViewState.Loaded){
                // just update the chosen user state here - we know the flag is stored, so no full reload required
                val updatedUsers = (state.value as MainViewState.Loaded).users.map {
                    if (it.id == userId){
                        it.copy(followed = !it.followed)
                    }else{
                        it
                    }
                }
                mutableState.value = MainViewState.Loaded(updatedUsers)
            }
        }
    }
}