package com.digwex.network

import com.digwex.api.model.StepsJson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EntrypointApi {
  @Headers("Content-type: application/json") @POST("/set_steps")
  fun setSteps(
    @Body request: StepsJson
  ): Call<ResponseBody>

}