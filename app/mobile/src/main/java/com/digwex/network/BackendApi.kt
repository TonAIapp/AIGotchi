package com.digwex.network

import com.digwex.BuildConfig
import com.digwex.api.model.SyncJson
import com.digwex.api.model.TimeJson

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface BackendApi {

  @Headers("Content-type: application/json")
  @GET("${BuildConfig.API_PREFIX}/api/v1/device/{id}/package")
  fun data(@Header("Authorization") authorization: String, @Path("id") id: Int): Call<SyncJson>

  @GET("${BuildConfig.API_PREFIX}/api/v1/device/{id}/time")
  fun time(@Header("Authorization") authorization: String, @Path("id") id: Int): Call<TimeJson>

  @POST("${BuildConfig.API_PREFIX}/api/v1/device/{id}/screen")
  fun uploadScrennshot(@Path("id") id: Int, @Body body: RequestBody): Call<ResponseBody>

  @POST("${BuildConfig.API_PREFIX}/api/v1/device/{id}/log")
  fun uploadLog(@Path("id") id: Int, @Body body: RequestBody): Call<ResponseBody>
}
