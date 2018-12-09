package com.example.hectormediero.spaceinvadersdas.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hectormediero.spaceinvadersdas.Models.Score;
import com.example.hectormediero.spaceinvadersdas.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreAdapter extends ArrayAdapter<Score> {
    private Context context;
    private ArrayList<Score> datos;
    private String mCurrentPhotoPath;
    public ScoreAdapter(Context context, ArrayList datos,String mCurrentPhotoPath) {
        super(context, R.layout.item_list_view,datos);
        this.context=context;
        this.datos=datos;
        this.mCurrentPhotoPath=mCurrentPhotoPath;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item_list_view, null);
        // A partir de la vista, recogeremos los controles que contiene para
        // poder manipularlos.
        // Recogemos el ImageView y le asignamos una foto.
        ImageView imagen = (ImageView) item.findViewById(R.id.imageView);
       // imagen.setImageResource(R.drawable.celeste);
        setPic(imagen,datos.get(position).getNombre());

        TextView numero = (TextView) item.findViewById(R.id.etNumero);
        numero.setText(datos.get(position).getPuntuacion().toString());

        TextView etiqueta = (TextView) item.findViewById(R.id.etTag);
        etiqueta.setText(datos.get(position).getNombre());


        return item;
    }
    private void setPic(ImageView imageView,String username) {
        // Get the dimensions of the View
        int targetW = 100;
        int targetH = 100;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        String pathImage=findImage(username);
        if(pathImage.equals("No")){
            imageView.setImageResource(R.drawable.celeste);
        }else{
            BitmapFactory.decodeFile(pathImage, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(pathImage, bmOptions);
            imageView.setImageBitmap(bitmap);
        }

    }

    private String findImage(String username){
        String path = mCurrentPhotoPath +File.separator;
        File f = new File(path);
        //obtiene nombres de archivos dentro del directorio.
        File file[] = f.listFiles();
        for (int i=0; i < file.length; i++)
        {

            if(file[i].getName().contains(username)){
                return file[i].getAbsolutePath();
            }
        }
        return "No";
    }
}
