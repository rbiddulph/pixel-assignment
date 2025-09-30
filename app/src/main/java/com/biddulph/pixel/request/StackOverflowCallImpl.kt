package com.biddulph.pixel.request

import com.biddulph.pixel.data.UserRemoteResponse

class StackOverflowCallImpl : StackOverflowCall {

    //GET
    //http://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&
    //sort=reputation&site=stackoverflow

    override suspend fun fetchTopUsers(): UserRemoteResponse {
        //TODO download data
        return UserRemoteResponse(emptyList())
    }
}