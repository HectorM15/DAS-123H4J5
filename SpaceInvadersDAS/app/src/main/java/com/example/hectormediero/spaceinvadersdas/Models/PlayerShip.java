package com.example.hectormediero.spaceinvadersdas.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.hectormediero.spaceinvadersdas.R;

public class PlayerShip {

    RectF rect;

    // La nave espacial del jugador será representada por un Bitmap
    private Bitmap bitmap;

    // Que tan ancho y alto puede llegar nuestra nave espacial
    private float length;
    private float height;

    private int screenX, screenY;
    // X es la parte extrema a la izquierda del rectángulo el cual forma nuestra nave espacial
    private float x;

    // Y es la coordenada de a mero arriba
    private float y;

    // Esto va a mantener la rapidez de los pixeles por segundo a la que la nave espacial se moverá
    private float shipSpeed;

    // En qué direcciones se puede mover la nave espacial
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    public final int UP = 3;
    public final int DOWN = 4;

    // Se esta moviendo la nave espacial y en que dirección
    private int shipMoving = STOPPED;

    // Cuando creamos un objeto de esta clase daremos
// la anchura y la altura de la pantalla
    public PlayerShip(Context context, int screenX, int screenY) {

        // Inicializa un RectF vacío
        rect = new RectF();
        this.screenX=screenX;
        this.screenY=screenY;
        length = screenX / 16;
        height = screenY / 16;

        // Inicia la nave en el centro de la pantalla aproximadamente
        x = screenX / 2;
        y = screenY - height -10;

        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.playership);

        // ajusta el bitmap a un tamaño proporcionado a la resolución de la pantalla
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        // Qué tan rápido va la nave espacial en pixeles por segundo
        shipSpeed = 350;
    }

    public RectF getRect() {
        return rect;
    }

    // Este es un método de "get" para hacer el rectángulo que
// define nuestra nave espacial disponible en la clase de SpaceInvadersView
    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLength() {
        return length;
    }

    // Este método será usado para cambiar/establecer si la nave
// espacial va a la izquierda, la derecha o no se mueve
    public void setMovementState(int state) {
        shipMoving = state;
    }

    // Determina si la nave espacial del jugador necesita moverse y cambiar las coordenadas
// que están en x si es necesario
    public void update(long fps) {
        if (shipMoving == LEFT) {
            if ((x = x - shipSpeed / fps) <= 0)
                x = 0;
            else
                x = x - shipSpeed / fps;
        } else if (shipMoving == RIGHT) {
            if ((x = x + shipSpeed / fps) >= (length * 15))
                x = length * 15;
            else
                x = x + shipSpeed / fps;
        } else if (shipMoving == UP) {
            if ((y = y - shipSpeed / fps) >= (height * 15))
                y =0;
            else
                y = y - shipSpeed / fps;
        } else if (shipMoving == DOWN) {
            if ((y = y + shipSpeed / fps) >= (height * 15))
                y =(height*15);
            else
                y = y + shipSpeed / fps;
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;


    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void actualizarRectangulo(float x, float y){
        setX(x);
        setY(y);
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }
}
