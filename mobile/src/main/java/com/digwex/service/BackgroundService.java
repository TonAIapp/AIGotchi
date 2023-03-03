package com.digwex.service;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;

import androidx.annotation.Nullable;

import com.digwex.BuildConfig;
import com.digwex.Config;
import com.digwex.MainApplication;
import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public class BackgroundService extends Service {

  private HandlerThread mThread;
  public static final String REST_HEADER_AUTHORIZATION = "Authorization";

  @Inject
  protected Gson mGson;

  @Inject
  protected OkHttpClient mOkHttpClient;

  private String mApiHost = BuildConfig.API_HOST;
  private String mAccessToken;

  @Override
  public void onCreate() {
    super.onCreate();
    mThread = new HandlerThread(BackgroundService.class.getName() + "Thread",
      Process.THREAD_PRIORITY_FOREGROUND);
    mThread.start();
    //Handler mHandler = new Handler(mThread.getLooper());

    MainApplication.instance.getAppComponent().inject(this);
    mAccessToken = Config.INSTANCE.getAccessToken();

//    if (mClient == null && mApiHost != null)
      startReceiving();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mThread.quitSafely();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void startReceiving() {

  }
}
