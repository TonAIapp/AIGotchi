package com.digwex

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.os.Environment
import android.os.SystemClock
import android.util.Log.*
import androidx.multidex.MultiDexApplication
import com.digwex.components.log.Log
import com.digwex.components.log.providers.FileLogProvider
import com.digwex.components.log.providers.LogCatProvider
import com.digwex.dagger.AppComponent
import com.digwex.dagger.AppModule
import com.digwex.dagger.DaggerAppComponent

import com.digwex.helpers.LocaleHelper
import com.digwex.service.WatchDogService
import net.danlew.android.joda.JodaTimeAndroid
import java.util.*
import javax.inject.Inject
import kotlin.system.exitProcess
import org.greenrobot.greendao.database.Database
import com.digwex.utils.FilesUtils


class MainApplication : MultiDexApplication() {
  private lateinit var mAlarmManager: AlarmManager
  private lateinit var mPendingIntent: PendingIntent
  private lateinit var mScreenOnReceiver: AppScreenOnOffBroadcastReceiver

  @Inject
  lateinit var mPreferences: SharedPreferences

  lateinit var appComponent: AppComponent
    private set

  val isDebuggable: Boolean
    get() = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

  init {
    instance = this
  }



  override fun onCreate() {
    super.onCreate()

    Log.println(WARN, MainActivity::class.java, "GOOOOOOO")

    JodaTimeAndroid.init(this)

    val context: Context = applicationContext
    val startActivity = Intent(context, MainActivity::class.java)
      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    //.putExtra(MainActivity.INTENT_KEY_UNLOCK_ON_RESUME, true);

    mPendingIntent = PendingIntent.getActivity(context, 0, startActivity,
      PendingIntent.FLAG_CANCEL_CURRENT)

    mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (!isDebuggable)
      Thread.setDefaultUncaughtExceptionHandler(AppUncaughtException())

    val appModule = AppModule(this)
    appComponent = DaggerAppComponent.builder().appModule(appModule).build()
    appComponent.inject(this)

    if (!requireDirs()) {
      Log.println(WARN, MainActivity::class.java, "Can not create the required directories")
    }

    Log.setLevel(VERBOSE)
    Log.addProvider(LogCatProvider())
    Log.addProvider(FileLogProvider())

//    val helper = DaoMaster.DevOpenHelper(this, DB_NAME)
//    val db:Database = helper.writableDb

    registerScreenOnReceiver()
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(LocaleHelper.onAttach(base, "ru"))
  }

  fun destroy() {
    unregisterReceiver(mScreenOnReceiver)
  }

  private fun registerScreenOnReceiver() {
    val filter = IntentFilter()
    filter.addAction(Intent.ACTION_SCREEN_ON)
    filter.addAction(Intent.ACTION_SCREEN_OFF)
    mScreenOnReceiver = AppScreenOnOffBroadcastReceiver()
    registerReceiver(mScreenOnReceiver, filter)
  }

  fun cancelPendingStart() {
    Log.println(INFO, MainApplication::class.java, "Cancel pending start app")
    val startActivity = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(this,
      START_PENDING_CODE, startActivity, PendingIntent.FLAG_CANCEL_CURRENT)

    mAlarmManager.cancel(pendingIntent)
  }

  fun pendingStart(millis: Int) {
    Log.println(INFO, MainApplication::class.java, "Pending start application timeout %d(ms)", millis)
    val startActivity = Intent(this, MainActivity::class.java)
      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        or Intent.FLAG_ACTIVITY_CLEAR_TASK
        or Intent.FLAG_ACTIVITY_NEW_TASK)
    val pendingIntent = PendingIntent.getActivity(this,
      START_PENDING_CODE, startActivity, PendingIntent.FLAG_CANCEL_CURRENT)

    mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
      SystemClock.elapsedRealtime() + millis, pendingIntent)
  }

  fun reboot(millis: Int) {
    Log.println(INFO, MainApplication::class.java, "Restart application timeout %d(ms)", millis)
    stopService(Intent(applicationContext, WatchDogService::class.java))
    val startActivity = Intent(this, MainActivity::class.java)
      .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
        or Intent.FLAG_ACTIVITY_CLEAR_TASK
        or Intent.FLAG_ACTIVITY_NEW_TASK)
    val pendingIntent = PendingIntent.getActivity(this,
      REBOOT_PENDING_CODE, startActivity, PendingIntent.FLAG_CANCEL_CURRENT)

    mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
      SystemClock.elapsedRealtime() + millis, pendingIntent)

    android.os.Process.killProcess(android.os.Process.myPid())
    exitProcess(0)
  }

  fun reboot() {
    reboot(5000)
  }

  fun requireDirs(): Boolean {
    var external: String? = Config.externalStorage

    if(external == "") {
      val file = Environment.getExternalStorageDirectory()
      if (file != null) {
        external = file.path + '/'.toString() + BuildConfig.APP_DIR
        Config.externalStorage = external
      }
    }

    external?:return false

    var internal: String? = Config.internalStorage

    if(internal == "") {
      val file = getExternalFilesDir(null)
      if (file != null) {
        internal = file.path
        Config.internalStorage = internal
      }
    }

    internal?: return false

    internal +='/'
    external +='/'

    return (FilesUtils.mkdirsIfNotExists(internal + BuildConfig.LOGS_DIR)
      && FilesUtils.mkdirsIfNotExists(external + BuildConfig.UPDATE_DIR)
      && FilesUtils.mkdirsIfNotExists(external + BuildConfig.CONTENT_DIR)
      && FilesUtils.mkdirsIfNotExists(external + BuildConfig.CONTENT_DIR + '/'.toString()
      + BuildConfig.VIDEOS_DIR)
      && FilesUtils.mkdirsIfNotExists(external + BuildConfig.CONTENT_DIR + '/'.toString()
      + BuildConfig.IMAGES_DIR)
      && FilesUtils.mkdirsIfNotExists(external + BuildConfig.CONTENT_DIR + '/'.toString()
      + BuildConfig.HTML_DIR))
  }

  @SuppressLint("ApplySharedPref")
  fun deactivate() {
    mPreferences.edit()
      .remove(Config.PREFERENCES_KEY_ACTIVATION_ACCESS_TOKEN)
      .remove(Config.PREFERENCES_KEY_ACTIVATION_PIN)
      .commit()
    reboot(100)
  }

  companion object {
    private const val DB_NAME = "digwex-db"
    private const val START_PENDING_CODE = 1
    private const val REBOOT_PENDING_CODE = 2

    const val PLATFORM = "android"


    lateinit var instance: MainApplication
  }
}
