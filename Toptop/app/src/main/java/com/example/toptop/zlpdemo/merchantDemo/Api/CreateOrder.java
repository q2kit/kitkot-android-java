package com.example.merchantDemo.zlpdemo.merchantDemo.Api;

import com.example.merchantDemo.zlpdemo.merchantDemo.Constant.AppInfo;
import com.example.merchantDemo.zlpdemo.merchantDemo.Helper.Helpers;

import org.json.JSONObject;

import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CreateOrder {
    private class CreateOrderData {
        String AppId;
        String AppUser;
        String AppTime;
        String Amount;
        String AppTransId;
        String EmbedData;
        String Items;
        String BankCode;
        String Description;
        String Mac;

        private CreateOrderData(String amount) throws Exception {
            long appTime = new Date().getTime();
            AppId = String.valueOf(AppInfo.APP_ID);
            AppUser = "Android_Demo";
            AppTime = String.valueOf(appTime);
            Amount = amount;
            AppTransId = Helpers.getAppTransId();
            EmbedData = "{}";
            Items = "[]";
            BankCode = "zalopayapp";
            Description = "Merchant pay for order #" + Helpers.getAppTransId();
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    this.AppUser,
                    this.Amount,
                    this.AppTime,
                    this.EmbedData,
                    this.Items);

            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);
        }
    }

     public JSONObject createOrder(String amount) throws Exception {
        CreateOrderData input = new CreateOrderData(amount);

        RequestBody formBody = new FormBody.Builder()
                .add("app_id", input.AppId)
                .add("app_user", input.AppUser)
                .add("app_time", input.AppTime)
                .add("amount", input.Amount)
                .add("app_trans_id", input.AppTransId)
                .add("embed_data", input.EmbedData)
                .add("item", input.Items)
                .add("bank_code", input.BankCode)
                .add("description", input.Description)
                .add("mac", input.Mac)
                .build();

        JSONObject data = HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody);

//                 RequestBody formBody = new FormBody.Builder()
//                .add("orderCode", "123")
//                .build();
//         Request request = new Request.Builder()
//                 .url("http://10.20.247.77:44388/Order")
//                 .addHeader("Content-Type", "application/json")
//                 .post(formBody)
//                 .build();
//
//
//         OkHttpClient client = new OkHttpClient.Builder()
//                 .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
//                 .callTimeout(5000, TimeUnit.MILLISECONDS)
//                 .build();
//         Response response = client.newCall(request).execute();
//         JSONObject data = new JSONObject();
//
//         if (!response.isSuccessful()) {
//             Log.println(Log.ERROR, "BAD_REQUEST", response.body().string());
//             data = null;
//         } else {
//             data = new JSONObject(response.body().string());
//         }
        return data;
    }
}

