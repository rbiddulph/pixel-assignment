package com.biddulph.pixel.data

import org.json.JSONObject
import org.junit.Assert.assertNotNull
import org.junit.Test

class UserRemoteParserTest {

    @Test
    fun `parse users_sample json into UserRemoteResponse`(){

        val jsonString = requireNotNull(javaClass.getResource("/users_sample.json")){
            "File not found"
        }.readText()

        println("UserRemoteParserTest json = $jsonString")

        assertNotNull(jsonString)

        val jsonObject = JSONObject(jsonString)
        println("UserRemoteParserTest jsonObject = $jsonObject")

        //TODO json parsing into data objects

    }
}