package com.digwex.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.digwex.MainApplication;
import com.digwex.network.gson.DateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class AppModule {

  private static final String SHARED_REFERENCES_NAME = "preferences";

  private MainApplication mApplication;

  public AppModule(MainApplication application) {
    mApplication = application;
  }

  @Provides
  Context provideContext() {
    return mApplication;
  }

  @Provides
  MainApplication provideApplication() {
    return mApplication;
  }

  @Provides
  OkHttpClient provideWSOkHttpClient() {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    clientBuilder.followRedirects(false);
    clientBuilder.followSslRedirects(false);
    if (mApplication.isDebuggable()) {
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
      clientBuilder.addNetworkInterceptor(loggingInterceptor);
    }

    clientBuilder.pingInterval(30, TimeUnit.SECONDS);
    clientBuilder.readTimeout(1, TimeUnit.SECONDS);
    clientBuilder.writeTimeout(1, TimeUnit.SECONDS);
    clientBuilder.connectTimeout(15, TimeUnit.SECONDS);

    return clientBuilder.build();
  }

  @Provides
  @Singleton
  SharedPreferences provideSharedPreferences() {
    return mApplication.getSharedPreferences(SHARED_REFERENCES_NAME, 0);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
      .registerTypeAdapter(DateTime.class, new DateTimeConverter())
      .create();
  }
}
