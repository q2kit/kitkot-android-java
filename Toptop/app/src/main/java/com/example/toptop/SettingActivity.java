package com.example.toptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.toptop.payment.PaymentActivity;


public class SettingActivity extends AppCompatActivity {

    ImageView logout;
    ImageView premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logout = findViewById(R.id.logout);
        premium = findViewById(R.id.premium);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, PaymentActivity.class));
            }
        });
    }

    void signOut(){
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
    }
}