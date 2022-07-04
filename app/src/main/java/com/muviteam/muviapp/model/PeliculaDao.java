package com.muviteam.muviapp.model;

import android.util.Log;

import com.muviteam.muviapp.utils.ResultListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PeliculaDao extends PeliculaRetrofitDao {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "1d3a01b7a462bea05038866f2fab52a1";
    public static String LANGUAGE = "es";

    public PeliculaDao() {
        super(BASE_URL);
    }

    public void traerPeliculas(final ResultListener<List<Pelicula>> listenerDelControler){
        Call<ContainerPelicula> call = peliculasService.traerPelicula(API_KEY,LANGUAGE);
        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
                ContainerPelicula containerPelicula = response.body();
                listenerDelControler.finish(containerPelicula.getResults());
            }
            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {
                Log.d("ewfwq","dwsfaw");
            }
        });
    }

    public void traerUpcoming(final ResultListener<List<Pelicula>> listenerDelControler){
        Call<ContainerPelicula> call = peliculasService.traerUpcoming(API_KEY,LANGUAGE);
        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
                ContainerPelicula containerPelicula = response.body();
                listenerDelControler.finish(containerPelicula.getResults());
            }
            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {
                Log.d("ewfwq","dwsfaw");
            }
        });
    }

    public  void traerCredits(final ResultListener<Credits> resultListenerController,Integer movieId){
        Call<Credits> creditsCall = peliculasService.traerCredits(movieId,API_KEY);
        creditsCall.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                Credits resultsCredit = response.body();
                resultListenerController.finish(resultsCredit);
            }
            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void traerListaDeViewPager(final ResultListener<List<Pelicula>> listenerDelControler){
        Call<ContainerPelicula> call = peliculasService.traerPeliculaViewPager(API_KEY,LANGUAGE);
        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
                ContainerPelicula containerPelicula = response.body();
                listenerDelControler.finish(containerPelicula.getResults());
            }
            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {
                Log.d("ewfwq","dwsfaw");
            }
        });
    }

    public void traerPopulares(final ResultListener<List<Pelicula>> listenerDelControler){
        Call<ContainerPelicula> call = peliculasService.traerPeliculasPopulares(API_KEY,LANGUAGE);
        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
                ContainerPelicula containerPelicula = response.body();
                listenerDelControler.finish(containerPelicula.getResults());
            }
            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {
                Log.d("ewfwq","dwsfaw");
            }
        });
    }

    public void traerTrailer(final ResultListener<Videos> resultListenerController, Integer movieId){
        Call<Videos> videosCall = peliculasService.traerVideos(movieId, API_KEY);
        videosCall.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                Videos resultsCredit = response.body();
                resultListenerController.finish(resultsCredit);
            }
            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void traerPeliculaDeFamoso(final ResultListener<List<Pelicula>> listResultListener, Integer famosoId) {
        Call<ContainerPelicula> call = peliculasService.traerPeliculasDeFamoso(API_KEY,LANGUAGE,famosoId);
        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
                ContainerPelicula containerPelicula = response.body();
                listResultListener.finish(containerPelicula.getResults());
            }

            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {

            }
        });
    }

    public void traerSimilares(final ResultListener<List<Pelicula>> resultListenerController,Integer movieId){
        Call<ContainerPelicula> call = peliculasService.traerSimilares(movieId,LANGUAGE, API_KEY);
        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
                ContainerPelicula containerPelicula = response.body();
                resultListenerController.finish(containerPelicula.getResults());
            }

            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {
                Log.d("ewfwq","dwsfaw");
            }
        });
    }

    public void traerPeliculasPorBusqueda(final ResultListener<List<Pelicula>> resultListenerController, String movieBusqueda){
        Call<ContainerPelicula> call = peliculasService.traerPeliculaPorBusqueda(API_KEY, LANGUAGE, movieBusqueda);

        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
               ContainerPelicula containerPelicula = response.body();
                resultListenerController.finish(containerPelicula.getResults());
            }

            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {
                Log.d("ewfwq","dwsfaw");
            }
        });
    }

    public void traerPeliculasPorGenero(final ResultListener<List<Pelicula>> listResultListener, Integer genero) {
        Call<ContainerPelicula> call = peliculasService.traerPeliculasPorGenero(API_KEY, genero, LANGUAGE);
        call.enqueue(new Callback<ContainerPelicula>() {
            @Override
            public void onResponse(Call<ContainerPelicula> call, Response<ContainerPelicula> response) {
                ContainerPelicula containerPelicula = response.body();
                listResultListener.finish(containerPelicula.getResults());
            }

            @Override
            public void onFailure(Call<ContainerPelicula> call, Throwable t) {

            }
        });
    }
}
