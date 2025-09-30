package com.biddulph.pixel.service

import com.biddulph.pixel.request.StackOverflowCall
import com.biddulph.pixel.data.UserRemote
import com.biddulph.pixel.data.UserRemoteResponse
import com.biddulph.pixel.storage.FollowerStorage
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserServiceImplTest {

    class FakeCall : StackOverflowCall {
        override suspend fun fetchTopUsers(): UserRemoteResponse {
            val listOfUsers = listOf<UserRemote>(
                UserRemote(user_id = 1, display_name = "A", reputation = 100, profile_image = "https://..."),
                UserRemote(user_id = 2, display_name = "B", reputation = 200, profile_image = "https://...")
            )
            return UserRemoteResponse(listOfUsers)
        }

    }

    class FakeStore : FollowerStorage {

        private val store = emptySet<Int>().toMutableSet()
        override suspend fun getFollowedUserIds(): List<Int> {
            return store.toList()
        }

        override suspend fun toggleFollowedStateForUser(userId: Int) {
            if (store.contains(userId)) {
                store.remove(userId)
            } else {
                store.add(userId)
            }
        }

    }

    @Test
    fun `check fetchTopUsers returns data`() = runTest {
        val fakeCall = FakeCall()
        val fakeStorage = FakeStore()

        val service = UserServiceImpl(fakeCall, fakeStorage)

        val result = service.loadTopUsers()

        assertTrue(result.isSuccess)

        val values = result.getOrNull()

        assertNotNull(values)

        val firstItem = values?.get(0)
        val secondItem = values?.get(1)

        assertEquals("A", firstItem?.name)
        assertEquals("B", secondItem?.name)
    }

    @Test
    fun `check toggleFollow returns new data`() = runTest {
        val fakeCall = FakeCall()
        val fakeStorage = FakeStore()

        val service = UserServiceImpl(fakeCall, fakeStorage)

        val loadBeforeToggleResult = service.loadTopUsers()

        assertTrue(loadBeforeToggleResult.isSuccess)

        val values = loadBeforeToggleResult.getOrNull()

        assertNotNull(values)

        val firstItemBeforeToggle = values?.get(0)
        assertFalse(firstItemBeforeToggle?.followed == true)

        val toggleResult = service.toggleFollow(1)
        assertTrue(toggleResult.isSuccess)

        val loadAfterToggleResult = service.loadTopUsers()
        assertTrue(loadAfterToggleResult.isSuccess)

        val firstItemAfterToggle = values?.get(0)
        assertTrue(firstItemAfterToggle?.followed == false)
    }
}