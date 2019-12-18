package com.muviteam.muviapp.controller;

import com.muviteam.muviapp.model.FirestoreDao;
import com.muviteam.muviapp.model.Pelicula;
import com.muviteam.muviapp.utils.ResultListener;

import java.util.List;

public class ControllerFirestore {

    private FirestoreDao firestoreDao;

    public ControllerFirestore() {
        firestoreDao = new FirestoreDao();
    }

    public void agregarPeliculaAFav(Pelicula pelicula){
        firestoreDao.agregarPeliculaAFav(pelicula);
    }

    public void traerListaDeFavorito(final ResultListener<List<Pelicula>> listenerDeLaVista){
        firestoreDao.traerPeliculasFavoritas(new ResultListener<List<Pelicula>>() {
            @Override
            public void finish(List<Pelicula> result) {
                listenerDeLaVista.finish(result);
            }
        });
    }
}
