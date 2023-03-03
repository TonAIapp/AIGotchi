package com.digwex.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.util.Log
import com.digwex.Config
import com.digwex.MainActivity
import com.digwex.MainApplication
import com.digwex.api.ServerApi
import com.digwex.listener.StepListener
import com.digwex.utils.StepDetector
import org.joda.time.DateTime
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BackgroundService : Service(), SensorEventListener, StepListener {
  private lateinit var mThread: HandlerThread

  @Inject lateinit var mApi: ServerApi

  private lateinit var schedeleExecutor: ScheduledExecutorService

  private var sensorManager: SensorManager? = null
  private var simpleStepDetector: StepDetector? = null


  override fun onCreate() {
    super.onCreate()
    mThread = HandlerThread(
      BackgroundService::class.java.name + "Thread",
      Process.THREAD_PRIORITY_FOREGROUND
    )
    mThread.start()

    schedeleExecutor = Executors.newScheduledThreadPool(1)

    MainApplication.instance.appComponent.inject(this)

    if (sensorManager == null || simpleStepDetector == null) {
      sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
      simpleStepDetector = StepDetector()
      simpleStepDetector!!.registerListener(this)
      startReceiving()
    }


    schedeleExecutor.scheduleWithFixedDelay({

        try {
          mApi.sendSteps()
        } catch (ignored: Exception) {
          //println(ERROR, UpdaterService.class, Log.getStackTraceString(ex));
        }
    }, 0, 10, TimeUnit.MINUTES)
  }

  override fun onDestroy() {
    super.onDestroy()
    mThread.quitSafely()
  }

  override fun onBind(intent: Intent): IBinder? {
    return null
  }

  private fun startReceiving() {

    com.digwex.components.log.Log.println(
      Log.INFO, BackgroundService::class.java, "startReceiving"
    )

    sensorManager!!.registerListener(
      this,
      sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
      SensorManager.SENSOR_DELAY_FASTEST
    )
  }

  override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
      simpleStepDetector!!.updateAccelerometer(
        event.timestamp, event.values[0], event.values[1], event.values[2]
      )
    }
  }

  override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
  override fun step(timeNs: Long) {

    var steps = Config.lastOffset
    var nextUnix = Config.nextUnix
    val currUnix = DateTime.now().millis

    if (currUnix > nextUnix) {
      nextUnix = DateTime.now().plusDays(1).millisOfDay.toLong()

      Config.nextUnix = nextUnix
      Config.lastOffset = 0

      return
    }
    steps++
    Config.lastOffset = steps

    com.digwex.components.log.Log.println(
      Log.INFO,
      BackgroundService::class.java,
      "Update steps: $steps"
    )

  }
}