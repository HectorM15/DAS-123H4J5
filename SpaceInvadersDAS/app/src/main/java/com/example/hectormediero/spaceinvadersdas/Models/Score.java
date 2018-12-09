package com.example.hectormediero.spaceinvadersdas.Models;

import android.support.annotation.NonNull;

public class Score implements Comparable {

    private Integer puntuacion;
    private String nombre;
    private String rutaImagen;

    public Score(Integer puntuacion, String nombre) {
        this.puntuacion = puntuacion;
        this.nombre = nombre;
    }

    public Score(int i, String s, String amaranja) {
        this.puntuacion = i;
        this.nombre = s;
        this.rutaImagen= amaranja;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @Override
    public String toString() {
       return (nombre+"  Puntuacion: "+puntuacion);
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if(this.puntuacion>((Score) o).puntuacion){
            return -1;
        }else if(this.puntuacion<((Score) o).puntuacion){
            return 1;
        }
        return 0;
    }
}
