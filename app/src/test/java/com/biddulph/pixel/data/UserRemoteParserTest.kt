package com.biddulph.pixel.data

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class UserRemoteParserTest {

    @Test
    fun `parse users_sample json into UserRemoteResponse`() {

        val remoteResponse = parseUsersFromFile(filename = "/users_sample.json")

        assertEquals(1, remoteResponse.items.size)

        assertEquals(22656, remoteResponse.items[0].user_id)
        assertEquals("Jon Skeet", remoteResponse.items[0].display_name)
        assertEquals(1454978, remoteResponse.items[0].reputation)
        assertEquals("https://www.gravatar.com/avatar/6d8ebb117e8d83d74ea95fbdd0f87e13?s=256&d=identicon&r=PG", remoteResponse.items[0].profile_image)
    }

    @Test
    fun `parse users_tests json with optional profile image`() {
        val remoteResponse = parseUsersFromFile(filename = "/users_tests.json")

        assertEquals(2, remoteResponse.items.size)

        assertNotNull(remoteResponse.items[0].profile_image)
        assertNull(remoteResponse.items[1].profile_image)
    }

    @Test
    fun `parse users_empty json with no items`() {
        val remoteResponse = parseUsersFromFile(filename = "/users_empty.json")
        assertEquals(0, remoteResponse.items.size)
    }

    private fun parseUsersFromFile(filename: String): UserRemoteResponse {
        val jsonString = requireNotNull(javaClass.getResource(filename)) {
            "File not found"
        }.readText()

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