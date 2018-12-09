package com.example.hectormediero.spaceinvadersdas.Models;

import android.support.annotation.NonNull;

public class Score implements Comparable {

    private Integer puntuacion;
    private String nombre;
    private int imagen;

    public Score(Integer puntuacion, String nombre) {
        this.puntuacion = puntuacion;
        this.nombre = nombre;
    }

    public Score(int i, String s, int amaranja) {
        this.puntuacion = i;
        this.nombre = s;
        this.imagen= amaranja;
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
