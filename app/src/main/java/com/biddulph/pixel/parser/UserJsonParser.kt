package com.biddulph.pixel.parser

import androidx.core.text.HtmlCompat
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

            // ensure HTML encoding of display names is handled - for the purpose of this exercise just using a simple parser will suffice
            UserRemote(
                user_id = item.getInt("user_id"),
                display_name = HtmlCompat.fromHtml(item.getString("display_name"), HtmlCompat.FROM_HTML_MODE_COMPACT).toString(),
                reputation = item.getInt("reputation"),
                profile_image = item.optString("profile_image").ifEmpty { null }
            )
        }

        return UserRemoteResponse(items = parsedUsers)
    }
}