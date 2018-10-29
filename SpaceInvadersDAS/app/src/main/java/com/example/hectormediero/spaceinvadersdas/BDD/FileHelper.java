package com.example.hectormediero.spaceinvadersdas.BDD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

public class FileHelper {
	public static int INTERNO = 0;
	public static int SD = 1;

	public static void guardarEnFichero(int tipo, Activity act,
			String nFichero, String texto) throws FileNotFoundException,
			IOException {
		File f = null;
		FileOutputStream fos = null;
		try {
			if (tipo == SD) {
				File ruta_sd = Environment.getExternalStorageDirectory();
				f = new File(ruta_sd.getAbsolutePath(), nFichero);
				fos = new FileOutputStream(f);
			} else if (tipo == INTERNO) {
				fos = act.openFileOutput(nFichero, Context.MODE_PRIVATE);
			}
			OutputStreamWriter fout = new OutputStreamWriter(fos);
			fout.write(texto);
			fout.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}

	}

	public static String leerDeFichero(int tipo, Activity act, String nFichero)
			throws FileNotFoundException, IOException {
		String texto = null;
		File f = null;
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			if (tipo == SD) {
				File ruta_sd = Environment.getExternalStorageDirectory();
				f = new File(ruta_sd.getAbsolutePath(), nFichero);
				fis = new FileInputStream(f);
			} else if (tipo == INTERNO) {
				fis = act.openFileInput(nFichero);
			}
			br = new BufferedReader(new InputStreamReader(fis));
			texto = br.readLine();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			br.close();
			fis.close();
		}

		return texto;
	}
	
	
}
