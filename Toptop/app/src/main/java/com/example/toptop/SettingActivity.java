package com.example.toptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.toptop.payment.PaymentActivity;
import com.example.toptop.phantich.PhanTichActivity;


public class SettingActivity extends AppCompatActivity {

    ImageView logout;
    ImageView premium,thongke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logout = findViewById(R.id.logout);
        premium = findViewById(R.id.premium);
        thongke = findViewById(R.id.thongke);
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

        thongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, PhanTichActivity.class));
            }
        });


    }

    void signOut(){
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
    }
}