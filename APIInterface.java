package com.example.niarch.sts_final;


import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by niarch on 6/4/18.
 */

public interface APIInterface {
    @FormUrlEncoded
    @POST("/inputPost")
    public void insertInput(
            @Field("numberOfDays") int numberOfDays,
            @Field("startDate") String startDate,
            @Field("startTime") String startTime,
            @Field("budget") int budget,
            @Field("religious") float reliRating,
            @Field("historical") float histRating,
            @Field("art") float artRating,
            @Field("market") float markReating,
            @Field("science") float scieRating,
            @Field("nature") float natuRating,
            Callback<Response> callback);


}
