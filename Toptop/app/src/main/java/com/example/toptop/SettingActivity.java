package com.example.toptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.toptop.payment.PaymentActivity;
import com.example.toptop.phantich.PhanTichActivity;
import com.example.toptop.ui.me.MeFragment;


public class SettingActivity extends AppCompatActivity {

    ImageView logout, manage, back;
    ImageView premium,thongke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logout = findViewById(R.id.logout);
        premium = findViewById(R.id.premium);
        thongke = findViewById(R.id.thongke);
        manage = findViewById(R.id.manager_acc);
        back = findViewById(R.id.back);

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

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, ManageAccountActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, MeFragment.class));
            }
        });


    }

    void signOut(){
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
    }
}