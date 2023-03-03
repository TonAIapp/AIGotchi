package com.digwex.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.digwex.MainActivity;
import com.digwex.MainApplication;

public class SystemUtils {
  public static void reboot() {
    Context context = MainApplication.instance.getApplicationContext();
    Intent startActivity = new Intent(MainApplication.instance.getApplicationContext(), MainActivity.class)
      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1,
      startActivity, PendingIntent.FLAG_CANCEL_CURRENT);

    AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (mgr != null) {
      mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, mPendingIntent);
      //ExitActivity.exitApplicationAnRemoveFromRecent(MainActivity.this);
      System.exit(0);
    }
  }

  public static void reboot(int delay) {
    Context context = MainApplication.instance.getApplicationContext();
    Intent startActivity = new Intent(MainApplication.instance.getApplicationContext(), MainActivity.class)
      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 1,
      startActivity, PendingIntent.FLAG_CANCEL_CURRENT);

    AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (mgr != null) {
      mgr.set(AlarmManager.RTC, System.currentTimeMillis() + delay, mPendingIntent);
      //ExitActivity.exitApplicationAnRemoveFromRecent(MainActivity.this);
      System.exit(0);
    }
  }
}
