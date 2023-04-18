package com.example.toptop.chat;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatHandle {
    public List<ChatSection> exactListChatSection(JSONArray data){
        List<ChatSection> items = new ArrayList<>();
        for(int i = 0 ; i < data.length(); i++){
            try {
                JSONObject item = (JSONObject) data.get(i);
                Log.e("Data", item.getString("_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
}
