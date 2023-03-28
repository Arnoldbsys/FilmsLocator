package ru.dombuketa.filmslocaror.viewmodel

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import java.net.URL



import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume


class DetailsFragmentViewModel : ViewModel() {

    suspend fun loadWallpaper(url: String) : Bitmap{
        return suspendCoroutine{
            val url = URL(url)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            it.resume(bitmap)
        }
    }

}