package com.biddulph.pixel.service

import android.R.attr.name
import com.biddulph.pixel.data.User
import com.biddulph.pixel.data.UserLocal
import com.biddulph.pixel.data.UserRemote

/**
 * service to feed users into the view model
 */
class UserServiceImp : UserService {


    override suspend fun loadTopUsers() {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFollow(userId: Int) {
        TODO("Not yet implemented")
    }

    fun merge(remoteUser: UserRemote, localUser: UserLocal): User {
        return User(
            id = remoteUser.user_id,
            name = remoteUser.display_name,
            reputation = remoteUser.reputation,
            profileImage = remoteUser.profile_image,
            followed = localUser.followed
        )
    }
}