package com.digwex.components.log.providers

import com.digwex.BuildConfig
import com.digwex.Config
import com.digwex.components.log.LogProvider
import java.io.File
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class FileLogProvider : LogProvider {
  private val mStreamLogProvider: StreamLogProvider
  private val mSchedeleExecutor: ScheduledExecutorService

  private val mTimeoutRunnable = Runnable {
    try {
      val fileList: Array<File> = dir.listFiles()

      for (file in fileList) {
        if (file.exists()) {
          val time: Calendar = Calendar.getInstance()
          time.add(Calendar.DAY_OF_YEAR, -7)
          val lastModified = Date(file.lastModified())
          if (lastModified.before(time.time)) {
            file.delete()
          }
        }
      }
    } catch (ignore: Exception) {
    }
  }

  override fun println(requestedLevel: Int, tag: String, message: String) {
    mStreamLogProvider.println(requestedLevel, tag, message)
  }

  override fun printException(throwable: Throwable?) {
    if (throwable != null) {
      mStreamLogProvider.printException(throwable)
    }
  }

  private val dir: File

  init {
    val path = Config.internalStorage + '/' + BuildConfig.LOGS_DIR
    dir = File(path)
    mStreamLogProvider = StreamLogProvider(path)
    mSchedeleExecutor = Executors.newScheduledThreadPool(1)
    mSchedeleExecutor.scheduleWithFixedDelay(mTimeoutRunnable, 0, 1,
      TimeUnit.DAYS)
  }
}