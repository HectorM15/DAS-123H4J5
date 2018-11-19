package com.example.hectormediero.spaceinvadersdas.Models;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.example.hectormediero.spaceinvadersdas.Activities.SpaceInvaderActivity;
import com.example.hectormediero.spaceinvadersdas.R;

import java.util.ArrayList;

public class Music {
    private ArrayList<MediaPlayer> mpList;
    private int nCancion;
    private int totalCanciones;

    CountDownTimer cdt = new CountDownTimer(20000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            mpList.get(nCancion).stop();
            mpList.get(nCancion).release();
            nCancion = (int) Math.random() * totalCanciones;
            iniciarMusica();
        }
    };

    public Music(Context context) {
        mpList = new ArrayList<MediaPlayer>();

        mpList.add(MediaPlayer.create(context,R.raw.back_to_the_future));
        mpList.add(MediaPlayer.create(context,R.raw.digimon));
        mpList.add(MediaPlayer.create(context,R.raw.dragon_ball));
        mpList.add(MediaPlayer.create(context,R.raw.evil_mortys));
        mpList.add(MediaPlayer.create(context,R.raw.final_fantasy_v));
        mpList.add(MediaPlayer.create(context,R.raw.final_fantasy_vii));
        mpList.add(MediaPlayer.create(context,R.raw.final_fantasy_viii));
        mpList.add(MediaPlayer.create(context,R.raw.indiana_jones));
        mpList.add(MediaPlayer.create(context,R.raw.inter));
        mpList.add(MediaPlayer.create(context,R.raw.jurassic_park));
        mpList.add(MediaPlayer.create(context,R.raw.lord_of_the_rings));
        mpList.add(MediaPlayer.create(context,R.raw.never_gonna_give_you_up));
        mpList.add(MediaPlayer.create(context,R.raw.pokemon));
        mpList.add(MediaPlayer.create(context,R.raw.radioactive));
        mpList.add(MediaPlayer.create(context,R.raw.rasputin));
        mpList.add(MediaPlayer.create(context,R.raw.soviet_union_anthem));
        mpList.add(MediaPlayer.create(context,R.raw.thomas_the_tank_engine));
        mpList.add(MediaPlayer.create(context,R.raw.wake_me_up));
        mpList.add(MediaPlayer.create(context,R.raw.what_is_love));

        totalCanciones = this.mpList.size();
        nCancion = (int) Math.random() * totalCanciones;
    }

    public void iniciarMusica(){
        mpList.get(nCancion).start();
        cdt.start();
    }

    public void pararMusica(){
        mpList.get(nCancion).stop();
        cdt.cancel();
    }

}