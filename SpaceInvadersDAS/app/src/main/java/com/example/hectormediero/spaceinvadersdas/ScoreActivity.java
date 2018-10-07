package com.example.hectormediero.spaceinvadersdas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        String result=getIntent().getExtras().getString("result");
        int score =getIntent().getExtras().getInt("score");
        TextView resultTV = findViewById(R.id.GameResult);
        TextView scoreTV = findViewById(R.id.GameScore);
        resultTV.setText(result);
        scoreTV.setText(Integer.toString(score));
    }
    @Override
    public void onBackPressed (){
        System.out.println("YO YA NO QUIERO NA");
    }
}
