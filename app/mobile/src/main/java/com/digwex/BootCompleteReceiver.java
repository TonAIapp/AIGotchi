package com.digwex;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.digwex.service.BackgroundService;

import static com.digwex.components.log.Log.DISABLE;
import static com.digwex.components.log.Log.println;

public class BootCompleteReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        startLongerServiceOnBoot(context);
        startActivityOnBoot(context);
    }

    private void startLongerServiceOnBoot(Context context){
        context.startService(new Intent(context, BackgroundService.class));
    }

    private void startActivityOnBoot(Context context){
//        SharedPreferences preferences = context
//                .getSharedPreferences(AppModule.SHARED_REFERENCES_NAME, 0);


        println(DISABLE, BootCompleteReceiver.class, "Start application");

        context.startActivity(new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

//        if(preferences.getBoolean(MainApplication.PREFERENCES_KEY_SCREEN_OFF_BY_APP, false)){
//            preferences.edit().remove(MainApplication.PREFERENCES_KEY_SCREEN_OFF_BY_APP).apply();
//            context.startActivity(new Intent(context, MainActivity.class)
//                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    //.putExtra(MainActivity.INTENT_KEY_UNLOCK_ON_RESUME, true));
//        }
    }
}
