package com.captech.cosmonauts.cosmolte.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jvazquez on 8/5/16.
 */
public interface CosmolteCalendarItems {

    @GET("events")
    Call<List<CosmolteCalendarItem>> retrieveCalendarItems(@Query("apiKey") String apiKey);
}
