package com.biddulph.pixel.data

import com.biddulph.pixel.parser.UserJsonParser
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
        return UserJsonParser.parse(jsonString)
    }
}