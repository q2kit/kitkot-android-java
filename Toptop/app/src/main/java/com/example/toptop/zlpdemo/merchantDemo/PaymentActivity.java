package com.example.merchantDemo.zlpdemo.merchantDemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.merchantDemo.R;
import com.example.merchantDemo.zlpdemo.merchantDemo.models.VipPackage;

import org.json.JSONObject;

import java.util.ArrayList;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {
    TextView lblZpTransToken, txtToken;
    Button btnCreateOrder, btnPay;
    EditText txtAmount;
    ArrayList<VipPackage> vipPackages;

    private void BindView() {
        txtToken = findViewById(R.id.txtToken);
        lblZpTransToken = findViewById(R.id.lblZpTransToken);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        btnPay = findViewById(R.id.btnPay);
        IsLoading();
    }

    private void IsLoading() {
        lblZpTransToken.setVisibility(View.INVISIBLE);
        txtToken.setVisibility(View.INVISIBLE);
//        btnPay.setVisibility(View.INVISIBLE);
    }

    private void IsDone() {
//        lblZpTransToken.setVisibility(View.VISIBLE);
//        txtToken.setVisibility(View.VISIBLE);
//        btnPay.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // bind components with ids
        BindView();
        getData();

        // handle CreateOrder
//        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onClick(View v) {
//                CreateOrder orderApi = new CreateOrder();
//
//                try {
//                    JSONObject data = orderApi.createOrder(txtAmount.getText().toString());
//                    Log.d("Amount", txtAmount.getText().toString());
//                    lblZpTransToken.setVisibility(View.VISIBLE);
//                    String code = data.getString("return_code");
//                    Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();
//
//                    if (code.equals("1")) {
//                        lblZpTransToken.setText("zptranstoken");
//                        txtToken.setText(data.getString("zp_trans_token"));
//                        IsDone();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("vipPackageID", "51f390d2-fb2c-4460-aa0a-081651526015");


                    // Building a request
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            // Using a variable for the domain is great for testing
                            String.format("%s/SuperUser/GetToken/", getString(R.string.nptinh_server_domain)),
                            jsonParams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                   try {
                                       System.out.println(response);
                                       pay(response.get("token").toString());
                                   }catch (Exception e){
                                       System.out.println(e);
                                   }
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error);
                                    // Handle the error

                                }
                            });


                    Volley.newRequestQueue(getApplicationContext()).
                            add(request);

                } catch (Exception e) {
                    System.out.println(e);
                }


            }
        });
    }

    private void pay(String token) {
        ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                System.out.println("onPaymentSucceededonPaymentSucceededonPaymentSucceededonPaymentSucceededonPaymentSucceededonPaymentSucceededonPaymentSucceededonPaymentSucceededonPaymentSucceeded");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(PaymentActivity.this)
                                .setTitle("Payment Success")
                                .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }

                });
                IsLoading();
            }

            @Override
            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                new AlertDialog.Builder(PaymentActivity.this)
                        .setTitle("User Cancel Payment")
                        .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }

            @Override
            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                new AlertDialog.Builder(PaymentActivity.this)
                        .setTitle("Payment Fail")
                        .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }
        });

    }

    private void getData() {
        getVipPackages();
    }

    private void getVipPackages() {
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;


        String url = String.format("%s/VipPackage/", getString(R.string.nptinh_server_domain));
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
