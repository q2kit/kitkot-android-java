package com.example.toptop.ui.home;

import android.util.Log;

import com.example.toptop.chat.ChatMessage;
import com.example.toptop.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentHandle {
    public List<Comment> exactListComment(JSONArray data){
        List<Comment> items = new ArrayList<>();

        // fetch to get avatar, username

        for(int i = 0 ; i < data.length(); i++){
            try {
                JSONObject item = (JSONObject) data.get(i);
                String content = item.getString("content");
                String username =item.getString("username");
                String avatar =item.getString("avatar");

                Comment comment = new Comment(avatar,username, content );

                Log.e("Data", item.getString("_id"));
                items.add(comment);
                Log.e("comment list", comment.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
}
