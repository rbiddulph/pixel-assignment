package com.biddulph.pixel.service

import com.biddulph.pixel.data.User

/**
 * contract for users from data to ui
 */
interface UserService {
    suspend fun loadTopUsers() : Result<List<User>>
    suspend fun toggleFollow(userId : Int) : Result<Unit>
}