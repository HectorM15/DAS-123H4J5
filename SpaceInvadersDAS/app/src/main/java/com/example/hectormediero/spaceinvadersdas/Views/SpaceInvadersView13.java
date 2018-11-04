package com.example.hectormediero.spaceinvadersdas.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.Image;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hectormediero.spaceinvadersdas.Activities.ScoreActivity;
import com.example.hectormediero.spaceinvadersdas.Models.DefenceBrick;
import com.example.hectormediero.spaceinvadersdas.Models.Invader;
import com.example.hectormediero.spaceinvadersdas.Models.PlayerShip;
import com.example.hectormediero.spaceinvadersdas.R;

public class SpaceInvadersView13 extends SurfaceView implements Runnable {

    Context context;
    final Intent scoreGame;
    Bitmap dch, izq, arr, abj;
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
        scoreGame = new Intent(context.getApplicationContext(), ScoreActivity.class);
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

        for (int i = 0; i < numInvaders; i++) {
            if (invaders[i].getVisibility()) {
                for (int j = 0; j < numBricks; j++) {
                    if (bricks[i].getVisibility()) {
                        if (RectF.intersects(invaders[i].getRect(), bricks[j].getRect())) {
                            bricks[j].setInvisible();
                        }
                    }
                }
            }
        }
        // ¿Chocó algún invader en el extremo de la pantalla?
        if (bumped) {

            // Mueve a todos los invaders hacia abajo y cambia la dirección
            for (int i = 0; i < numInvaders; i++) {
                invaders[i].dropDownAndReverse();
                // Han aterrizado los invaders
                if (invaders[i].getY() > screenY - screenY / 10) {
                    gameOver();
                }
            }


        }

        if (lost) {
            gameOver();
        }


    }

    private void win() {
        scoreGame.putExtra("mayor13", "false");
        scoreGame.putExtra("result", "YOU WON");
        scoreGame.putExtra("score", score);
        context.startActivity(scoreGame);

    }

    private void gameOver() {
        scoreGame.putExtra("mayor13", "false");
        scoreGame.putExtra("result", "GAME OVER");
        scoreGame.putExtra("score", score);
        context.startActivity(scoreGame);
    }

    private void draw() {

        // Asegurate de que la superficie del dibujo sea valida o tronamos
        if (ourHolder.getSurface().isValid()) {
            // Bloquea el lienzo para que este listo para dibujar
            canvas = ourHolder.lockCanvas();

            // Dibuja el color del fondo
            canvas.drawColor(Color.argb(255, 48, 45, 44));

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

            dch = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.dcha);
            izq = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.izq);
            arr = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.arr);
            abj = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.abj);

            canvas.drawBitmap(dch, 150, screenY - 150, paint);
            canvas.drawBitmap(izq, 0, screenY - 150, paint);
            canvas.drawBitmap(arr, 75, screenY - 200, paint);
            canvas.drawBitmap(abj, 75, screenY - 100, paint);


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

        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // El jugador ha tocado la pantalla
            case MotionEvent.ACTION_DOWN:
                paused = false;

                if (x > 0 && x < 0 + izq.getWidth() && y > screenY - 140 && y < screenY - 150 + izq.getHeight()) {
                    //IZQ
                    // System.out.println("IZQ");
                    playerShip.setMovementState(playerShip.LEFT);
                } else if (x > 150 && x < 150 + izq.getWidth() && y > screenY - 150 && y < screenY - 140 + izq.getHeight()) {
                    //DCHA
                    //System.out.println("DCHA");
                    playerShip.setMovementState(playerShip.RIGHT);
                } else if (x > 75 && x < 75 + izq.getWidth() && y > screenY - 200 && y < screenY - 190 + izq.getHeight()) {
                    //UP
                    //System.out.println("UP");
                    playerShip.setMovementState(playerShip.UP);
                } else if (x > 75 && x < 75 + izq.getWidth() && y > screenY - 100 && y < screenY - 90 + izq.getHeight()) {
                    //DOWN
                    //System.out.println("DOWN");
                    playerShip.setMovementState(playerShip.DOWN);
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