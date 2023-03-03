package com.digwex;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;

public class AppScreenOnOffBroadcastReceiver extends WakefulBroadcastReceiver {

    public static boolean power = true;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON:
                    //println(DEBUG, AppScreenOnOffBroadcastReceiver.class, "ON");
                    power = true;
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    //println(DEBUG, AppScreenOnOffBroadcastReceiver.class, "OFF");
                    power = false;
                    break;
            }
        }
    }
}
