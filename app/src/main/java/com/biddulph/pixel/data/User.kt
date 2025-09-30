package com.biddulph.pixel.data

/**
 * Combines our UserLocal for storage and UserRemote for remote data
 */
data class User (val id: Int, val name: String, val reputation: Int, val profileImage: String?, val followed: Boolean)
