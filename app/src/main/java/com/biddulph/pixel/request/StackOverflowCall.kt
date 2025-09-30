package com.biddulph.pixel.request

import com.biddulph.pixel.data.UserRemoteResponse

interface StackOverflowCall {

    suspend fun fetchTopUsers(): UserRemoteResponse
}