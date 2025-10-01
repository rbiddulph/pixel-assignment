package com.biddulph.pixel.parser

import com.biddulph.pixel.data.UserRemote
import com.biddulph.pixel.data.UserRemoteResponse
import org.json.JSONObject
import kotlin.text.ifEmpty

object UserJsonParser {
     fun parse(jsonString: String): UserRemoteResponse {
        val jsonObject = JSONObject(jsonString)
        val items = jsonObject.getJSONArray("items")

        val parsedUsers = (0 until items.length()).map { index ->
            val item = items.getJSONObject(index)

            UserRemote(
                user_id = item.getInt("user_id"),
                display_name = item.getString("display_name"),
                reputation = item.getInt("reputation"),
                profile_image = item.optString("profile_image").ifEmpty { null }
            )
        }

        return UserRemoteResponse(items = parsedUsers)
    }
}