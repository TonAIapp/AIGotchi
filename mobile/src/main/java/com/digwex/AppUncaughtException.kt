package com.digwex

import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.util.Log.ERROR
import com.digwex.components.log.Log

class AppUncaughtException internal constructor() : Thread.UncaughtExceptionHandler {

  private val mApp: MainApplication = MainApplication.instance

  override fun uncaughtException(thread: Thread, throwable: Throwable) {

    val messaage = StringBuilder(throwable.message?:"")
    try {
      messaage.append(android.util.Log.getStackTraceString(throwable))
    }catch (ignore: Exception) {
    }

    Log.println(ERROR, AppUncaughtException::class.java, "Reboot app after exception:\n\t%s",
      messaage)


//    try {
//
//      val builder = EventBuilder()
//        .withLevel(Event.Level.FATAL)
//        .withFingerprint("{{ " + "default }}", BuildConfig.VERSION_NAME)
//        .withLogger(if (mApp.isDebuggable) "develop" else "client")
//        .withSentryInterface(ExceptionInterface(throwable), true)
//
//      if (Config.isDeviceInitialized()) {
//        builder.withExtra("backend_url", Config.entrypoint)
//        builder.withExtra("device_id", Config.playerId)
//        builder.withExtra("timezone", mApp.deviceTimezone)
//      }
//
//      Sentry.capture(builder)
//    } catch (ex: Exception) {
//      Log.println(Log.WARN, AppUncaughtException::class.java, "Sentry exception:\n\t"
//        + throwable.message)
//    }

    val handler = HandlerThread(AppUncaughtException::javaClass.name + "Thread",
      Process.THREAD_PRIORITY_FOREGROUND)
    handler.start()
    Handler(handler.looper).postDelayed({
      mApp.reboot(1000)
    }, 5000)

    Thread.sleep(5000)
  }
}
