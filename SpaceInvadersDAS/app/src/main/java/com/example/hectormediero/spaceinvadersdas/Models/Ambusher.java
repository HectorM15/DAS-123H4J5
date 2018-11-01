package com.example.hectormediero.spaceinvadersdas.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.hectormediero.spaceinvadersdas.R;

import java.util.Random;

public class Ambusher {
    RectF rect;

    Random generator = new Random();

    // La nave espacial del jugador va a ser representada por un Bitmap
    private Bitmap bitmap1;
    private Bitmap bitmap2;

    // Qué tan largo y ancho será nuestro Invader
    private float length;
    private float height;

    // X es el extremo a la izquierda del rectángulo que le da forma a nuestro invader
    private float x;

    // Y es la coordenada superior
    private float y;

    // Esto mantendrá la rapidez de los pixeles por segundo a la que el invader se moverá.
    private float shipSpeed;


    public final int RIGHT = 2;

    // Se está moviendo la nave espacial y en qué dirección
    private int shipMoving = RIGHT;

    boolean isVisible = false;

    public Ambusher(Context context, int row, int column, int screenX, int screenY) {

        // Inicializa un RectF vacío
        rect = new RectF();

        length = screenX / 20;
        height = screenY / 20;

        int padding = screenX / 25;

        x = column * (length + padding);
        y = row * (length + padding/4);

        // Inicializa el bitmap
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader1);
        bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader2);

        // Ajusta el primer bitmap a un tamaño apropiado para la resolución de la pantalla
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                (int) (length),
                (int) (height),
                false);

        // Ajusta el segundo bitmap a un tamaño apropiado para la resolución de la pantalla
        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);

        // Qué tan rápido va el invader en pixeles por segundo
        shipSpeed = 300;
    }

    public void setVisible(){
        isVisible = true;
    }

    public void setInvisible() {isVisible = false;}

    public boolean getVisibility(){
        return isVisible;
    }

    public Bitmap getBitmap(){
        return bitmap1;
    }

    public Bitmap getBitmap2(){
        return bitmap2;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    public void update(long fps){
        if (isVisible) {
            if (shipMoving == RIGHT) {
                x = x + shipSpeed / fps;
            }
        }
        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    //Dispara de manera aleatoria, pero frecuentemente
    public boolean TakeAim () {
        int randomNumber = -1;
        randomNumber = generator.nextInt(25);
        if(randomNumber == 0) {
            return true;
        }
        return false;
    }
}