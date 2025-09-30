package com.biddulph.pixel.service

import com.biddulph.pixel.data.User
import com.biddulph.pixel.data.UserLocal
import com.biddulph.pixel.data.UserRemote

/**
 * service to feed users into the view model
 */
class UserServiceImpl : UserService {

    override suspend fun loadTopUsers(): Result<List<User>> {
       //TODO get list of top 20 users from SO, merge with local storage of followed ids, return
        return TODO("Provide the return value")
    }

    override suspend fun toggleFollow(userId: Int): Result<Unit> {
        //TODO update the follow state for this user id in FollowerStorage
        return TODO("Provide the return value")
    }

    fun merge(remoteUser: UserRemote, localUser: UserLocal?): User {
        return User(
            id = remoteUser.user_id,
            name = remoteUser.display_name,
            reputation = remoteUser.reputation,
            profileImage = remoteUser.profile_image,
            followed = localUser?.followed ?: false
        )
    }
}