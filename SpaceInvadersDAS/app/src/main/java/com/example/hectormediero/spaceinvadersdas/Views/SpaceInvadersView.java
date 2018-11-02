package com.example.hectormediero.spaceinvadersdas.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.hectormediero.spaceinvadersdas.Activities.ScoreActivity;
import com.example.hectormediero.spaceinvadersdas.Models.Ambusher;
import com.example.hectormediero.spaceinvadersdas.Models.Bullet;
import com.example.hectormediero.spaceinvadersdas.Models.DefenceBrick;
import com.example.hectormediero.spaceinvadersdas.Models.Invader;
import com.example.hectormediero.spaceinvadersdas.Models.PlayerShip;
import com.example.hectormediero.spaceinvadersdas.R;

public class SpaceInvadersView extends SurfaceView implements Runnable {

    Context context;
    final Intent scoreGame;
    Bitmap dch, izq, arr, abj;

    // Esta es nuestra secuencia
    private Thread gameThread = null;

    Boolean hanCambiado = false;
    // Nuestro SurfaceHolder para bloquear la superficie antes de que dibujemos nuestros gráficos
    private SurfaceHolder ourHolder;

    // Un booleano el cual vamos a activar y desactivar
    // cuando el juego este activo- o no.
    private volatile boolean playing;

    // El juego esta pausado al iniciar
    private boolean paused = true;

    //Contador para el Timer de los Ambushers
    public int clock = -1;

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

    private int contadorColor;
    // La nave del jugador
    private PlayerShip playerShip;

    // La bala del jugador
    private Bullet bullet;

    // Las balas de los Invaders
    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 4;

    // Las balas de los Ambushers
    private Bullet[] ambusherBullets = new Bullet[200];
    private int nextAmbusherBullet;
    private int maxAmbusherBullets = 20;


    // Hasta 60 Invaders
    Invader[] invaders = new Invader[60];
    int numInvaders = 0;

    //Hasta 60 Ambushers
    private Ambusher[] ambusher = new Ambusher[60];
    int numAmbusher = 0;

    // Las guaridas del jugador están construidas a base de ladrillos
    private DefenceBrick[] bricks = new DefenceBrick[400];
    private int numBricks;

    // La puntuación
    int score = 0;

    // Vidas
    private int lives = 1;

    // ¿Que tan amenazador debe de ser el sonido?

    // Cual amenazante sonido debe de seguir en reproducirse
    private boolean uhOrOh;
    // Cuando fué la última vez que reproducimos un amenazante sonido
    private long lastMenaceTime = System.currentTimeMillis();

    // Cuando inicializamos (call new()) en gameView

