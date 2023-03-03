package com.digwex.utils;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.digwex.components.log.Log.printException;

public class Utils {

  @Nullable
  public static String getIntentStringExtra(@NonNull Intent intent, @NonNull String key) {
    if (intent.getExtras() != null) {
      String value = intent.getExtras().getString(key);

      if (value != null && !value.isEmpty()) {
        return value;
      }
    }

    return null;
  }

  public static void addLogInterceptorForDebugBuild(@NonNull OkHttpClient.Builder builder,
                                                    @NonNull HttpLoggingInterceptor.Level level) {
    //if(MainApplication.getInstance().isDebuggable()){
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(level);
    builder.addNetworkInterceptor(loggingInterceptor);
    //}
  }

  public static String md5(String str) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(str.getBytes());

    byte[] byteData = md.digest();

    StringBuilder builder = new StringBuilder();
    for (byte aByteData : byteData) {
      builder.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
    }
    return builder.toString();
  }

  @Nullable
  public static <T> T deepCopy(T object, Class<T> type) {
    try {
      Gson gson = new Gson();
      return gson.fromJson(gson.toJson(object, type), type);

    } catch (Exception e) {
      printException(e);

      return null;
    }
  }
}
