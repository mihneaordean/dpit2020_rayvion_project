package com.example.dpit2020navem.Intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.dpit2020navem.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomePageTimer();

    }

    public void onBackPressed() {
        finish();
        System.exit(0);
    }


    private void welcomePageTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent= new Intent(WelcomeActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        },2300);
    }
}