package com.biddulph.pixel.storage

/**
 * Storage for the follower flag
 */
class FollowerStorage {

    //TODO datastore obj
    
    suspend fun getFollowedUserIds(): List<Int>{
        //TODO return all user ids where followed = true
        return ArrayList()
    }

    suspend fun toggleFollowedStateForUser(userId : Int){
        //TODO if user exists, flip state, else add
    }
}