package ru.dombuketa.filmslocaror.view.notify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
//import android.database.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.core.Observable;
import ru.dombuketa.db_module.dto.Film;
import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.view.MainActivity;
import ru.dombuketa.net_module.entity.ApiConstants;

public class NotifyHelper_J {
    private static final String NOTIFY_TITLE = "Не забудьте посмотреть J";

    public static void createNotification(Context context, Observable<Film> film){
        film.subscribe(item ->{
            Log.i("notification","!!!" + item.getTitle());
            Intent inte = new Intent(context, MainActivity.class);
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
            Glide.with(context).asBitmap().load(ApiConstants.BASE_URL + "w500" + item.getPoster())
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
}
