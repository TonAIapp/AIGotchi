package com.digwex.utils;

import androidx.annotation.NonNull;

import com.digwex.network.CookieContainer;
import com.digwex.network.DefaultRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static com.digwex.utils.Utils.addLogInterceptorForDebugBuild;

public class RetrofitUtils {

  public static DefaultRequest request() {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);
    //addLogInterceptorForDebugBuild(clientBuilder, HttpLoggingInterceptor.Level.BODY);

    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl("http://localhos")
      .build();

    return retrofit.create(DefaultRequest.class);
  }

  public static DefaultRequest request(@NonNull HeaderInterceptor headerInterceptor,
                                       @NonNull CookieContainer cookieContainer) {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);
    clientBuilder.addInterceptor(headerInterceptor);
    clientBuilder.cookieJar(cookieContainer);
    //addLogInterceptorForDebugBuild(clientBuilder, HttpLoggingInterceptor.Level.BODY);

    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl("http://localhos")
      .build();

    return retrofit.create(DefaultRequest.class);
  }

  public static DefaultRequest request(@NonNull CookieContainer cookieContainer) {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);
    //addLogInterceptorForDebugBuild(clientBuilder, HttpLoggingInterceptor.Level.BODY);

    clientBuilder.cookieJar(cookieContainer);

    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl("http://localhos")
      .build();

    return retrofit.create(DefaultRequest.class);
  }

  public static <T> T request(final Class<T> type, @NonNull String baseUrl) {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);
    //addLogInterceptorForDebugBuild(clientBuilder, HttpLoggingInterceptor.Level.BODY);
    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl(baseUrl)
      .build();

    return retrofit.create(type);
  }

  public static <T> T request(final Class<T> type, @NonNull String baseUrl,
                              @NonNull HeaderInterceptor headerInterceptor) {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);
    clientBuilder.addInterceptor(headerInterceptor);

    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl(baseUrl)
      .build();

    return retrofit.create(type);
  }

  public static <T> T request(final Class<T> type, @NonNull String baseUrl,
                              @NonNull HeaderInterceptor headerInterceptor,
                              @NonNull Converter.Factory converterFactory) {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);

//    addLogInterceptorForDebugBuild(clientBuilder,
//      HttpLoggingInterceptor.Level.BODY);

    addLogInterceptorForDebugBuild(clientBuilder, HttpLoggingInterceptor.Level.HEADERS);

    clientBuilder.addInterceptor(headerInterceptor);

    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl(baseUrl)
      .addConverterFactory(converterFactory)
      .build();

    return retrofit.create(type);
  }

  public static <T> T request(final Class<T> type,
                              @NonNull String baseUrl,
                              @NonNull Converter.Factory converterFactory) {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);
    clientBuilder.followSslRedirects(false);

    addLogInterceptorForDebugBuild(clientBuilder, HttpLoggingInterceptor.Level.HEADERS);

    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl(baseUrl)
      .addConverterFactory(converterFactory)
      .build();

    return retrofit.create(type);
  }

  public static <T> T request(final Class<T> type, @NonNull String baseUrl,
                              @NonNull CookieContainer cookieContainer,
                              @NonNull Converter.Factory converterFactory) {

    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(15, TimeUnit.SECONDS)
      .writeTimeout(15, TimeUnit.SECONDS);
    clientBuilder.followRedirects(false);
    clientBuilder.followSslRedirects(false);
    clientBuilder.cookieJar(cookieContainer);

    Retrofit retrofit = new Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl(baseUrl)
      .addConverterFactory(converterFactory)
      .build();

    return retrofit.create(type);
  }

  public static class HeaderInterceptor implements Interceptor {

    private final Map<String, String> mHeaders;

    public HeaderInterceptor(final Map<String, String> headers) {
      mHeaders = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      Request.Builder builder = request.newBuilder();

      String cookies = mHeaders.remove("Cookie");

      if (cookies != null)
        builder.addHeader("Cookie", URLDecoder.decode(cookies, "UTF-8"));

      Set<Map.Entry<String, String>> set = mHeaders.entrySet();

      for (Map.Entry<String, String> entry : set) {
        builder.addHeader(entry.getKey(), entry.getValue());
      }

      return chain.proceed(builder.build());
    }
  }
}
