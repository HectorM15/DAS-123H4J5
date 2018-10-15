package com.example.hectormediero.spaceinvadersdas.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hectormediero.spaceinvadersdas.Models.Score;

public class BaseDeDatosPuntuaciones extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "puntuaciones.db";


    public static final String TABLA_PUNTUACIONES = "Ranking";
    public static final String COLUMNA_ID = "_id";
    public static final String COLUMNA_NOMBRE = "Nombre";
    public static final String COLUMNA_PUNTUACION = "Puntuacion";

    private static final String SQL_CREAR = "create table "
            + TABLA_PUNTUACIONES + "" +
            "("
            + COLUMNA_ID + " integer primary key autoincrement, "
            + COLUMNA_NOMBRE + " text not null,"
            +COLUMNA_PUNTUACION+
            ");";

    public BaseDeDatosPuntuaciones(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addScore(Score score){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMNA_NOMBRE, score.getNombre());
        values.put(COLUMNA_PUNTUACION, score.getPuntuacion());

        db.insert(TABLA_PUNTUACIONES, null,values);
        db.close();
    }


    public boolean remove(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLA_PUNTUACIONES,
                    " _id = ?",
                    new String[] { String.valueOf (id ) });
            db.close();
            return true;

        }catch(Exception ex){
            return false;
        }
    }

    public void getScoreById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_NOMBRE};

        Cursor cursor =
                db.query(TABLA_PUNTUACIONES,
                        projection,
                        " _id = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();

        System.out.println("El nombre es " +  cursor.getString(1) );
        db.close();

    }

    public Cursor getAllScore(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMNA_ID, COLUMNA_NOMBRE,COLUMNA_PUNTUACION};

        Cursor cursor =
                db.query(TABLA_PUNTUACIONES,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();

        System.out.println("El nombre es " +  cursor.getString(1) +" y su puntuación es de : "+cursor.getString(2));
        db.close();
        return cursor;

    }

    public void updateNombre(String nombre, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre",nombre);

        int i = db.update(TABLA_PUNTUACIONES,
                values,
                " _id = ?",
                new String[] { String.valueOf( id ) });
        db.close();

    }
}
