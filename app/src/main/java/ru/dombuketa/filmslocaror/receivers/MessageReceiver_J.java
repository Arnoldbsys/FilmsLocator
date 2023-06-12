package ru.dombuketa.filmslocaror.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

public class MessageReceiver_J extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        switch (intent.getAction()){
            case Intent.ACTION_BATTERY_LOW:
                Toast.makeText(context,"Батарея разряжена", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Intent.ACTION_POWER_CONNECTED:
                Toast.makeText(context,"Батарея заряжается", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                if (AppCompatDelegate.getDefaultNightMode () != AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(context, "Ночной режим", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(context, "Дневной режим", Toast.LENGTH_SHORT).show();
                }
        }

    }
}
