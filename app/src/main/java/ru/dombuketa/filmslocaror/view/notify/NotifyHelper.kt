package ru.dombuketa.filmslocaror.view.notify

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.filmslocaror.R
import ru.dombuketa.filmslocaror.view.MainActivity
import ru.dombuketa.net_module.entity.ApiConstants

object NotifyHelper {
    const val NOTIFY_TITLE = "Не забудьте посмотреть"
    fun createNotification(context: Context, film: Observable<Film>){
        film.subscribe({
            println("!!!" + it.title)
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("film_id", it.id)
            intent.putExtra("film", it)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(context!!, NotifyConsts.CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_outline_cloud_queue_24)
                setContentTitle(NOTIFY_TITLE)
                setContentText(it.title)
                priority = NotificationCompat.PRIORITY_HIGH
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }
            val notificationManager = NotificationManagerCompat.from(context)
            Glide.with(context)
                .asBitmap()
                .load(ApiConstants.IMAGES_URL + "w500" + it.poster)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        notificationManager.notify(it.id, builder.build())
                    }
                    override fun onLoadCleared(placeholder: Drawable?) { }
                })
            notificationManager.notify(it.id, builder.build())
            },{
                println("!!!" + it.message)
            })
    }
}