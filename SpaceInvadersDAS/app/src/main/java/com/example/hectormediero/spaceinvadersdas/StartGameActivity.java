package com.example.hectormediero.spaceinvadersdas;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartGameActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        Button btnEmpezar;
        final Intent  spaceGame = new Intent(getApplicationContext(), SpaceInvaderActivity.class);

        btnEmpezar = findViewById(R.id.startGameButton);
        final AlertDialog.Builder restEdad = new AlertDialog.Builder(this);
        restEdad.setTitle("Importante");
        restEdad.setMessage("¿ Tiene usted más de 13 años ?");
        restEdad.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                spaceGame.putExtra("mayor13", "true");
                startActivity(spaceGame);
            }
        });
        restEdad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                spaceGame.putExtra("mayor13", "false");
                startActivity(spaceGame);
            }
        });

        btnEmpezar.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                restEdad.show();
            }
        });
    }


}
