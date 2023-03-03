package com.digwex.service

import android.annotation.SuppressLint
import android.util.Log.DEBUG
import com.digwex.Config
import com.digwex.CorrectTime
import com.digwex.api.DeviceApi
import com.digwex.api.model.TimeJson
import com.digwex.components.log.Log.println
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeService @Inject
internal constructor(private val mDeviceApi: DeviceApi) {

  private val mSchedeleExecutor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

  private var isStart: Boolean = false

  private val mTimeoutRunnable = Runnable {
    val date: TimeJson = mDeviceApi.time() ?: return@Runnable
    date.utc = date.utc.withZone(DateTimeZone.UTC)
    val unix: Long = date.utc.millis
    val offset: Int = date.offset * 60 * 1000
    Config.lastUnix = unix
    Config.lastOffset = offset

    setTime(unix, offset)
  }

  init {
    val lastUnix: Long = Config.lastUnix
    val curr: Long = DateTime(DateTimeZone.UTC).millis
    setTime(if (curr < lastUnix) lastUnix else curr, Config.lastOffset)
  }

  private fun setTime(serverUnix: Long, offset_: Int) {
    val currUnix: Long = DateTime(DateTimeZone.UTC).millis
    delta = serverUnix - currUnix
    offset = offset_

    println(DEBUG, TimeService::class.java, "%s | %s",
      CorrectTime.utcNow.toString(dateFormat),
      CorrectTime.now.toString(dateFormat))
  }

  fun prevTime() {
//        val date: DateTime? = mDeviceApi.time()
//        if (date == null) {
//            val serverUnix: Long = mApplication.lastUnix
//            setTime(serverUnix)
//        } else {
//            setTime(date.millis)
//        }
  }

  fun start() {
    if (isStart) return
    println(DEBUG, TimeService::class.java, "Start time service")
    isStart = true
    mSchedeleExecutor.scheduleWithFixedDelay(mTimeoutRunnable, 0, 30,
      TimeUnit.SECONDS)
  }

  companion object {

    var offset: Int = 0
    var delta: Long = 0

    @SuppressLint("SimpleDateFormat")

    private val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS")
  }
}
