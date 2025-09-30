package com.biddulph.pixel.service

import com.biddulph.pixel.request.StackOverflowCall
import com.biddulph.pixel.data.User
import com.biddulph.pixel.data.UserRemote
import com.biddulph.pixel.storage.FollowerStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * service to feed users into the view model
 */
class UserServiceImpl(val remoteCall: StackOverflowCall, val localStorage: FollowerStorage) : UserService {

    override suspend fun loadTopUsers(): Result<List<User>> = withContext(Dispatchers.IO) {

        val apiCallResponse = remoteCall.fetchTopUsers()
        val localFollowers = localStorage.getFollowedUserIds().toSet()

        val users = apiCallResponse.items.map {
            merge(it, localUser = localFollowers.contains(it.user_id))
        }

        Result.success(users)
    }

    override suspend fun toggleFollow(userId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        localStorage.toggleFollowedStateForUser(userId)
        Result.success(Unit)
    }

    internal companion object {
        fun merge(remoteUser: UserRemote, localUser: Boolean): User {
            return User(
                id = remoteUser.user_id,
                name = remoteUser.display_name,
                reputation = remoteUser.reputation,
                profileImage = remoteUser.profile_image,
                followed = localUser
            )
        }
    }
}