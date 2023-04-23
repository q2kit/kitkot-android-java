package com.example.toptop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.toptop.MainActivity;
import com.example.toptop.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText phone;
    SignInClient oneTapClient;
    BeginSignInRequest signUpRequest;
    EditText email;
    EditText name;
    EditText username;
    EditText password;
    Button signup;
    Button gg;
    TextView back_to_login;
    private static final int REQ_ONE_TAP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        phone = findViewById(R.id.edit_phone);
        email = findViewById(R.id.edit_email);
        name = findViewById(R.id.edit_name);
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        signup = findViewById(R.id.button);
        gg = findViewById(R.id.google_btn);
        back_to_login = findViewById(R.id.back_to_login);
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO()
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_str = phone.getText().toString();
                String email_str = email.getText().toString();
                String name_str = name.getText().toString();
                String username_str = username.getText().toString();
                String password_str = password.getText().toString();

                if (phone_str.isEmpty()) email.setError("Phone number is required");
                if (email_str.isEmpty()) email.setError("Email is required");
                if (name_str.isEmpty()) email.setError("Name is required");
                if (username_str.isEmpty()) email.setError("Username is required");
                if (password_str.isEmpty()) email.setError("Password is required");
                if (!phone_str.isEmpty() && !email_str.isEmpty() && !name_str.isEmpty() && !username_str.isEmpty() && !password_str.isEmpty()) {
                    String url = "https://soc.q2k.dev/api/register/";
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    response_processing(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //TODO()
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("phone", phone_str);
                            params.put("email", email_str);
                            params.put("name", name_str);
                            params.put("username", username_str);
                            params.put("password", password_str);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
                    requestQueue.add(request);
                }
            }
        });

        gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(SignupActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                try {
                                    startIntentSenderForResult(
                                            result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                            null, 0, 0, 0);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.e("TAG", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(SignupActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", e.getLocalizedMessage());
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        Log.d("TAG", "ID token: " + idToken);

                        String url = "https://soc.q2k.dev/api/google-auth/";
                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        response_processing(response);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("g_token", idToken);
                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                        queue.add(request);

                    }
                } catch (ApiException e) {
                }
                break;
        }
    }

    private void response_processing(String response) {
        try {
            JSONObject res = new JSONObject(response);
            if (res.getBoolean("success")) {
                String token = res.getString("token");
                JSONObject user = res.getJSONObject("user");
                int uid = user.getInt("uid");
                String username = user.getString("username");
                String email = user.getString("email");
                String phone = user.getString("phone");
                String avatar = user.getString("avatar");
                int followers = user.getInt("followers");
                int following = user.getInt("following");
                int liked = user.getInt("liked");

                // Lưu token vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", token);
                editor.putInt("uid", uid);
                editor.putString("username", username);
                editor.putString("email", email);
                editor.putString("phone", phone);
                editor.putString("avatar", avatar);
                editor.putInt("followers", followers);
                editor.putInt("following", following);
                editor.putInt("liked", liked);

                Intent homeIntent = new Intent(SignupActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            } else {
                String message = res.getString("message");
                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}