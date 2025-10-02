package com.biddulph.pixel.request

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Download bitmap images directly from url if available
 */
object ProfileImageDownloader {
    suspend fun downloadImage(urlString: String?): Bitmap? = withContext(Dispatchers.IO) {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        try {
            val responseCode = connection.responseCode
            if (responseCode != 200) {
                null
            }
            val bitmap = BitmapFactory.decodeStream(connection.inputStream)
            bitmap
        } finally {
            connection.disconnect()
        }
    }
}