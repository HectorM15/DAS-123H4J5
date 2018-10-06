package com.example.hectormediero.spaceinvadersdas;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class SpaceInvadersView13 extends SurfaceView implements Runnable {

    Context context;

    // Esta es nuestra secuencia
    private Thread gameThread = null;

    // Nuestro SurfaceHolder para bloquear la superficie antes de que dibujemos nuestros gráficos
    private SurfaceHolder ourHolder;

    // Un booleano el cual vamos a activar y desactivar
    // cuando el juego este activo- o no.
    private volatile boolean playing;

    // El juego esta pausado al iniciar
    private boolean paused = true;

    // Un objeto de lienzo (Canvas) y de pintar (Paint)
    private Canvas canvas;
    private Paint paint;

    // Esta variable rastrea los cuadros por segundo del juego
    private long fps;

    // Esto se utiliza para ayudar a calcular los cuadros por segundo
    private long timeThisFrame;

    // El tamaño de la pantalla en pixeles
    private int screenX;
    private int screenY;

    // La nave del jugador
    private PlayerShip playerShip;


    // Hasta 60 Invaders
    Invader[] invaders = new Invader[60];
    int numInvaders = 0;

    // Las guaridas del jugador están construidas a base de ladrillos
    private DefenceBrick[] bricks = new DefenceBrick[400];
    private int numBricks;

    // Para los efectos de sonido
    

    // La puntuación
    int score = 0;

    // Vidas
    private int lives = 1;

    // ¿Que tan amenazador debe de ser el sonido?
    private long menaceInterval = 1000;
    // Cual amenazante sonido debe de seguir en reproducirse
    private boolean uhOrOh;
    // Cuando fué la última vez que reproducimos un amenazante sonido
    private long lastMenaceTime = System.currentTimeMillis();

    // Cuando inicializamos (call new()) en gameView
// Este método especial de constructor se ejecuta
    public SpaceInvadersView13(Context context, int x, int y) {

        // La siguiente línea del código le pide a
        // la clase de SurfaceView que prepare nuestro objeto.
        // !Que amable¡.
        super(context);

        // Hace una copia del "context" disponible globalmete para que la usemos en otro método
        this.context = context;

        // Inicializa los objetos de ourHolder y paint
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;



        prepareLevel();
    }

    private void prepareLevel() {

        // Aquí vamos a inicializar todos los objetos del juego

        // Hacer una nueva nave espacial del jugador
        playerShip = new PlayerShip(context, screenX, screenY);

        // Construir un ejercito de invaders
        numInvaders = 0;
        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 5; row++) {
                invaders[numInvaders] = new Invader(context, row, column, screenX, screenY);
                numInvaders++;
            }
        }
        // Construir las guaridas
        numBricks = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new DefenceBrick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }

    }

    // Este método se ejecuta cuando el jugador empieza el juego

    @Override
    public void run() {

        while (playing) {

            // Captura el tiempo actual en milisegundos en startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Actualiza el cuadro
            if (!paused) {
                update();
            }

            // Dibuja el cuadro
            draw();

            // Calcula los cuadros por segundo de este cuadro
            // Ahora podemos usar los resultados para
            // medir el tiempo de animaciones y otras cosas más.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // Vamos a hacer algo nuevo aquí hacia el final de proyecto

        }
    }

    private void update() {

        // ¿Chocó el invader contra el lado de la pantalla?
        boolean bumped = false;

        // ¿Ha perdido el jugador?
        boolean lost = false;

        // Mueve la nave espacial del jugador
        playerShip.update(fps);

        // Actualiza a los invaders si se ven
        for (int i = 0; i < numInvaders; i++) {

            if (invaders[i].getVisibility()) {
                // Mueve el siguiente invader
                invaders[i].update(fps);

                // Si ese movimiento causó que golpearan la pantalla,
                // cambia bumped a true.
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0) {

                    bumped = true;

                }
            }

        }

        // Actualiza a todas las balas de los invaders si están activas

        // ¿Chocó algún invader en el extremo de la pantalla?
        if (bumped) {

            // Mueve a todos los invaders hacia abajo y cambia la dirección
            for (int i = 0; i < numInvaders; i++) {
                invaders[i].dropDownAndReverse();
                // Han aterrizado los invaders
                if (invaders[i].getY() > screenY - screenY / 10) {
                    lost = true;
                }
            }

            // Incrementa el nivel de amenaza
            // al hacer los sonidos más frecuentes
            menaceInterval = menaceInterval - 80;
        }

        if (lost) {
            prepareLevel();
        }



    }

    private void draw() {

        // Asegurate de que la superficie del dibujo sea valida o tronamos
        if (ourHolder.getSurface().isValid()) {
            // Bloquea el lienzo para que este listo para dibujar
            canvas = ourHolder.lockCanvas();

            // Dibuja el color del fondo
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Escoje el color de la brocha para dibujar
            paint.setColor(Color.argb(255, 255, 255, 255));

            // Dibuja a la nave espacial del jugador
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - 50, paint);
            // Dibuja a los invaders
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (uhOrOh) {
                        canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                    } else {
                        canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY(), paint);
                    }
                }
            }
            // Dibuja los ladrillos si están visibles
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }

            // Dibuja la puntuación y las vidas restantes
            // Cambia el color de la brocha
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score, 10, 50, paint);

            // Extrae todo a la pantalla
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // Si SpaceInvadersActivity es pausado/detenido
    // apaga nuestra secuencia.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // Si SpaceInvadersActivity es iniciado entonces
    // inicia nuestra secuencia.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // La clase de SurfaceView implementa a onTouchListener
    // Así es que podemos anular este método y detectar toques a la pantalla.
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // El jugador ha tocado la pantalla
            case MotionEvent.ACTION_DOWN:

                paused = false;

                //laterales de la pantalla
                if (motionEvent.getX() <= (screenX / 3)) {
                    //se mueve a la izq
                    playerShip.setMovementState(playerShip.LEFT);
                } else if (((screenX / 3) * 2) < motionEvent.getX()) {
                    //se mueve a la dcha
                    playerShip.setMovementState(playerShip.RIGHT);
                }


                break;

            // El jugador a retirado el dedo de la pantalla
            case MotionEvent.ACTION_UP:
                //se para
                playerShip.setMovementState(playerShip.STOPPED);
                break;
        }

        return true;
    }

}