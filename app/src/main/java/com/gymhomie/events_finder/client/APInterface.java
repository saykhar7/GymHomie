package com.gymhomie.events_finder.client;

import com.gymhomie.events_finder.EventsListApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APInterface {

    @GET("get-all-events")
    Call<EventsListApiResponse> geteventsList();

}
