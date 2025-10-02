package com.biddulph.pixel.storage

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FollowerStorageTest {

    private lateinit var storage: FollowerStorageImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        storage = FollowerStorageImpl(context)
    }

    @Test
    fun `check follower storage empty`() = runTest {
        val results = storage.getFollowedUserIds()

        assertEquals(0, results.size)
    }

    @Test
    fun `check following user increases count`() = runTest {
        // user id of 1
        storage.toggleFollowedStateForUser(1)
        val results = storage.getFollowedUserIds()

        assertEquals(1, results.size)
    }

    @Test
    fun `check unfollowing user decreases count`() = runTest {
        // user id of 1
        storage.toggleFollowedStateForUser(1)
        val results = storage.getFollowedUserIds()

        assertEquals(1, results.size)

        storage.toggleFollowedStateForUser(1)
        val resultsPostUnfollow = storage.getFollowedUserIds()

        assertEquals(0, resultsPostUnfollow.size)
    }

}