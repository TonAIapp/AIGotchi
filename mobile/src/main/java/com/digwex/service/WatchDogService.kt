package com.digwex.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log.INFO
import com.digwex.AppActivity
import com.digwex.BuildConfig
import com.digwex.Config
import com.digwex.MainApplication
import com.digwex.components.log.Log

class WatchDogService : Service() {

  // linear layout will use to detect touch event
  private var cnt = 0
  private var mActivityManager: ActivityManager? = null

  private lateinit var mThread: Thread

  private var prev: ActivityManager.RunningAppProcessInfo? = null

  private fun foregroundApps(name: String): ActivityManager.RunningAppProcessInfo? {
    if (mActivityManager == null)
      mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    val manager: ActivityManager = mActivityManager ?: return null
    val l: List<ActivityManager.RunningAppProcessInfo> = manager.runningAppProcesses ?: return null

    for (aL in l) {
      if (aL.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && aL.processName == name) {
        return aL
      }
    }
    return null
  }

  override fun onCreate() {
    super.onCreate()
    mThread = Thread(Runnable { startMonitor()  })
    mThread.start()
  }

  private fun startMonitor() {
    while (!Thread.interrupted()) {
      val k: Int = Math.round(Config.expanding * 0.2).toInt()
      val tmp: ActivityManager.RunningAppProcessInfo? = foregroundApps(BuildConfig.APPLICATION_ID)
      if (prev == tmp && prev == null) {
        cnt++
        if (cnt == k) {
          reopen(100)
        }
      } else {
        cnt = 0
      }
      prev = tmp
      try {
        Thread.sleep(5000)
      } catch (ignored: InterruptedException) {
      }
    }
  }

  private fun reopen(millis: Int) {
    Log.println(INFO, MainApplication::class.java, "Reopen application timeout %d(ms)", millis)

    val appActivity = Intent(this, AppActivity::class.java)
      .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TOP)

    try {
      startActivity(appActivity)
    } catch (ex: Exception) {
      appActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      startActivity(appActivity)
    }
  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return Service.START_STICKY
  }
}