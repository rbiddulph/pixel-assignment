package com.biddulph.pixel.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import com.biddulph.pixel.R
import com.biddulph.pixel.cache.ProfileImageCache
import com.biddulph.pixel.request.ProfileImageDownloader

@Composable
fun ProfileImage(url: String?,
                 contentDescription: String,
                 modifier: Modifier = Modifier) {

    // keep a reference to the bitmap through state change
    var remoteBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // don't run in previews
    if (!LocalInspectionMode.current) {
        // use URL to download image
        LaunchedEffect(url) {
            // check cache
            if (url != null) {
                // check cache
                remoteBitmap = ProfileImageCache.get(url)
                if (remoteBitmap == null) {
                    // download new
                    remoteBitmap = ProfileImageDownloader.downloadImage(url)
                    remoteBitmap?.let { ProfileImageCache.put(url, it) }
                }
            }else{
                remoteBitmap = null
            }
        }
    }

    // show different image if available
    remoteBitmap?.let { bitmap ->
        Image(bitmap = bitmap.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier)
    } ?: Image(painter = painterResource(id = R.drawable.profile_placeholder),
        contentDescription = contentDescription,
        modifier = modifier)

}