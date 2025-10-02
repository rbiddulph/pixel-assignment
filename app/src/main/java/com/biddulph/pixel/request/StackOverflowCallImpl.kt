package com.biddulph.pixel.request

import com.biddulph.pixel.data.UserRemoteResponse
import com.biddulph.pixel.parser.UserJsonParser
import java.net.HttpURLConnection
import java.net.URL

/**
 * Makes a request to a static url for the 20 top stackoverflow users
 */
class StackOverflowCallImpl : StackOverflowCall {
    
    private val urlString = "https://api.stackexchange.com/2.2/users?page=1&pagesize=20&order=desc&sort=reputation&site=stackoverflow"

    override suspend fun fetchTopUsers(): UserRemoteResponse {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        return try{
            val responseCode = connection.responseCode
            if (responseCode != 200){
                // more intelligent to handle these other response codes, but for now we're only building a simple app
                throw RuntimeException("unexpected response code [$responseCode]")
            }

            val inputStreamString = connection.inputStream.bufferedReader().readText()
            UserJsonParser.parse(inputStreamString)
        } finally {
            connection.disconnect()
        }
    }
}