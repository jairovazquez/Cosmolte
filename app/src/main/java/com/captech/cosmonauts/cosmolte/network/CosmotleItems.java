package com.captech.cosmonauts.cosmolte.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jvazquez on 7/26/16.
 */
public interface CosmotleItems {

    @GET("{type}")
    Call<List<CosmotleItem>> retrieveItems(@Path("type") String itemsType, @Query("apiKey") String apiKey);

    @POST("{type}")
    Call<CosmotleItem> postCredit(@Path("type") String creditType, @Body CosmotleItem item);
}

