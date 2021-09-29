package com.itbooth.mobility.ludodice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class SplashActivity extends AppCompatActivity {
    KonfettiView viewKonfetti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewKonfetti = findViewById(R.id.viewConfettiWrapper);
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, VideoActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, 3000);
        */
        startConfetti();
    }

    public void startConfetti(){
        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, display.widthPixels + 50f, -50f, -50f)
                .streamFor(300, 1500L);
    }
}