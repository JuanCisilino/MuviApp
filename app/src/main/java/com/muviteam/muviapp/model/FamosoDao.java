package com.muviteam.muviapp.model;

import android.util.Log;

import com.facebook.FacebookSdkNotInitializedException;
import com.muviteam.muviapp.utils.ResultListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamosoDao extends PeliculaRetrofitDao {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "1d3a01b7a462bea05038866f2fab52a1";
    public static String LANGUAGE = "es";


    public FamosoDao() {
        super(BASE_URL);
    }

    public void traerFamososDeLaPelicula(Integer movie_Id,final ResultListener<Credits> listenerDelControler ){
        Call<Credits> call = peliculasService.traerCredits(movie_Id, API_KEY);
        call.enqueue( new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                Credits containerCast = response.body();
                listenerDelControler.finish(containerCast);
            }
            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                Log.d("ewfwq","dwsfaw");
            }
        });
    }

    public void traerFamoso(Integer person_id, final ResultListener<Famoso> listenerDelControler){

        Call<Famoso> call = peliculasService.traerFamoso(person_id, API_KEY,LANGUAGE);

        call.enqueue(new Callback<Famoso>() {
            @Override
            public void onResponse(Call<Famoso> call, Response<Famoso> response) {
                Famoso famoso = response.body();
                listenerDelControler.finish(famoso);
            }

            @Override
            public void onFailure(Call<Famoso> call, Throwable t) {

            }
        });

    }
}
