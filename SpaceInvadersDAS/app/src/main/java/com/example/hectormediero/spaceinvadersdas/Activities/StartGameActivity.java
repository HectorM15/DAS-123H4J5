package com.example.hectormediero.spaceinvadersdas.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hectormediero.spaceinvadersdas.Activities.SpaceInvaderActivity;
import com.example.hectormediero.spaceinvadersdas.R;

public class StartGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        //Verifica si el permiso de la cámara no está concedido
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso no se encuentra concedido se solicita
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

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
