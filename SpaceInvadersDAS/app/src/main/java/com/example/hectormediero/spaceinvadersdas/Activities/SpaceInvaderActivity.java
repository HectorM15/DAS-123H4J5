package com.example.hectormediero.spaceinvadersdas.Activities;

import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.view.Display;

import com.example.hectormediero.spaceinvadersdas.Views.SpaceInvadersView;
import com.example.hectormediero.spaceinvadersdas.Views.SpaceInvadersView13;

// SpaceInvadersActivity es el punto de entrada al juego.
// Se va a encargar del ciclo de vida del juego al llamar
// los métodos de spaceInvadersView cuando sean solicitados por el OS.

public class SpaceInvaderActivity extends Activity {

    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego
    // y responderá a los toques a la pantalla
    SpaceInvadersView spaceInvadersView;
    SpaceInvadersView13 spaceInvadersView13;
    String mayor13,username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mayor13=getIntent().getExtras().getString("mayor13");
        username=getIntent().getExtras().getString("username");
        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);
        System.out.println(username);
        if(mayor13.equals("true")){
             spaceInvadersView = new SpaceInvadersView(this, size.x, size.y,username);
            setContentView(spaceInvadersView);
        }else{
             spaceInvadersView13 = new SpaceInvadersView13(this, size.x, size.y);
            setContentView(spaceInvadersView13);
        }

        // Inicializar gameView y establecerlo como la visualización



    }

    @Override
    protected void onResume() {
        super.onResume();
        // Le dice al método de reanudar del gameView que se ejecute
        if(mayor13.equals("true")){
            spaceInvadersView.resume();
        }else{
            spaceInvadersView13.resume();
        }

    }

    // Este método se ejecuta cuando el jugador se sale del juego
    @Override
    protected void onPause() {
        super.onPause();
        // Le dice al método de pausa del gameView que se ejecute
        if(mayor13.equals("true")){
            spaceInvadersView.pause();
        }else{
            spaceInvadersView13.pause();
        }
    }

    public String getUsername() {
        return username;
    }
}
