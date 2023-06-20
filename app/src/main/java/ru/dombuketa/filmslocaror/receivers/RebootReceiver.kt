package ru.dombuketa.filmslocaror.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.dombuketa.filmslocaror.App

class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        } else {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                Log.i("bootReceiver", "Устройство было перезагружено")
                val dagger = App.instance.dagger
                dagger.getInteractor().getNotifications().subscribe({
                    it.forEach {
                        if (context != null) {
                            dagger.getNotifyHelper().createNotification(context,
                                dagger.getInteractor().getFilmFromAPI(it.filmId))
                        }
                    }
                },{
                    println("!!! bootReceiver/ Ошибка " + it.message)
                })
            }
        }
    }
}