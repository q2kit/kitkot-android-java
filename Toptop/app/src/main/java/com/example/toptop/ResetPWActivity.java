package com.example.toptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPWActivity extends AppCompatActivity {

    Button get_code, submit, login, register;
    EditText email_or_phone, otp, new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        get_code = findViewById(R.id.btn_get_otp);
        submit = findViewById(R.id.submit);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        email_or_phone = findViewById(R.id.edit_account);
        otp = findViewById(R.id.edit_otp);
        new_password = findViewById(R.id.edit_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPWActivity.this, MainActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPWActivity.this, SignupActivity.class));
            }
        });

        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_or_phone_str = email_or_phone.getText().toString();
                if (email_or_phone_str.isEmpty()) {
                    email_or_phone.setError("Please enter your email or phone number");
                } else {
                    send_otp(email_or_phone_str);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_or_phone_str = email_or_phone.getText().toString();
                String otp_str = otp.getText().toString();
                String new_password_str = new_password.getText().toString();
                if (email_or_phone_str.isEmpty()) {
                    email_or_phone.setError("Please enter your email or phone number");
                } else if (otp_str.isEmpty()) {
                    otp.setError("Please enter your OTP");
                } else if (new_password_str.isEmpty()) {
                    new_password.setError("Please enter your new password");
                } else {
                    submit(email_or_phone_str, otp_str, new_password_str);
                }
            }
        });
    }

    private void send_otp(String email_or_phone_str) {
        String url = "https://soc.q2k.dev/api/reset-password/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(ResetPWActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ResetPWActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResetPWActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_or_phone", email_or_phone_str);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ResetPWActivity.this);
        requestQueue.add(request);
    }
    private void submit(String email_or_phone_str, String otp_str, String new_password_str) {
        String url = "https://soc.q2k.dev/api/reset-password/";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (jsonObject.getBoolean("success")) {
                        Toast.makeText(ResetPWActivity.this, message, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(ResetPWActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, 1500);
                    } else {
                        Toast.makeText(ResetPWActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ResetPWActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email_or_phone", email_or_phone_str);
                params.put("otp", otp_str);
                params.put("new_password", new_password_str);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ResetPWActivity.this);
        requestQueue.add(request);
    }
}