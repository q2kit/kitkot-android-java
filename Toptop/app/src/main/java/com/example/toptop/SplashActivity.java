package com.example.toptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = Funk.get_token(SplashActivity.this);

                if(token.isEmpty()){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
            }
        }, 3000);

    }
}