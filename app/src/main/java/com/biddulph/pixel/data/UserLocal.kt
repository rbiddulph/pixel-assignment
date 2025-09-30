package com.biddulph.pixel.data

/**
 * Represents a user from our app POV
 */
data class UserLocal (val userId: Int, val followed: Boolean)
// userId to link to UserRemote, followed for local followed state