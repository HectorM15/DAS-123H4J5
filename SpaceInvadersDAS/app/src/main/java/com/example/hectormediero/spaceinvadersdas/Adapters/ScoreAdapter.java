package com.example.hectormediero.spaceinvadersdas.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hectormediero.spaceinvadersdas.Models.Score;
import com.example.hectormediero.spaceinvadersdas.R;

import java.util.ArrayList;

public class ScoreAdapter extends ArrayAdapter<Score> {
    private Context context;
    private ArrayList<Score> datos;

    public ScoreAdapter(Context context, ArrayList datos) {
        super(context, R.layout.item_list_view,datos);
        this.context=context;
        this.datos=datos;
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
        imagen.setImageResource(R.drawable.celeste);

        TextView numero = (TextView) item.findViewById(R.id.etNumero);
        numero.setText(datos.get(position).getPuntuacion().toString());
        //numero.setText("Hola");

        TextView etiqueta = (TextView) item.findViewById(R.id.etTag);
        etiqueta.setText(datos.get(position).getNombre());


        return item;
    }
}
