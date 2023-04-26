package com.example.toptop.notification;

import android.util.Log;

import com.example.toptop.chat.ChatMessage;
import com.example.toptop.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationHandle {
    final  int  NOTI_VIDEO_LIKE = 4;
    public String getContent(int type){
        switch (type){
            case NOTI_VIDEO_LIKE:
                return "like your video";
        }
        return "";
    }
    public List<Notification> exactListNoti(JSONArray data){
        List<Notification> notis = new ArrayList<>();

        // fetch to get avatar, username

        for(int i = 0 ; i < data.length(); i++){
            try {
                JSONObject item = (JSONObject) data.get(i);
                String id = item.getString("_id");
                int type = Integer.parseInt(item.getString("type"));
                String sendTime = Util.convertIntToTime(Long.parseLong(item.getString("send_time")));
                String username =item.getString("username");
                String avatar =item.getString("avatar");
                String content = getContent(type);

                Notification  noti = new Notification(
                        id,
                        username,
                        avatar,
                        content,
                        sendTime
                );
                Log.e("Exact noti", noti.toString());
                notis.add(noti);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return notis;
    }
}
