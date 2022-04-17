package com.example.gambit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
        private static final String ROOT_URL ="https://api.gambit-app.ru";
        private static Retrofit retrofit;
        public static Retrofit getClient(){
            if(retrofit==null){
                retrofit = new Retrofit.Builder()
                        .baseUrl(ROOT_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;

        }
    }

