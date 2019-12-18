package com.muviteam.muviapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Pelicula implements Serializable {

    public static final String BASE_URL = "https://image.tmdb.org/t/p/original";

    @SerializedName("title")
    private String stringTitulo;
    @SerializedName("id")
    private Integer integerId;
    private String stringDirector;
    @SerializedName("release_date")
    private String stringFechaDeEstreno;
    @SerializedName("poster_path")
    private String stringImagenurl;
    @SerializedName("backdrop_path")
    private String stringImagenurlcelda;
    @SerializedName("overview")
    private String stringSinopsis;
    @SerializedName("vote_average")
    private Float popularity;


    public Float getPopularity() {
        return popularity;
    }

    public Pelicula(){

    }

    public Pelicula(String stringTitulo, String stringImagenurl, String stringImagenurlcelda) {
        this.stringTitulo = stringTitulo;
        this.integerId = integerId;
        this.stringDirector = stringDirector;
        this.stringFechaDeEstreno = stringFechaDeEstreno;
        this.stringImagenurl = stringImagenurl;
        this.stringImagenurlcelda = stringImagenurlcelda;
        this.stringSinopsis = stringSinopsis;
    }

    public String getTitulo() {
        return stringTitulo;
    }

    public void setTitulo(String titulo) {
        this.stringTitulo = titulo;
    }

    public String getDirector() {
        return stringDirector;
    }

    public void setDirector(String director) {
        this.stringDirector = director;
    }

    public String getFechaDeEstreno() {
        return stringFechaDeEstreno;
    }

    public void setFechaDeEstreno(String fechaDeEstreno) {
        this.stringFechaDeEstreno = fechaDeEstreno;
    }

    public String getImagenurl() {
        return stringImagenurl;
    }

    public void setImagenurl(String imagenurl) {
        this.stringImagenurl = imagenurl;
    }

    public String getSinopsis() {
        return stringSinopsis;
    }

    public void setSinopsis(String sinopsis) {
        sinopsis = sinopsis;
    }

    public Integer getId() {
        return integerId;
    }

    public void setId(Integer id) {
        this.integerId = id;
    }

    public String generaURLImagen(){
        return BASE_URL + stringImagenurl;
    }

    public String generaURLImagencelda(){
        return BASE_URL + stringImagenurlcelda;
    }

    public String getImagenurlcelda() {
        return stringImagenurlcelda;
    }

    public void setImagenurlcelda(String imagenurlcelda) {
        this.stringImagenurlcelda = imagenurlcelda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return integerId.equals(pelicula.integerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(integerId);
    }

}
