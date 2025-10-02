package com.biddulph.pixel.cache

import android.graphics.Bitmap
import androidx.collection.LruCache

/**
 * Simple cache of 100MB for images in memory - should suffice for 20 bitmaps
 */
object ProfileImageCache {

    private const val MAX_MEMORY = 100 * 1024 //about 100MB

    private val cache = LruCache<String, Bitmap>(maxSize = MAX_MEMORY)

    fun get(url: String): Bitmap?{
        return cache[url]
    }

    fun put(url: String, bitmap: Bitmap){
        cache.put(url, bitmap)
    }
}