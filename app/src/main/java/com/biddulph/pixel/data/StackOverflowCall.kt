package com.biddulph.pixel.data

interface StackOverflowCall {

    //GET
    //http://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&
    //sort=reputation&site=stackoverflow

    suspend fun fetchTopUsers(): UserRemoteResponse
}