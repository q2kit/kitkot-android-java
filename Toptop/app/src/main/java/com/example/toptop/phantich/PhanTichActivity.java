package com.example.toptop.phantich;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.toptop.Funk;
import com.example.toptop.R;
import com.example.toptop.payment.models.VipPackage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PhanTichActivity extends AppCompatActivity {

    ArrayList barArraylist;
    BarChart barChart, barChartLike;
    private EditText eStartDate, eEndDate;
    Button btnPhanTich;
    private int userId=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phantich);
        userId = Funk.get_user(this).getUid();
        barChart = findViewById(R.id.barchart);
        barChartLike = findViewById(R.id.barchartLike);
        getData();

        initDatePicker();

        btnPhanTich = findViewById(R.id.btnPhantich);
        btnPhanTich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

    }
    private void initDatePicker() {
        eStartDate = findViewById(R.id.eStartDate);
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int mounth = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Context me = this;
        eStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(me, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String month =""+(m+1), day = ""+d;
                        if(m<10) {
                            month="0"+month;
                        }
                        if(d<10){
                            day = "0" +d;
                        }
                        eStartDate.setText(y + "-" + month + "-" + day);
                    }
                }, year, mounth, day);
                datePickerDialog.show();
            }
        });

        eEndDate = findViewById(R.id.eEndDate);
        eEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(me, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String month =""+(m+1), day = ""+d;
                        if(m<10) {
                            month="0"+month;
                        }
                        if(d<10){
                            day = "0" +d;
                        }
                        eEndDate.setText(y + "-" + month + "-" + day);
                    }
                }, year, mounth, day);
                datePickerDialog.show();
            }
        });

    }


    private void getData() {
        try {


            JSONObject jsonParams = new JSONObject();
            jsonParams.put("userID", userId);
            jsonParams.put("startDate", eStartDate.getText());
            jsonParams.put("endDate", eEndDate.getText());
            jsonParams.put("typeDate", 1);
            // Building a request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    String.format("%s/SuperUser/ThongKeLuotThichTheoThoiGian/", getString(R.string.nptinh_server_domain)),
                    jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Convert the "KetQua" value to a HashMap
                                HashMap<String, Integer> ketQuaMap = new HashMap<>();
                                JSONObject ketQuaJsonObject;
                                try {
                                    ketQuaJsonObject = response.getJSONObject("Data").getJSONObject("KetQua");
                                } catch (JSONException e) {
                                    return;
                                }
                                Iterator<?> keys = ketQuaJsonObject.keys();
                                while (keys.hasNext()) {
                                    String key = (String) keys.next();
                                    try {
                                        int value = ketQuaJsonObject.getInt(key);
                                        ketQuaMap.put(key, value);
                                    } catch (JSONException e) {
                                        System.out.println(e);
                                    }
                                }

                                ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
                                ArrayList<String> labelNames = new ArrayList<>();
                                int count = 0;
                                for (String i : ketQuaMap.keySet()) {
                                    System.out.println("key: " + i + " value: " + ketQuaMap.get(i));
                                    barEntryArrayList.add(new BarEntry(count, ketQuaMap.get(i)));
                                    labelNames.add(i);
                                    count++;

                                }
                                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Thống kê lượt thích theo thời gian");
                                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                Description desc = new Description();
                                desc.setText("Months");
                                barChart.setDescription(desc);
                                BarData barData = new BarData(barDataSet);
                                barChart.setData(barData);

                                XAxis xAxis = barChart.getXAxis();
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
                                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                xAxis.setDrawGridLines(false);
                                xAxis.setDrawAxisLine(false);
                                xAxis.setGranularity(1f);
                                xAxis.setLabelCount(labelNames.size());
                                xAxis.setLabelRotationAngle(270);
                                barChart.animateY(2000);
                                barChart.invalidate();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            // Handle the error
                            Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();//display the response on screen


                        }
                    });
            Volley.newRequestQueue(getApplicationContext()).
                    add(request);
            // Building a request
            JsonObjectRequest request2 = new JsonObjectRequest(
                    Request.Method.POST,
                    String.format("%s/SuperUser/ThongKeLuotThichTheoVideo/", getString(R.string.nptinh_server_domain)),
                    jsonParams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Convert the "KetQua" value to a HashMap
                                HashMap<String, Integer> ketQuaMap = new HashMap<>();
                                JSONObject ketQuaJsonObject;
                                try {
                                    ketQuaJsonObject = response.getJSONObject("Data").getJSONObject("KetQua");
                                } catch (JSONException e) {
                                    return;
                                }
                                Iterator<?> keys = ketQuaJsonObject.keys();
                                while (keys.hasNext()) {
                                    String key = (String) keys.next();
                                    try {
                                        int value = ketQuaJsonObject.getInt(key);
                                        ketQuaMap.put(key, value);
                                    } catch (JSONException e) {
                                        System.out.println(e);
                                    }
                                }

                                ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
                                ArrayList<String> labelNames = new ArrayList<>();
                                int count = 0;
                                for (String i : ketQuaMap.keySet()) {
                                    System.out.println("key: " + i + " value: " + ketQuaMap.get(i));
                                    barEntryArrayList.add(new BarEntry(count, ketQuaMap.get(i)));
                                    labelNames.add(i);
                                    count++;

                                }
                                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Thống kê lượt thích theo video");
                                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                Description desc = new Description();
                                desc.setText("Tên video");
                                barChartLike.setDescription(desc);
                                BarData barData = new BarData(barDataSet);
                                barChartLike.setData(barData);

                                XAxis xAxis = barChartLike.getXAxis();
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelNames));
                                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                xAxis.setDrawGridLines(false);
                                xAxis.setDrawAxisLine(false);
                                xAxis.setGranularity(1f);
                                xAxis.setLabelCount(labelNames.size());
                                xAxis.setLabelRotationAngle(270);
                                barChartLike.animateY(2000);
                                barChartLike.invalidate();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            // Handle the error
                            Toast.makeText(getApplicationContext(), "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();//display the response on screen


                        }
                    });
            Volley.newRequestQueue(getApplicationContext()).
                    add(request2);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
