package com.biddulph.pixel.viewmodel

import com.biddulph.pixel.data.UserRemote
import com.biddulph.pixel.data.UserRemoteResponse
import com.biddulph.pixel.request.StackOverflowCall
import com.biddulph.pixel.service.UserServiceImpl
import com.biddulph.pixel.storage.FollowerStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    class FakeCallSuccess : StackOverflowCall {
        override suspend fun fetchTopUsers(): UserRemoteResponse {
            val listOfUsers = listOf<UserRemote>(
                UserRemote(user_id = 1, display_name = "A", reputation = 100, profile_image = "https://..."),
                UserRemote(user_id = 2, display_name = "B", reputation = 200, profile_image = "https://...")
            )
            return UserRemoteResponse(listOfUsers)
        }
    }

    class FakeCallFailure : StackOverflowCall {
        override suspend fun fetchTopUsers(): UserRemoteResponse {
            val listOfUsers = listOf<UserRemote>()
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
    fun `check state launches as loading`() = runTest {
        val service = UserServiceImpl(remoteCall = FakeCallSuccess(), localStorage = FakeStore())
        val viewModel = MainViewModel(userService = service)
        assertTrue(viewModel.state.value is MainViewState.Loading)

    }

    @Test
    fun `check state success once data loaded`() = runTest {
        val service = UserServiceImpl(remoteCall = FakeCallSuccess(), localStorage = FakeStore())
        val viewModel = MainViewModel(userService = service)
        viewModel.loadUsers()
        advanceUntilIdle()
        assertTrue(viewModel.state.value is MainViewState.Loaded)
    }

    @Test
    fun `check state failed once data load failed`() = runTest {
        val service = UserServiceImpl(remoteCall = FakeCallFailure(), localStorage = FakeStore())
        val viewModel = MainViewModel(userService = service)
        viewModel.loadUsers()
        advanceUntilIdle()
        assertTrue(viewModel.state.value is MainViewState.Failed)
    }
}