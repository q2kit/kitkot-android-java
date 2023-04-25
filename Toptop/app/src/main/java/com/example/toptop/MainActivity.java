package com.example.toptop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.example.toptop.chat.ChatActivity;
import com.example.toptop.firebase.Firebase;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SignInClient oneTapClient;
    BeginSignInRequest signUpRequest;
    Button btnGG;
    Button login;
    Button bt_Chat;
    TextView resetPW;
    EditText edit_account, edit_password;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGG = findViewById(R.id.google_btn);
        bt_Chat = findViewById(R.id.bt_Chat);
        login = findViewById(R.id.button);
        resetPW = findViewById(R.id.resetPW);
        edit_account = findViewById(R.id.edit_account);
        edit_password = findViewById(R.id.edit_password);
        Firebase firebase = new Firebase();
        firebase.getNumberNotification(bt_Chat);
        bt_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(switchActivityIntent);
            }
        });
        //press signup
        final TextView txtLogin = findViewById(R.id.textView7);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO()
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edit_account.getText().toString();
                String password = edit_password.getText().toString();
                String url = "https://soc.q2k.dev/api/login/";

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
                            // Xử lý lỗi ở đây
                            Log.d("TAG", error.toString());
                        }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("account", account);
                        params.put("password", password);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(request);
            }
        });

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        resetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(MainActivity.this, ResetPWActivity.class);
                startActivity(switchActivityIntent);
            }
        });

        btnGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<BeginSignInResult>() {
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
                        .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No Google Accounts found. Just continue presenting the signed-out UI.
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
                                        // Xử lý lỗi kết nối hoặc lỗi phản hồi từ API ở đây
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("g_token", idToken);
                                return params;
                            }
                        };
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(request);
                    }
                } catch (ApiException e) {
                }
                break;
        }
    }
    protected void response_processing(String response) {
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

                Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            } else {
                String message = res.getString("message");
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}