package ru.dombuketa.filmslocaror.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import ru.dombuketa.filmslocaror.utils.SingleLiveEvent
import java.io.IOException
import java.net.URL
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume


class DetailsFragmentViewModel : ViewModel() {
    val error = SingleLiveEvent<String>() //42*

    suspend fun loadWallpaper(url: String) : Bitmap?{
        return suspendCoroutine{
            val url = URL(url)
            var bitmap : Bitmap? = null
            try {
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            } catch (e : IOException) { //42*
                error.postValue(ERROR_CONNECTION) //42*
            } //42*
            it.resume(bitmap)
        }
    }
    //42*
    fun clearError() {
        error.postValue("")
    }



    companion object{
        const val ERROR_CONNECTION = "Ошибка соединения с сервером."
    }
}