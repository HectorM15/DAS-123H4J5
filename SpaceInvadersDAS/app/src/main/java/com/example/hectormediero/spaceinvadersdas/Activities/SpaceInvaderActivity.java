package com.example.hectormediero.spaceinvadersdas.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Display;
import android.widget.Toast;

import com.example.hectormediero.spaceinvadersdas.Views.SpaceInvadersView;
import com.example.hectormediero.spaceinvadersdas.Views.SpaceInvadersView13;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    Boolean rebotes;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mayor13=getIntent().getExtras().getString("mayor13");
        username=getIntent().getExtras().getString("username");
        rebotes= getIntent().getExtras().getBoolean("rebotes");
        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);
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
    protected void onDestroy() {
        super.onDestroy();
        lanzarIntentCamara();
    }

    public void lanzarIntentCamara(){
        dispatchTakePictureIntent("");
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void dispatchTakePictureIntent(String nombre) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
        galleryAddPic();
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = username;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = storageDir.getPath();
        return image;
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
