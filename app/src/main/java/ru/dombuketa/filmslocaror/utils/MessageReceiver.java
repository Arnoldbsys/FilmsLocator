package ru.dombuketa.filmslocaror.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        switch (intent.getAction()){
            case Intent.ACTION_BATTERY_LOW:
                Toast.makeText(context,"Батарея разряжена", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_POWER_CONNECTED:
                Toast.makeText(context,"Батарея заряжается", Toast.LENGTH_SHORT).show();
        }

    }
}
