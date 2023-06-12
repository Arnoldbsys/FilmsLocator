package ru.dombuketa.filmslocaror.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.filmslocaror.view.notify.NotifyConsts;
import ru.dombuketa.filmslocaror.view.notify.NotifyHelper_J;

public class ReminderBroadcast_J extends BroadcastReceiver {
    public ReminderBroadcast_J() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(NotifyConsts.FILM_BUNDLE_KEY);
        Film film = (Film)bundle.get(NotifyConsts.FILM_KEY);
        Observable<Film> filmrx = Single.just((Film)bundle.get(NotifyConsts.FILM_KEY)).toObservable();

        NotifyHelper_J.createNotification(context, filmrx);
    }
}
