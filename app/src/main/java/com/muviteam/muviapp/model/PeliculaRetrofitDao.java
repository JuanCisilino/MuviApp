package com.muviteam.muviapp.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PeliculaRetrofitDao {

    private Retrofit retrofit;
    protected Service peliculasService;

    public PeliculaRetrofitDao(String baseURL) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        peliculasService = retrofit.create(Service.class);
    }
}
