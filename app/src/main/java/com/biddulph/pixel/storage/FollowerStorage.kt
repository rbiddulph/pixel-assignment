package com.biddulph.pixel.storage

/**
 * interface for the follower store - separated into interface to allow it to be mocked
 */
interface FollowerStorage {

    suspend fun getFollowedUserIds(): List<Int>
    suspend fun toggleFollowedStateForUser(userId: Int)
}