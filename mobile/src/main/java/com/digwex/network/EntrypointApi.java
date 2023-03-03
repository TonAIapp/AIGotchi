package com.digwex.network;

import com.digwex.api.model.ActivateRequestJson;
import com.digwex.api.model.ActivateResponseJson;
import com.digwex.api.model.SyncJson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EntrypointApi {

    @Headers("Content-type: application/json")
    @POST("/api/v1/device/activate")
    Call<ActivateResponseJson> activate(@Body ActivateRequestJson request);

    @GET("/api/v1/device/{id}/package")
    Call<SyncJson> data(int id);

    @GET("/v3/entrypoint/time")
    Call<ResponseBody> time();
}
