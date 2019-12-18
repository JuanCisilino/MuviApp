package com.muviteam.muviapp.model;

import java.util.ArrayList;
import java.util.List;

public class ContainerPelicula {

    private List<Pelicula> results;

    public ContainerPelicula() {
        this.results = new ArrayList<>();
    }

    public List<Pelicula> getResults() {
        return results;
    }

    public void setResults(List<Pelicula> results) {
        this.results = results;
    }

    public void agregarPelicula(Pelicula pelicula){
        results.add(pelicula);
    }
    public void removerPelicula(Pelicula pelicula){
        results.remove(pelicula);
    }

    public Boolean contieneLaPelicula(Pelicula pelicula){
        return results.contains(pelicula);
    }
}
