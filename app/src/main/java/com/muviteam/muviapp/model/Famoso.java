package com.muviteam.muviapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Famoso implements Serializable {

    public static final String BASE_URL = "https://image.tmdb.org/t/p/original";

    @SerializedName("name")
    private String StringNombre;
    private String biography;
    @SerializedName("profile_path")
    private String StringImagenDeFamoso;
    @SerializedName("id")
    private Integer StringId;

    public Famoso(String nombre, String biografia, String imagenDeFamoso, Integer id){
        this.StringNombre = nombre;
        this.biography = biografia;
        this.StringImagenDeFamoso = imagenDeFamoso;
        this.StringId = id;
    }

    public Famoso(){

    }

    public Integer getId() {
        return StringId;
    }

    public String getNombre() {
        return StringNombre;
    }

    public String getStringNombre() {
        return StringNombre;
    }

    public void setStringNombre(String stringNombre) {
        StringNombre = stringNombre;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getStringImagenDeFamoso() {
        return StringImagenDeFamoso;
    }

    public void setStringImagenDeFamoso(String stringImagenDeFamoso) {
        StringImagenDeFamoso = stringImagenDeFamoso;
    }

    public Integer getStringId() {
        return StringId;
    }

    public void setStringId(Integer stringId) {
        StringId = stringId;
    }

    public String generaURLImagen(){
        return BASE_URL + StringImagenDeFamoso;
    }
}
