package com.example.hectormediero.spaceinvadersdas;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        final Intent spaceGame = new Intent(getApplicationContext(), SpaceInvaderActivity.class);
        final Intent startGame = new Intent(getApplicationContext(), StartGameActivity.class);

        String result=getIntent().getExtras().getString("result");
        int score =getIntent().getExtras().getInt("score");
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
                startActivity(spaceGame);
            }
        });
        rejugarDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                startActivity(startGame);
            }
        });

        replayGame.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                rejugarDialog.show();
            }
        });
    }
    @Override
    public void onBackPressed (){
        System.out.println("YO YA NO QUIERO NA");
    }
}
