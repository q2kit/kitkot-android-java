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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.toptop.chat.ChatActivity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.core.splashscreen.SplashScreen;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    SignInClient oneTapClient;
    BeginSignInRequest signUpRequest;
    Button btnGG;
    Button bt_Chat;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGG = findViewById(R.id.google_btn);
        bt_Chat = findViewById(R.id.bt_Chat);
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

        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

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
                        Log.d("TAG", "ID token: " + idToken);

                        String url = "https://soc.q2k.dev/api/google-auth/";
                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Xử lý phản hồi từ API ở đây
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
                                                int likes = user.getInt("likes");

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
                                                editor.putInt("likes", likes);
                                            } else {
                                                throw new RuntimeException(res.getString("message"));
                                            }
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }

                                        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(homeIntent);
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
                        // Thêm request vào hàng đợi của Volley
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(request);

                    }
                } catch (ApiException e) {
                }
                break;
        }
    }
}