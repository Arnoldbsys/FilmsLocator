package ru.dombuketa.filmslocaror.view.notify

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.reactivex.rxjava3.core.Observable
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.db_module.dto.Notification
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.R
import ru.dombuketa.filmslocaror.domain.Interactor
import ru.dombuketa.filmslocaror.receivers.ReminderBroadcast
import ru.dombuketa.filmslocaror.view.MainActivity
import ru.dombuketa.net_module.entity.ApiConstants
import java.util.*

class NotifyHelper {
     val interactor: Interactor = App.instance.dagger.getInteractor()

        private fun createWatchLaterEvent(context: Context, dateTimeInMillis: Long, film: Film){
            val alarmManager = context.getSystemService((Context.ALARM_SERVICE)) as AlarmManager
            val intent = Intent(film.title, null, context, ReminderBroadcast::class.java)
            val  bundle = Bundle()
            bundle.putParcelable(NotifyConsts.FILM_KEY, film)
            intent.putExtra(NotifyConsts.FILM_BUNDLE_KEY, bundle)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTimeInMillis, pendingIntent)
        }

        fun notificatioSet(context: Context, film: Film){
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            DatePickerDialog(context,
                {_, dY, dM, doM ->
                    val timeSetListener = TimePickerDialog.OnTimeSetListener{
                            _, hoD, pickerMinute ->
                        val  pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(dY, dM, doM, hoD, pickerMinute, 0)
                        val dateTimeInMillis = pickedDateTime.timeInMillis
                        interactor.putNodificationtoDB(Notification(0, film.id, film.title, dY, dM, doM, hoD, pickerMinute, film.poster))
                        createWatchLaterEvent(context, dateTimeInMillis, film)
                    }
                    TimePickerDialog(context, timeSetListener, currentHour, currentMinute, true).show()
                },
                currentYear, currentMonth, currentDay).show()
        }

        fun createNotification(context: Context, film: Observable<Film>){
            film.subscribe({
                println("!!!" + it.title)
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("film_id", it.id)
                intent.putExtra("film", it)
                //TODO(Обновление в базе)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                val builder = NotificationCompat.Builder(context!!, NotifyConsts.CHANNEL_ID).apply {
                    setSmallIcon(R.drawable.ic_outline_cloud_queue_24)
                    setContentTitle(Companion.NOTIFY_TITLE)
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
                println("E!!!" + it.message)
            })
        }

    companion object {
        const val NOTIFY_TITLE = "Не забудьте посмотреть"
    }
}