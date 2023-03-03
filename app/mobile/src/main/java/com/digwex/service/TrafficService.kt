package com.digwex.service

import android.net.TrafficStats
import android.os.Process

import com.digwex.Config

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import javax.inject.Singleton

import android.util.Log.DEBUG
import com.digwex.components.log.Log.println

@Singleton
class TrafficService @Inject
internal constructor() {
  private val schedeleExecutor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
  private val mUid: Int = Process.myUid()
  private var isStart: Boolean = false

  private val mTimeoutRunnable = {
    try {
      updateStats()
    } catch (e: Exception) {
      //e.printStackTrace();
    }
  }

  init {
    val lastRxBytes = Config.lastRxBytes
    val currRxBytes = TrafficStats.getUidRxBytes(mUid)
    val lastTxBytes = Config.lastTxBytes
    val currTxBytes = TrafficStats.getUidTxBytes(mUid)

    if (lastRxBytes > currRxBytes || lastTxBytes > currTxBytes) {
      Config.nextClearTime = System.currentTimeMillis() + DAY_MILlIS
      Config.rxBytesForDay = currRxBytes
      Config.txBytesForDay = currTxBytes
      Config.lastRxBytes = 0
      Config.lastTxBytes = 0
    }
  }

  @Synchronized
  fun start() {
    if (isStart) return
    isStart = true
    schedeleExecutor.scheduleWithFixedDelay(mTimeoutRunnable, 0, 5, TimeUnit.MINUTES)
  }

  private fun updateStats() {
    val lastRxBytes = Config.lastRxBytes
    val currRxBytes = TrafficStats.getUidRxBytes(mUid)
    val lastTxBytes = Config.lastTxBytes
    val currTxBytes = TrafficStats.getUidTxBytes(mUid)
    var rxBytesForDay = Config.rxBytesForDay - currRxBytes + lastRxBytes
    var txBytesForDay = Config.txBytesForDay - currTxBytes + lastTxBytes

    Config.lastRxBytes = currRxBytes
    Config.lastTxBytes = currTxBytes

    val currMillis = System.currentTimeMillis()
    var nextMillis = Config.nextClearTime
    if (nextMillis < currMillis) {
      nextMillis = currMillis + DAY_MILlIS
      Config.nextClearTime = nextMillis
      printStatsForDay(rxBytesForDay, txBytesForDay)
      rxBytesForDay = 0
      txBytesForDay = 0
    } else {
      printStats(rxBytesForDay, txBytesForDay)
    }
    Config.rxBytesForDay = rxBytesForDay
    Config.txBytesForDay = txBytesForDay
  }

  private fun printStats(rxBytes: Long, txBytes: Long) {
    println(DEBUG, TrafficStats::class.java, "Trafic info: Rx/Tx - %d/%d", rxBytes, txBytes)
  }

  private fun printStatsForDay(rxBytes: Long, txBytes: Long) {
    println(DEBUG, TrafficStats::class.java, "Trafic info for day: Rx/TX - %d/%d", rxBytes,
      txBytes)
  }

  companion object {

    private val DAY_MILlIS = TimeUnit.DAYS.toMillis(1)
  }
}
