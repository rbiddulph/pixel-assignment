package com.biddulph.pixel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.biddulph.pixel.service.UserService

class MainViewModelFactory(private val userService: UserService) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(userService) as T
    }


}