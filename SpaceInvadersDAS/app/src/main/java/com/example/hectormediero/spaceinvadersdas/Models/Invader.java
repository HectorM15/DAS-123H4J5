package com.example.hectormediero.spaceinvadersdas.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.hectormediero.spaceinvadersdas.R;

import java.util.Random;

public class Invader {

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

    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Se está moviendo la nave espacial y en qué dirección
    private int shipMoving = RIGHT;

    boolean isVisible;
    Paint paint = new Paint();
    Context contextPri;

    public Invader(Context context, int row, int column, int screenX, int screenY) {

        // Inicializa un RectF vacío
        rect = new RectF();
        contextPri = context;
        length = screenX / 20;
        height = screenY / 20;

        isVisible = true;

        int padding = screenX / 25;

        x = column * (length + padding);
        y = row * (length + padding / 4);

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
        shipSpeed = 40;
    }

    public void setInvisible() {
        isVisible = false;
    }

    public boolean getVisibility() {
        return isVisible;
    }

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap1;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
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

    public void update(long fps) {
        if (shipMoving == LEFT) {
            x = x - shipSpeed / fps;
        }

        if (shipMoving == RIGHT) {
            x = x + shipSpeed / fps;
        }

        // Actualiza rect el cual es usado para detectar impactos
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    public void dropDownAndReverse() {
        if (shipMoving == LEFT) {
            shipMoving = RIGHT;
        } else {
            shipMoving = LEFT;
        }

        y = y + height;

    }

    public boolean takeAim(float playerShipX, float playerShipLength) {

        int randomNumber = -1;

        // Si está cerca del jugador
        if ((playerShipX + playerShipLength > x &&
                playerShipX + playerShipLength < x + length) || (playerShipX > x && playerShipX < x + length)) {
            return true;

        }

        // Si está disparando aleatoriamente (sin estar cerca del jugador) una probabilidad de 1 en 5000
        randomNumber = generator.nextInt(10);
        if (randomNumber == 0) {
            return true;
        }

        return false;
    }

    public void cambiarColor(Boolean cambiado) {
        if (cambiado) {
            bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.verde);
        } else {
            bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.invader2);
        }
        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);
    }
    public void cambiarColor(Integer cambiado) {
        switch (cambiado) {
            case 1:
                bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.verde);
                break;
            case 2:
                bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.invader2);
                break;
            case 3:
                bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.verde);
                break;
            case 4:
                bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.verde);
                break;
            case 5:
                bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.verde);
                break;
            case 6:
                bitmap2 = BitmapFactory.decodeResource(contextPri.getResources(), R.drawable.verde);
                break;
        }


        bitmap2 = Bitmap.createScaledBitmap(bitmap2,
                (int) (length),
                (int) (height),
                false);
    }

}
