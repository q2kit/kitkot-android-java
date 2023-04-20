package com.example.merchantDemo.chat;

import android.util.Log;

import com.example.merchantDemo.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatHandle {
    public List<ChatMessage> exactListChatSection(JSONArray data){
        List<ChatMessage> items = new ArrayList<>();

        // fetch to get avatar, username

        for(int i = 0 ; i < data.length(); i++){
            try {
                JSONObject item = (JSONObject) data.get(i);
                int id = Integer.parseInt(item.getString("_id"));
                String lastMessage = item.getString("last_message");
                String sendTime = Util.convertIntToTime(Long.parseLong(item.getString("send_time")));
                int userIdSend = Integer.parseInt(item.getString("user_id_send"));

                ChatMessage  section = new ChatMessage(
                        id,
                        lastMessage,
                        "User "+ id,
                        "https://znews-photo.zingcdn.me/w660/Uploaded/ofh_btgazspf/2019_10_20/1_1.jpg",
                        sendTime,
                        userIdSend == 1
                );

                Log.e("Data", item.getString("_id"));
                items.add(section);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }


    public List<Message> exactListMessage(JSONArray data){
        List<Message> items = new ArrayList<>();

        // fetch to get avatar, username

        for(int i = 0 ; i < data.length(); i++){
            try {
                JSONObject item = (JSONObject) data.get(i);
                String text = item.getString("message");
                String sendTime = Util.convertIntToTime(Long.parseLong(item.getString("send_time")));
                int userIdSend = Integer.parseInt(item.getString("user_id_send"));
                int userIdReceive = Integer.parseInt(item.getString("user_id_receive"));

                Message message = new Message(
                        text,
                        userIdSend,
                        userIdReceive,
                        sendTime
                );

                Log.e("Message", message.toString());
                message.setIncoming(userIdSend != 1);
                items.add(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }


    public Message exactMessage(JSONObject item){
        try {
            String text = item.getString("message");
            String sendTime = Util.convertIntToTime(Long.parseLong(item.getString("send_time")));
            int userIdSend = Integer.parseInt(item.getString("user_id_send"));
            int userIdReceive = Integer.parseInt(item.getString("user_id_receive"));

            Message message = new Message(
                    text,
                    userIdSend,
                    userIdReceive,
                    sendTime
            );

            Log.e("Message", userIdSend+"");
            message.setIncoming(userIdSend != 1);
            return message;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
