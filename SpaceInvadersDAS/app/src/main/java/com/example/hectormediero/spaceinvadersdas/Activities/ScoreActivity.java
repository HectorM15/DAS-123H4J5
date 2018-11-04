package com.example.hectormediero.spaceinvadersdas.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hectormediero.spaceinvadersdas.BDD.BaseDeDatosPuntuaciones;
import com.example.hectormediero.spaceinvadersdas.R;
import com.example.hectormediero.spaceinvadersdas.Models.Score;
import com.example.hectormediero.spaceinvadersdas.Views.CustomList;
import com.example.hectormediero.spaceinvadersdas.Views.DialogBuild;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.example.hectormediero.spaceinvadersdas.BDD.FileHelper.INTERNO;
import static com.example.hectormediero.spaceinvadersdas.BDD.FileHelper.SD;
import static com.example.hectormediero.spaceinvadersdas.BDD.FileHelper.leerDeFichero;

public class ScoreActivity extends AppCompatActivity {
    final private Integer maxNumScores = 15;
    private BaseDeDatosPuntuaciones bdd;
    private String mayor13,username;
    private Integer score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        bdd = new BaseDeDatosPuntuaciones(this);

        final Intent spaceGame = new Intent(getApplicationContext(), SpaceInvaderActivity.class);
        final Intent startGame = new Intent(getApplicationContext(), StartGameActivity.class);

        String result = getIntent().getExtras().getString("result");
        mayor13 = getIntent().getExtras().getString("mayor13");
        score = getIntent().getExtras().getInt("score");
        username = getIntent().getExtras().getString("username");

        TextView resultTV = findViewById(R.id.GameResult);
        // TextView scoreTV = findViewById(R.id.GameScore);
        Button replayGame = findViewById(R.id.replayButton);
        final ArrayList<Score> arrayPuntuaciones = new ArrayList<>();
        arrayPuntuaciones.add(new Score(3000, "Hector"));
        arrayPuntuaciones.add(new Score(2000, "Pablo"));
        arrayPuntuaciones.add(new Score(1000, "Karol"));
        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("nueva_puntuacion.txt")));
            String lineaActual;
            while ((lineaActual = fin.readLine()) != null) {
                System.out.println(lineaActual);
                String[] puntuacioneGuardadas = lineaActual.split("#");
                for (int i = 0; i < puntuacioneGuardadas.length; i++) {
                    String[] datosPuntuacion = puntuacioneGuardadas[i].split("¬");
                    System.out.println(datosPuntuacion[0] + "-" + datosPuntuacion[1]);
                    arrayPuntuaciones.add(new Score(Integer.parseInt(datosPuntuacion[1]), datosPuntuacion[0]));
                }
            }
            fin.close();

            Log.i("Ficheros", "Fichero leido!");
            Log.e("Ficheros", "Texto: " + lineaActual);
        } catch (FileNotFoundException e) {
            Log.i("Ficheros", "Fichero no leido!");
        } catch (IOException e) {
            Log.i("Ficheros", "ALGO PASA!");
        }

        String[] array = new String[arrayPuntuaciones.size()];
        int contador = 0;
        for (Score s : arrayPuntuaciones) {
            array[contador] = s.toString();
            contador++;
        }
        System.out.println(arrayPuntuaciones.toString());
        /*CustomList listAdapter = new
                CustomList(this,arrayPuntuaciones);
        */

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.row_view, array);
        ListView listView = (ListView) findViewById(R.id.lista_puntuaciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                System.out.println(arrayPuntuaciones.get(position).getNombre());
            }
        });
        resultTV.setText(result);
        // scoreTV.setText(Integer.toString(score));


        final AlertDialog.Builder rejugarDialog = new AlertDialog.Builder(this);
        rejugarDialog.setTitle("Aviso");
        rejugarDialog.setMessage("¿Desea volver a jugar?");
        rejugarDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                spaceGame.putExtra("mayor13", mayor13);
                spaceGame.putExtra("username", username);
                startActivity(spaceGame);
                finish();
            }
        });
        rejugarDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                startActivity(startGame);
                finish();
            }
        });

        replayGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                rejugarDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void addScore() {
        String name = "";
        Integer puntuacion = score;
        bdd.addScore(new Score(puntuacion, name));
    }

    private void createTableScore() {
        Integer i = 0;
        Cursor c = bdd.getAllScore();
        while (c.moveToNext() && i < maxNumScores) {

        }
    }


}
