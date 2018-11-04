package com.example.hectormediero.spaceinvadersdas.Views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hectormediero.spaceinvadersdas.Models.Score;
import com.example.hectormediero.spaceinvadersdas.R;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter{
    private final Activity context;
    private final ArrayList<Score> web;

    public CustomList(Activity context, ArrayList<Score> web) {

        super(context, R.layout.row_view);
        this.context = context;
        this.web = web;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.row_view, null, true);
        return rowView;
    }
}