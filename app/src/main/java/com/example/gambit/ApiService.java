package com.example.gambit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/category/39")
    Call<ArrayList<Dishes>> getDishes(
        @Query("page") String page);
}
