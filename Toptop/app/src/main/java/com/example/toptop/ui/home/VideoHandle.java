package com.example.toptop.ui.home;

import android.util.Log;

import com.example.toptop.chat.ChatMessage;
import com.example.toptop.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoHandle {
    public Video exactVideoInfo(JSONObject item){
        Video video;
        try {
            int id = Integer.parseInt(item.getString("video_id"));
            int likeNumber = Integer.parseInt(item.getString("like_num"));
            int commentNumber = Integer.parseInt(item.getString("comment_num"));
            boolean isLiked = Boolean.parseBoolean(item.getString("is_liked"));
            video = new Video(id, likeNumber, commentNumber, isLiked);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return video;
    }
}
