package com.digwex

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration

import android.os.Bundle
import android.util.Log

import android.view.View

import android.widget.RelativeLayout
import com.digwex.service.BackgroundService

import com.digwex.service.WatchDogService
import java.util.*
import javax.inject.Inject

@Suppress("UNUSED_PARAMETER")
class AppActivity : ImmersiveAppCompatActivity(){


  private lateinit var mApp: MainApplication
  private lateinit var mSplash: RelativeLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_app)

    mApp = MainApplication.instance
    mApp.appComponent.inject(this)
    mSplash = findViewById(R.id.splash)


//    val ids = IntArray(2)
//    ids[0] = resources.getIdentifier(
//      "splash_land_${BuildConfig.SPLASH}",
//      "drawable", packageName)
//    if (ids[0] != 0) {
//      ids[1] = resources.getIdentifier(
//        "splash_${BuildConfig.SPLASH}",
//        "drawable", packageName)
//      if (ids[1] != 0) {
//        mSplash.getChildAt(0).visibility = GONE
//        val draw: Array<ImageView> = arrayOf(ImageView(this), ImageView(this))
//        draw[0].setImageResource(ids[0])
//        draw[0].visibility = GONE
//        draw[1].setImageResource(ids[1])
//        draw[1].visibility = GONE
//        mSplash.addView(draw[0])
//        mSplash.addView(draw[1])
//        mDrawSplash = draw
//      }
//    }
//    startService(Intent(mApp.applicationContext, WatchDogService::class.java))

    com.digwex.components.log.Log.println(
      Log.INFO,
      AppActivity::class.java,
      "Itialized: ${Config.telegramId}"
    )

    if(Config.telegramId!=-1L) {
      startService(
        Intent(
          mApp.applicationContext,
          BackgroundService::class.java
        )
      )
    }
  }

  override fun onStart() {
    super.onStart()
  }

  override fun onResume() {
    super.onResume()
  }

  override fun onPause() {
    super.onPause()

    if (mApp.isDebuggable) return

    val activityManager =
      applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    activityManager.moveTaskToFront(taskId, 0)
  }

  override fun onStop() {
    super.onStop()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
  }

  override fun onBackPressed() {
    // do nothing
  }


//  private fun updateSplash(w: Int, h: Int) {
//    val draw: Array<ImageView> = mDrawSplash ?: return
//    if (w > h) {
//      draw[0].visibility = VISIBLE
//      draw[1].visibility = GONE
//    } else {
//      draw[1].visibility = VISIBLE
//      draw[0].visibility = GONE
//    }
//  }
//
//  private fun setDisplayParam() {
//    var width = Config.width
//    var height = Config.height
//
//    if (width == height) {
//      height = -1
//      width = height
//    }
//
//    changeResolution(Config.x, Config.y, width, height)
//  }


  fun onClickCloseApp(v: View) {
    stopService(Intent(mApp.applicationContext, WatchDogService::class.java))
    AndroidUtils.exitApplicationAnRemoveFromRecent(this)
  }

  fun onClickReactivateApp(v: View) {
    stopService(Intent(mApp.applicationContext, WatchDogService::class.java))
    mApp.deactivate()
    finish()
  }


  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)

    if (!hasFocus) {
      val closeDialog = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
      sendBroadcast(closeDialog)
    }
  }


  //////// CAMER
}
