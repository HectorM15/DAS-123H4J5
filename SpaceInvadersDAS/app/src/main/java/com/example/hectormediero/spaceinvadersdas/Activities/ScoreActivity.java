package com.example.hectormediero.spaceinvadersdas.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hectormediero.spaceinvadersdas.BDD.BaseDeDatosPuntuaciones;
import com.example.hectormediero.spaceinvadersdas.R;
import com.example.hectormediero.spaceinvadersdas.Models.Score;
import com.example.hectormediero.spaceinvadersdas.Views.DialogBuild;

public class ScoreActivity extends AppCompatActivity {
    final private Integer maxNumScores=15;
    private BaseDeDatosPuntuaciones bdd;
    private String mayor13;
    private Integer score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        bdd= new BaseDeDatosPuntuaciones(this);

        final Intent spaceGame = new Intent(getApplicationContext(), SpaceInvaderActivity.class);
        final Intent startGame = new Intent(getApplicationContext(), StartGameActivity.class);

        String result = getIntent().getExtras().getString("result");
        mayor13 = getIntent().getExtras().getString("mayor13");
        score = getIntent().getExtras().getInt("score");

        TextView resultTV = findViewById(R.id.GameResult);
        TextView scoreTV = findViewById(R.id.GameScore);
        Button replayGame = findViewById(R.id.replayButton);

        resultTV.setText(result);
        scoreTV.setText(Integer.toString(score));


        final AlertDialog.Builder rejugarDialog = new AlertDialog.Builder(this);
        rejugarDialog.setTitle("Aviso");
        rejugarDialog.setMessage("¿Desea volver a jugar?");
        rejugarDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                spaceGame.putExtra("mayor13", mayor13);
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
        System.out.println("YO YA NO QUIERO NA");
    }

    private void addScore(){
        String name ="";
        Integer puntuacion = score;
        bdd.addScore(new Score(puntuacion,name));
    }

    private void createTableScore(){
        Integer i=0;
        Cursor c= bdd.getAllScore();
        while(c.moveToNext() && i<maxNumScores){

        }
    }
}
