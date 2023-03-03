package com.digwex.api

import com.digwex.BuildConfig
import com.digwex.Config
import com.digwex.api.model.StepsJson
import com.digwex.network.EntrypointApi
import com.digwex.utils.RetrofitUtils
import com.google.gson.Gson
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerApi @Inject
internal constructor(mGson: Gson) {
  private val mApiHost: String = BuildConfig.API_HOST
  private val mConverterFactory: GsonConverterFactory = GsonConverterFactory.create(mGson)


  fun sendSteps() {
    val h = RetrofitUtils.HeaderInterceptor(
      HashMap()
    )
    val api: EntrypointApi = RetrofitUtils.request(
      EntrypointApi::class.java, mApiHost, h, GsonConverterFactory.create()
    )

    try {
      api.setSteps(StepsJson(Config.lastOffset, Config.telegramId)).execute()
    }catch (ignored: IOException) {

    }
  }
}
