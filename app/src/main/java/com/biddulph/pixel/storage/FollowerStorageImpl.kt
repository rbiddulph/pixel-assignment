package com.biddulph.pixel.storage

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Storage for the follower flag
 */
class FollowerStorageImpl(context: Context) : FollowerStorage {

    private companion object Companion {
        const val PREFS_NAME = "followers_pref"
        const val PREFS_FOLLOWERS = "followers"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun getFollowedUserIds(): List<Int> = withContext(Dispatchers.IO) {
        val results = prefs.getStringSet(PREFS_FOLLOWERS, emptySet<String>()) ?: emptySet<String>()
        results.map { value -> value.toInt() }
    }

    override suspend fun toggleFollowedStateForUser(userId: Int) = withContext(Dispatchers.IO) {
        val currentResults = prefs.getStringSet(PREFS_FOLLOWERS, emptySet<String>()) ?: emptySet<String>()
        val copy = currentResults.toMutableSet()
        val userIdString = userId.toString()
        if (copy.contains(userIdString)) {
            copy.remove(userIdString)
        } else {
            copy.add(userIdString)
        }
        prefs.edit { putStringSet(PREFS_FOLLOWERS, copy) }
    }
}