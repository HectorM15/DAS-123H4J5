package com.example.hectormediero.spaceinvadersdas.Models;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Button {
    private RectF rect;
    private float x;
    private float y;
    private float length;

    private Bitmap bitmap1;

    public RectF getRect() {
        return rect;
    }

    public Bitmap getBitmap() {
        return bitmap1;
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

}
