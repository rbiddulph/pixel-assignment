package com.biddulph.pixel.data

import com.biddulph.pixel.service.UserServiceImpl
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UserTest {

    val remoteUser = UserRemote(user_id = 100, display_name = "Test User", reputation = 1, profile_image = null)
    val localUserFollowed = UserLocal(userId = 100, followed = true)
    val localUserUnfollowed = UserLocal(userId = 100, followed = true)

    @Test
    fun `merge users correctly with remote fields`() {
        val mergedUser = UserServiceImpl.merge(remoteUser, localUserFollowed.followed)

        assertEquals(remoteUser.user_id, mergedUser.id)
        assertEquals(remoteUser.display_name, mergedUser.name)
        assertEquals(remoteUser.reputation, mergedUser.reputation)
        assertEquals(remoteUser.profile_image, mergedUser.profileImage)
    }

    @Test
    fun `merge users correctly when followed`() {
        val mergedUser = UserServiceImpl.merge(remoteUser, localUserFollowed.followed)

        assertEquals(remoteUser.user_id, mergedUser.id)
        assertEquals(localUserFollowed.followed, mergedUser.followed)
    }

    @Test
    fun `merge users correctly when unfollowed`() {
        val mergedUser = UserServiceImpl.merge(remoteUser, localUserUnfollowed.followed)

        assertEquals(remoteUser.user_id, mergedUser.id)
        assertEquals(localUserUnfollowed.followed, mergedUser.followed)
    }
}