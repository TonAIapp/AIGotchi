package com.digwex.api

import com.digwex.BuildConfig
import com.digwex.Config
import com.digwex.api.model.SyncJson
import com.digwex.api.model.TimeJson
import com.digwex.network.BackendApi
import com.digwex.service.BackgroundService
import com.digwex.utils.RetrofitUtils
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceApi @Inject
internal constructor(mGson: Gson) {
  private val mApiHost: String = BuildConfig.API_HOST
  private val mConverterFactory: GsonConverterFactory = GsonConverterFactory.create(mGson)

  init {
    try {
      //URL url = new URL(mBaseUrl);
      //timeUrl = "http://" + url.getHost();
    } catch (ignore: Exception) {
    }
  }

  fun time(): TimeJson? {
    val api = RetrofitUtils.request(BackendApi::class.java, mApiHost, mConverterFactory)
    try {
      val response: Response<TimeJson> = api
        .time("Bearer ${Config.accessToken}", Config.playerId).execute()
      if (response.isSuccessful) {
        val json: TimeJson = response.body()
        json.utc.millis
        return json
      }
    } catch (ignored: Exception) {
    }
    return null
  }

  fun data(): SyncJson? {
    val api = RetrofitUtils.request(BackendApi::class.java, mApiHost, mConverterFactory)
    val response: Response<SyncJson>
    try {
      response = api.data("Bearer ${Config.accessToken}", Config.playerId).execute()
      if (response.isSuccessful) {
        return response.body()
      }
    } catch (ignored: IOException) {

    }
    return null
  }

  @Throws(Exception::class)
  fun uploadScreenshot(data: ByteArray) {
    val headers = HashMap<String, String>()
    headers[BackgroundService.REST_HEADER_AUTHORIZATION] = "Bearer ${Config.accessToken}"


    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.addInterceptor(RetrofitUtils.HeaderInterceptor(headers))

    //addLogInterceptorForDebugBuild(clientBuilder, HttpLoggingInterceptor.Level.BODY);

    val retrofit = Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl(mApiHost)
      .build()

    val body = RequestBody.create(MediaType.parse("application/octet-stream"), data)

    retrofit.create(BackendApi::class.java).uploadScrennshot(Config.playerId, body).execute()
  }

  @Throws(Exception::class)
  fun uploadLog(data: ByteArray): Boolean {
    val headers = HashMap<String, String>()
    headers[BackgroundService.REST_HEADER_AUTHORIZATION] = "Bearer ${Config.accessToken}"

    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.addInterceptor(RetrofitUtils.HeaderInterceptor(headers))

    val retrofit = Retrofit.Builder()
      .client(clientBuilder.build())
      .baseUrl(mApiHost)
      .build()

    val body = RequestBody.create(MediaType.parse("application/octet-stream"), data)

    val response = retrofit.create(BackendApi::class.java)
      .uploadLog(Config.playerId, body).execute()
    //System.out.println("RESPONSE CODE: " + response.code());

    return response.code() == 204
  }


  companion object {
    private val MEDIA_TYPE_OCTET: MediaType = MediaType.parse("application/octet-stream")!!
    private val MEDIA_TYPE_JPG: MediaType = MediaType.parse("image/jpg")!!

    private const val SCREENSHOT_PARAMETER_NAME = "file"
    private const val SCREENSHOT_FILENAME = "screenshot.jpg"

    private const val LOG_PARAMETER_NAME = "file"
    private const val LOG_FILENAME = "log.zip"
  }
}