    // Este método especial de constructor se ejecuta
    public SpaceInvadersView(Context context, int x, int y) {

        // La siguiente línea del código le pide a
        // la clase de SurfaceView que prepare nuestro objeto.
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

        // Preparar las balas del jugador
        bullet = new Bullet(screenY);

        // Inicializa la formación de invadersBullets
        for (int i = 0; i < invadersBullets.length; i++) {
            invadersBullets[i] = new Bullet(screenY);
        }

        //Prepara las balas de los Ambushers
        for (int i = 0; i < ambusherBullets.length; i++) {
            ambusherBullets[i] = new Bullet(screenY);
        }

        // Construir un ejercito de invaders
        numInvaders = 0;
        for (int column = 0; column < 5; column++) {
            for (int row = 0; row < 4; row++) {
                invaders[numInvaders] = new Invader(context, row, column, screenX, screenY);
                numInvaders++;
            }
        }

        // Prepara los Ambushers
        for (numAmbusher = 0; numAmbusher < 10; numAmbusher++) {
            ambusher[numAmbusher] = new Ambusher(context, 0, 0, screenX, screenY);
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

    //Timer para los Ambushers, cada 10 segundos aparece uno, durante 10 minutos
    CountDownTimer ambush = new CountDownTimer(600000, 10000) {
        public void onTick(long millisUntilFinished) {
            if (clock >= 0) {
                ambusher[clock].setVisible();
            }
            clock++;
        }

        @Override
        public void onFinish() {
        }
    };

    // Este método se ejecuta cuando el jugador empieza el juego

    @Override
    public void run() {
        Looper.prepare();
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
        boolean bumpedAmbusher = false;
        contadorColor = 0;
        // ¿Ha perdido el jugador?
        boolean lost = false;

        // Mueve la nave espacial del jugador
        playerShip.update(fps);


        // Actualiza a los invaders si se ven
        for (int i = 0; i < numInvaders; i++) {
            if (invaders[i].getVisibility()) {
                // Mueve el siguiente invader
                invaders[i].update(fps);

                // ¿Quiere hacer un disparo?
                if (invaders[i].takeAim(playerShip.getX(),
                        playerShip.getLength())) {
                    //¿Puedo hacerlo?
                    if (!invadersBullets[i].getStatus() && nextBullet <= maxInvaderBullets) {

                        // Si sí, intentalo y genera una bala
                        if (invadersBullets[i].shoot(invaders[i].getX()
                                        + invaders[i].getLength() / 2,
                                invaders[i].getY(), bullet.DOWN)) {

                            // Disparo realizado
                            // Preparete para el siguiente disparo
                            nextBullet++;
                        }
                    }
                }

                // Si ese movimiento causó que golpearan la pantalla,
                // cambia bumped a true.
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0) {

                    bumped = true;

                }
            }

        }

        //Actualiza a los Ambushers si están activos, y si llegan al final, desaparecen
        for (int i = 0; i < numAmbusher; i++) {
            if (ambusher[i].getVisibility()) {
                ambusher[i].update(fps);
                if (ambusher[i].TakeAim()) {
                    if (ambusherBullets[nextAmbusherBullet].shoot(ambusher[i].getX() + ambusher[i].getLength() / 2, ambusher[i].getY(), bullet.DOWN)) {
                        nextAmbusherBullet++;
                        if (nextAmbusherBullet == maxAmbusherBullets) {
                            nextAmbusherBullet = 0;
                        }
                    }
                    if (ambusher[i].getX() > screenX - ambusher[i].getLength()
                            || ambusher[i].getX() < 0) {

                        bumpedAmbusher = true;

                    }
                }
                if (ambusher[i].getX() > screenX - ambusher[i].getLength()
                        || ambusher[i].getX() < 0) {
                    bumpedAmbusher = true;
                }
                if (bumpedAmbusher) {
                    ambusher[i].setInvisible();
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
                    lost = true;
                }
            }

            // Incrementa el nivel de amenaza
            // al hacer los sonidos más frecuentes
        }

        if (lost) {
            gameOver();
        }

        // Actualiza las balas del jugador
        if (bullet.getStatus()) {
            bullet.update(fps);
        }

        // Actualiza todas las balas de los invaders si están activas
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                invadersBullets[i].update(fps);
            }
        }

        //Actualiza las balas del Ambusher
        for (int i = 0; i < ambusherBullets.length; i++) {
            if (ambusherBullets[i].getStatus()) {
                ambusherBullets[i].update(fps);
            }
        }

        // Ha tocado la parte alta de la pantalla la bala del jugador
        if (bullet.getImpactPointY() < 0) {
            bullet.changeDirection(1);
        } else if (bullet.getImpactPointY() > screenY) {
            bullet.changeDirection(0);
        }

