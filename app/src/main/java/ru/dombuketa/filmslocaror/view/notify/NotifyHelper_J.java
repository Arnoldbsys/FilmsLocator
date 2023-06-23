package ru.dombuketa.filmslocaror.view.notify;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
//import android.database.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.Calendar;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.core.Observable;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.db_module.dto.Notification;
import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.receivers.ReminderBroadcast_J;
import ru.dombuketa.filmslocaror.view.MainActivity;
import ru.dombuketa.filmslocaror.view.MainActivity_J;
import ru.dombuketa.net_module.entity.ApiConstants;

public class NotifyHelper_J {
    private static final String NOTIFY_TITLE = "Не забудьте посмотреть J";

    public static void createNotification(Context context, Observable<Film> film){
        film.subscribe(item ->{
            Log.i("notification","!!!" + item.getTitle());
            Intent inte = new Intent(context, MainActivity_J.class);
            inte.putExtra("film_id", item.getId());
            inte.putExtra("film", item);
            PendingIntent pendingInte = PendingIntent.getActivity(context, 0, inte, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builde = new NotificationCompat.Builder(context, NotifyConsts.CHANNEL_ID);
            builde.setSmallIcon((R.drawable.ic_outline_cloud_queue_24));
            builde.setContentTitle(NOTIFY_TITLE);
            builde.setContentText(item.getTitle());
            builde.setPriority(NotificationCompat.PRIORITY_HIGH);
            builde.setContentIntent(pendingInte);
            builde.setAutoCancel(true);

            NotificationManagerCompat notifManager = NotificationManagerCompat.from(context);
            Glide.with(context).asBitmap().load(ApiConstants.IMAGES_URL + "w500" + item.getPoster())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        builde.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource));
                        notifManager.notify(item.getId(), builde.build());
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });
            notifManager.notify(item.getId(), builde.build());
        }, error ->{
            Log.i("notification","!!!" + error.getMessage());

        });
    }

    public static void notificationSet_J(Context context, Film film){
        Calendar calendar = Calendar.getInstance();
        Integer curY = calendar.get(Calendar.YEAR);
        Integer curM = calendar.get(Calendar.MONTH);
        Integer curD = calendar.get(Calendar.DAY_OF_MONTH);
        Integer curH = calendar.get(Calendar.HOUR);
        Integer curm = calendar.get(Calendar.MINUTE);
        new DatePickerDialog(context,
            (view, year, month, dayOfMonth) -> {
                TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar pickedDataTime = Calendar.getInstance();
                        pickedDataTime.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                        Long dataTimeInMillis = pickedDataTime.getTimeInMillis();
                        App_J.getInstance().daggerj.getInteractor_j().putNodificationtoDB(
                            new Notification(0, film.getId(), film.getTitle(), year, month, dayOfMonth, hourOfDay, minute, film.getPoster(), true));
                        createWatchLaterEvent_J(context, dataTimeInMillis, film);
                    }
                };
                new TimePickerDialog(context, timePickerListener, curH, curm, true).show();
            }, curY, curM, curD).show();
    }


    private static void createWatchLaterEvent_J(Context context, Long dataTimeMillis, Film film){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(film.getTitle(), null, context, ReminderBroadcast_J.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(NotifyConsts.FILM_KEY, film);
        intent.putExtra(NotifyConsts.FILM_BUNDLE_KEY, bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dataTimeMillis, pendingIntent);
    }
}
