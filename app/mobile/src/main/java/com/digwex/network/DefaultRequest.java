package com.digwex.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface DefaultRequest {
    @GET
    Call<ResponseBody> get(@Url String url);

    @POST
    Call<ResponseBody> post(@Url String url, RequestBody data);
}