        // Ha tocado la parte baja de la pantalla la bala del invader
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getImpactPointY() > screenY) {
                invadersBullets[i].changeDirection(0);
            } else if (invadersBullets[i].getImpactPointY() < 0) {
                invadersBullets[i].changeDirection(1);
            } else if (invadersBullets[i].getStatus()) {
                for (int k = 0; k < numInvaders; k++) {
                    if (invaders[k].getVisibility()) {
                        if (RectF.intersects(invadersBullets[i].getRect(), invaders[k].getRect()) && invadersBullets[i].getDir() == true) {//ÑAPA
                            invaders[k].setInvisible();
                            invadersBullets[i].setInactive();
                            nextBullet--;
                            score = score + 100;

                            // Ha ganado el jugador
                            if (score == numInvaders * 100) {
                                win();
                            }
                        }
                    }
                }
            }
        }

        // Las balas de los Ambushers rebotan al llegar abajo o arriba
        for (int i = 0; i < ambusherBullets.length; i++) {
            if (ambusherBullets[i].getImpactPointY() > screenY) {
                ambusherBullets[i].changeDirection(0);
            } else if (ambusherBullets[i].getImpactPointY() < 0) {
                ambusherBullets[i].changeDirection(1);
            } else if (ambusherBullets[i].getStatus()) {
                for (int k = 0; k < numAmbusher; k++) {
                    if (ambusher[k].getVisibility()) {
                        if (RectF.intersects(ambusherBullets[i].getRect(), ambusher[k].getRect()) && ambusherBullets[i].getDir() == true) {//ÑAPA
                            ambusher[k].setInvisible();
                            ambusherBullets[i].setInactive();
                            nextAmbusherBullet--;
                            score = score + 100;

                            // Ha ganado el jugador
                            if (score == numInvaders * 100) {
                                win();
                            }
                        }
                    }
                }
            }
        }

        //Han chocado los invader con los ladrillos
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

        //Han chocado playerShip  con los brick
        for (int j = 0; j < numBricks; j++) {
            if (bricks[j].getVisibility()) {
                if (RectF.intersects(playerShip.getRect(), bricks[j].getRect())) {
                    gameOver();
                }
            }
        }

        // Ha tocado la bala del jugador a algún invader
        if (bullet.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), invaders[i].getRect())) {
                        invaders[i].setInvisible();
                        bullet.setInactive();
                        score = score + 100;

                        // Ha ganado el jugador
                        if (score == numInvaders * 100) {
                            win();
                        }
                    }
                }
            }
        }

        // Ha impactado una bala alienígena a un ladrillo de la guarida
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                for (int j = 0; j < numBricks; j++) {
                    if (bricks[j].getVisibility()) {
                        if (RectF.intersects(invadersBullets[i].getRect(), bricks[j].getRect())) {
                            // A collision has occurred
                            invadersBullets[i].setInactive();
                            nextBullet--;
                            bricks[j].setInvisible();
                            contadorColor++;
                        }
                    }
                }
            }

        }

        // Ha impactado una bala Ambusher a un ladrillo de la guarida?
        for (int i = 0; i < ambusherBullets.length; i++) {
            if (ambusherBullets[i].getStatus()) {
                for (int j = 0; j < numBricks; j++) {
                    if (bricks[j].getVisibility()) {
                        if (RectF.intersects(ambusherBullets[i].getRect(), bricks[j].getRect())) {
                            // A collision has occurred
                            ambusherBullets[i].setInactive();
                            bricks[j].setInvisible();
                            contadorColor++;

                        }
                    }
                }
            }

        }


        // Ha impactado una bala del jugador a un ladrillo de la guarida
        if (bullet.getStatus()) {
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), bricks[i].getRect())) {
                        // A collision has occurred
                        bullet.setInactive();
                        bricks[i].setInvisible();
                        contadorColor++;
                    }
                }
            }
        }

        // Ha impactado una bala de un invader a la nave espacial del jugador
        for (int i = 0; i < invadersBullets.length; i++) {
            if (invadersBullets[i].getStatus()) {
                if (RectF.intersects(playerShip.getRect(), invadersBullets[i].getRect())) {
                    invadersBullets[i].setInactive();
                    nextBullet--;
                    lives--;
                    // ¿Se acabó el juego?
                    if (lives == 0) {
                        gameOver();

                    }
                }
            }
        }

        // Ha impactado una bala de un Ambusher a la nave espacial del jugador
        for (int i = 0; i < ambusherBullets.length; i++) {
            if (ambusherBullets[i].getStatus()) {
                if (RectF.intersects(playerShip.getRect(), ambusherBullets[i].getRect())) {
                    ambusherBullets[i].setInactive();
                    lives--;
                    // ¿Se acabó el juego?
                    if (lives == 0) {
                        gameOver();
                    }
                }
            }
        }
        if (contadorColor > 1) {
            cambioColorAleatorio();
        } else if (contadorColor == 1) {
            cambioColorUnico();
        }

    }

    private void cambioColorUnico() {
        int color = (int) (Math.random() * 5);
        for (int i = 0; i < numInvaders; i++) {
            invaders[i].cambiarColor(hanCambiado, color);
        }
        if (hanCambiado)
            hanCambiado = false;
        else
            hanCambiado = true;
    }

    private void cambioColorAleatorio() {
        for (int i = 0; i < numInvaders; i++) {
            invaders[i].cambiarColor((int) (Math.random() * 5));
        }
        if (hanCambiado)
            hanCambiado = false;
        else
            hanCambiado = true;
    }

    private void win() {
        ambush.cancel();
        scoreGame.putExtra("mayor13", "true");
        scoreGame.putExtra("result", "YOU WON");
        scoreGame.putExtra("score", score);
        context.startActivity(scoreGame);

    }

    private void gameOver() {
        ambush.cancel();
        scoreGame.putExtra("mayor13", "true");
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
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), playerShip.getY(), paint);

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

            canvas.drawBitmap(dch, 150, screenY - 100, paint);
            canvas.drawBitmap(izq, 0, screenY - 100, paint);
            canvas.drawBitmap(arr, 75, screenY - 200, paint);
            canvas.drawBitmap(abj, 75, screenY - 100, paint);


            // Dibuja a los invaders
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY() + invaders[i].getHeight(), paint);

                }
            }

            //Dibuja los Ambushers, si son visibles
            for (int i = 0; i < numAmbusher; i++) {
                if (ambusher[i].getVisibility()) {
                    canvas.drawBitmap(ambusher[i].getBitmap(), ambusher[i].getX(), ambusher[i].getY(), paint);

                }
            }
            // Dibuja los ladrillos si están visibles
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }

            // Dibuja a las balas del jugador si están activas
            if (bullet.getStatus()) {
                canvas.drawRect(bullet.getRect(), paint);
            }


            // Actualiza todas las balas de los invaders si están activas
            for (int i = 0; i < invadersBullets.length; i++) {
                if (invadersBullets[i].getStatus()) {
                    canvas.drawRect(invadersBullets[i].getRect(), paint);
                }
            }

            //Lo mismo para Ambushers
            for (int i = 0; i < ambusherBullets.length; i++) {
                if (ambusherBullets[i].getStatus()) {
                    canvas.drawRect(ambusherBullets[i].getRect(), paint);
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
        ambush.cancel();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // Si SpaceInvadersActivity es iniciado entonces
    // inicia nuestra secuencia.
    public void resume() {
        ambush.start();
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

                if (x > 0 && x < 0 + izq.getWidth() && y > screenY - 90 && y < screenY - 100 + izq.getHeight()) {
                    //IZQ
                   // System.out.println("IZQ");
                    playerShip.setMovementState(playerShip.LEFT);
                } else if (x > 150 && x < 150 + izq.getWidth() && y > screenY - 100 && y < screenY - 90 + izq.getHeight()) {
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
                } else if (motionEvent.getY() < screenY - screenY / 8) {
                    if (bullet.shoot(playerShip.getX() +
                            playerShip.getLength() / 2, playerShip.getY(), bullet.UP)) ;

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