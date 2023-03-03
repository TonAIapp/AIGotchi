package com.digwex

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log.INFO
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.digwex.components.log.Log.println
import com.digwex.service.TrafficService
import javax.inject.Inject

class MainActivity : ImmersiveAppCompatActivity() {


//  @Inject
//  lateinit var mTrafficService: TrafficService

  //  private val mLock = Any()
//
//  private var mWakeLock: PowerManager.WakeLock? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    println(INFO, MainActivity::class.java, "Start v" + BuildConfig.VERSION_NAME)

    MainApplication.instance.appComponent.inject(this)

    val action: String? = intent?.action
    val data: Uri? = intent?.data


    println(INFO, MainActivity::class.java, "action: $action | uri: ${data
      .toString()}" )

//    mAllerts = arrayOf(
//      AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert),
//      AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert),
//      AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert))
//
//    if (savedInstanceState == null) {
//      println(INFO, MainActivity::class.java, "Start v" + BuildConfig.VERSION_NAME)
//    }
//
//    for (i in mAllerts.indices) {
//      mAllerts[i].setTitle(getString(R.string.grant_access))
//      mAllerts[i].setCancelable(false)
//    }
//
//    mAllerts[1].setMessage(getString(R.string.permission_never))
//    mAllerts[1].setNegativeButton(getString(R.string.exit)) { _, _ ->
//      finish()
//      AndroidUtils.exitApplicationAnRemoveFromRecent(this)
//    }
//
//    mAllerts[2].setTitle(getString(R.string.start_error_title))
//    mAllerts[2].setMessage(getString(R.string.start_error))
//    mAllerts[2].setNegativeButton(getString(R.string.exit)) { _, _ ->
//      finish()
//      AndroidUtils.exitApplicationAnRemoveFromRecent(this)
//    }

    start()
  }

  private fun requestPermissions() {
    if (checkStoragePermission()) {
      start()
    } else {
      val intent = Intent(this, PermissionActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
      startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE)
    }
  }

  override fun onStart() {
    //println(DEBUG, MainActivity.class, "OnStart");
    super.onStart()
//    unlockScreen()
  }

  override fun onBackPressed() {

    // do nothing
  }

  override fun onResume() {
    super.onResume()
    //println(DEBUG, MainActivity.class, "Resume");
//    synchronized(mLock) {
//      if (mWakeLock == null) {
//        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
//        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
//          javaClass.name)
//      }
//      mWakeLock!!.acquire(10 * 60 * 1000L /*10 minutes*/)
//    }
  }

  //    @Override
  //    public boolean dispatchKeyEvent(KeyEvent event) {
  //        if (BLOCKED_KEYS.contains(event.getKeyCode())) {
  //            return true;
  //        } else {
  //            return super.dispatchKeyEvent(event);
  //        }
  //    }

  override fun onDestroy() {
    super.onDestroy()
    //println(DEBUG, MainActivity.class, "Destroy");

//    synchronized(mLock) {
//      if (mWakeLock != null && mWakeLock!!.isHeld) {
//        try {
//          mWakeLock!!.release()
//        } catch (ignore: Exception) {
//        }
//
//      }
//    }
  }

//  override fun attachBaseContext(newBase: Context) {
//    super.attachBaseContext(LocaleHelper.onAttach(newBase, "us"))
//  }

  private fun unlockScreen() {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
      WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
      WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
      WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
  }

  private fun start() {
//    mTrafficService.start()

    val intent = Intent(this, AppActivity::class.java)

    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    startActivity(intent)
    finish()
  }

//  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    super.onActivityResult(requestCode, resultCode, data)
//    when (requestCode) {
//      REQUEST_EXTERNAL_STORAGE ->
//        // WTF not called onRequestPermissionsResult
//        if (resultCode == Activity.RESULT_OK || checkStoragePermission()) {
//          start()
//        } else {
//          mAllerts[1].show()
//        }
//    }
//  }

  private fun checkStoragePermission(): Boolean {
    return ContextCompat.checkSelfPermission(this,
      Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
      ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
  }

  companion object {
    private const val REQUEST_EXTERNAL_STORAGE = 3
  }
}