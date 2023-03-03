package com.digwex.service;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;

import com.digwex.MainApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.util.Log.DEBUG;
import static com.digwex.components.log.Log.println;

@Singleton
public class MemoryWatcher {

  private final MainApplication mApp;
  private ActivityManager mActivityService;
  private Handler handler;

  private static final int CRITICAL = 73400320;
  private boolean isStart;

  @Inject
  MemoryWatcher() {
    mApp = MainApplication.instance;
    mApp.getAppComponent().inject(this);
    Context mContext = mApp.getApplicationContext();
    mActivityService = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
    if (mActivityService != null) {
      handler = new Handler();
      ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
      mActivityService.getMemoryInfo(memoryInfo);
    }
  }

  private Runnable runnable = new Runnable() {
    public void run() {
      Runtime rt = Runtime.getRuntime();
      long usedMemory = (rt.totalMemory() - rt.freeMemory()) / 1048576L;
      long availHeap = rt.maxMemory() / 1048576L - usedMemory;
      ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
      mActivityService.getMemoryInfo(memoryInfo);
      println(DEBUG, MemoryWatcher.class, "Memory info (MB): " +
          "availMem = %d | " +
          "availHeap = %d | " +
          "threshold = %d | " +
          "lowMemory: %s",
        memoryInfo.availMem / 1048576L,
        availHeap,
        memoryInfo.threshold / 1048576L,
        memoryInfo.lowMemory);
      if (memoryInfo.lowMemory || memoryInfo.availMem < memoryInfo.threshold + CRITICAL) {
        mApp.reboot();
        return;
      }
      int delay = 120000;
      if (memoryInfo.availMem < memoryInfo.threshold * 2.5) {
        Runtime.getRuntime().gc();
        delay = 60000;
      }
      if (memoryInfo.availMem < memoryInfo.threshold * 1.5) {
        Runtime.getRuntime().gc();
        delay = 30000;
      }
      if (memoryInfo.availMem < memoryInfo.threshold * 1.2) {
        Runtime.getRuntime().gc();
        delay = 15000;
      }
      handler.postDelayed(this, delay);
    }
  };


  public synchronized void start() {
    if (isStart) return;
    isStart = true;
    if (mActivityService != null) {
      runnable.run();
    }
  }

  public void stop() {
    if (handler != null) {
      handler.removeCallbacks(runnable);
    }
  }
}
