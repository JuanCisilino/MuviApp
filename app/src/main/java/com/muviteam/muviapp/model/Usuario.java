package com.muviteam.muviapp.model;

public class Usuario {

    private String nombre, apellido, imagenUrl;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String imagenUrl) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.imagenUrl = imagenUrl;
    }

    public String getImagenUrl() {return imagenUrl;}

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

}
