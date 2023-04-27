package com.example.toptop;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.toptop.model.User;


public class Funk {
    private static final String SHARED_PREF_NAME = "my_shared_pref";


    public static void set_token(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String get_token(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }

    public static void set_user(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("uid", user.getUid());
        editor.putString("username", user.getUsername());
        editor.putString("name", user.getName());
        editor.putString("avatar", user.getAvatar());
        editor.putBoolean("is_premium", user.isIs_premium());
        editor.putInt("videos", user.getVideos());
        editor.putInt("followers", user.getFollowers());
        editor.putInt("following", user.getFollowing());
        editor.putInt("likes", user.getLikes());
        editor.apply();
    }

    public static User get_user(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        User user = new User();
        user.setUid(sharedPreferences.getInt("uid", 0));
        user.setUsername(sharedPreferences.getString("username", ""));
        user.setName(sharedPreferences.getString("name", ""));
        user.setAvatar(sharedPreferences.getString("avatar", ""));
        user.setIs_premium(sharedPreferences.getBoolean("is_premium", false));
        user.setVideos(sharedPreferences.getInt("videos", 0));
        user.setFollowers(sharedPreferences.getInt("followers", 0));
        user.setFollowing(sharedPreferences.getInt("following", 0));
        user.setLikes(sharedPreferences.getInt("likes", 0));
        return user;
    }
}
