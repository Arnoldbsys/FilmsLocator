package ru.dombuketa.filmslocaror.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.dombuketa.db_module.dto.Film
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.view.notify.NotifyConsts
import ru.dombuketa.filmslocaror.view.notify.NotifyHelper
import javax.inject.Inject

class ReminderBroadcast : BroadcastReceiver() {
    @Inject lateinit var notifyHelper: NotifyHelper
    init {
        App.instance.dagger.injectt(this)
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.getBundleExtra((NotifyConsts.FILM_BUNDLE_KEY))
        val film : Film = bundle?.get(NotifyConsts.FILM_KEY) as Film
        val filmrx : Observable<Film> = Single.just(bundle?.get(NotifyConsts.FILM_KEY) as Film).toObservable()
        notifyHelper.createNotification(context!!, filmrx)
    }
}