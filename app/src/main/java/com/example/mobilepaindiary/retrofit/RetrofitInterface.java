package com.example.mobilepaindiary.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//interface for http operations(API) and return type
public interface RetrofitInterface {
    @GET("weather?&units=metric")
    Call<SearchResponse> getWeather(@Query("q") String q,@Query("APPID") String key);
}
