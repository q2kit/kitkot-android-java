package com.example.toptop.payment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.toptop.Funk;
import com.example.toptop.HomeActivity;
import com.example.toptop.R;
import com.example.toptop.payment.models.VipPackage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {
    Button  btnPay;
    TextView txtExpireDate,txtTitle;
    ArrayList<VipPackage> vipPackages;
    RadioGroup rdgVipPackage;
    int userID =2;

    private VipPackage selectedVipPackage;

    private void BindView() {

        btnPay = findViewById(R.id.btnPay);
        rdgVipPackage =  findViewById(R.id.rdgVipPackage);
        txtExpireDate =  findViewById(R.id.txtExpireDate);
        txtTitle =  findViewById(R.id.txtTitle);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        userID = Funk.get_user(this).getUid();
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // bind components with ids
        BindView();
        customUIContent();
        getData();
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonParams = new JSONObject();
                    VipPackage registedVipPackage = selectedVipPackage;
                    jsonParams.put("VipPackageID", registedVipPackage.getVipPackageID());
                    // Building a request
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            String.format("%s/SuperUser/GetToken/", getString(R.string.nptinh_server_domain)),
                            jsonParams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                   try {
                                       System.out.println(response);
                                       pay(response.get("token").toString(), registedVipPackage);
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
                                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!" , Toast.LENGTH_LONG).show();//display the response on screen


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

    @Override
    protected void onResume() {
        super.onResume();
        customUIContent();
    }

    private void customUIContent() {
        btnPay.setVisibility(View.INVISIBLE);
        rdgVipPackage.setVisibility(View.INVISIBLE);
        txtExpireDate.setVisibility(View.INVISIBLE);

        JSONObject jsonParams = new JSONObject();

        // Building a request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                String.format("%s/SuperUser/"+userID, getString(R.string.nptinh_server_domain)),
                jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            String exp = response.getJSONObject("Data").getString("ExpPremiumDate");
                            if(!exp.equals("null")){
                                LocalDate date = LocalDate.parse(exp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                                LocalDate now = LocalDate.now();

                                if (date.isBefore(now)) {
                                    txtTitle.setText("Đăng ký thành viên Premium");
                                    btnPay.setVisibility(View.VISIBLE);
                                    rdgVipPackage.setVisibility(View.VISIBLE);
                                } else if (date.isAfter(now)) {
                                    txtExpireDate.setVisibility(View.VISIBLE);

                                    txtExpireDate.setText("Ngày hết hạn: "+exp);
                                    txtTitle.setText("THÔNG TIN PREMIUM");
                                }
                            }else{
                                txtTitle.setText("Đăng ký thành viên Premium");
                                btnPay.setVisibility(View.VISIBLE);
                                rdgVipPackage.setVisibility(View.VISIBLE);
                            }


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
                        Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!" , Toast.LENGTH_LONG).show();//display the response on screen
                    }
                });
        Volley.newRequestQueue(getApplicationContext()).
                add(request);

    }

    private void pay(String token, VipPackage registedVipPackage) {
        ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
            @Override
            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                try {
                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("ID", userID);
                    jsonParams.put("VipPackageID", registedVipPackage.getVipPackageID());
                    // Building a request
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            String.format("%s/SuperUser/Register/", getString(R.string.nptinh_server_domain)),
                            jsonParams,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        System.out.println(response);
                                        Toast.makeText(getApplicationContext(), "Đăng ký thành công!" , Toast.LENGTH_LONG).show();//display the response on screen
                                        startActivity(new Intent(PaymentActivity.this, HomeActivity.class));

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
                                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!" , Toast.LENGTH_LONG).show();//display the response on screen
                                }
                            });
                    Volley.newRequestQueue(getApplicationContext()).
                            add(request);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onPaymentCanceled(String zpTransToken, String appTransID) {
              
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
        String url = String.format("%s/VipPackage/", getString(R.string.nptinh_server_domain));
        JsonArrayRequest req= new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                vipPackages = new ArrayList<>();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        VipPackage msg = new VipPackage();
                        msg.setVipPackageID(obj.getString("vipPackageID"));
                        msg.setVipPackageName(obj.getString("vipPackageName"));
                        msg.setMonthDuration(obj.getInt("monthDuration"));
                        msg.setPrice(obj.getInt("price"));
                        vipPackages.add(msg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if(vipPackages.size()>0){
                    selectedVipPackage = vipPackages.get(0);
                }

                //Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

                for (VipPackage option : vipPackages) {
                    RadioButton radioButton = new RadioButton(getApplicationContext());
                    radioButton.setText(option.getVipPackageName()+"("+option.getPrice()+"đ)");
                    radioButton.setTag(option);
                    rdgVipPackage.addView(radioButton);
                }
                RadioButton radioButton = (RadioButton) rdgVipPackage.getChildAt(0);
                radioButton.setChecked(true);

                rdgVipPackage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = group.findViewById(checkedId);
                        selectedVipPackage= (VipPackage) radioButton.getTag();
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                // hidePDialog();
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!" , Toast.LENGTH_LONG).show();//display the response on screen


            }
        });
        Volley.newRequestQueue(getApplicationContext()).
                add(req);

        // RequestQueue initialized

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
