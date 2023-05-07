package com.example.toptop.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.toptop.Funk;
import com.example.toptop.R;
import com.example.toptop.chat.ChatDetailFragment;
import com.example.toptop.chat.ChatMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileDialogFragment extends DialogFragment {

    private int uid;
    ImageView avatar, premium_icon;
    TextView name, username, videos, followers, following, likes;
    Button follow, btChat;
    String urlAvatar;

    FragmentManager fragmentManager;
    IProfile iProfile;
    public ProfileDialogFragment(int uid, IProfile iProfile) {
        this.uid = uid;
        this.iProfile = iProfile;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_profile, null);
        avatar = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.profile_name);
        premium_icon = view.findViewById(R.id.premium_icon);
        username = view.findViewById(R.id.profile_username);
        videos = view.findViewById(R.id.profile_videos);
        followers = view.findViewById(R.id.profile_followers);
        following = view.findViewById(R.id.profile_following);
        likes = view.findViewById(R.id.profile_likes);
        int userId = Funk.get_user(getContext()).getUid();
        follow = view.findViewById(R.id.btn_follow);
        btChat = view.findViewById(R.id.btChat);
        if(userId == uid){
            follow.setVisibility(View.INVISIBLE);
            btChat.setVisibility(View.INVISIBLE);
        }
        getProfile(uid);
        btChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uid > 0){
                    ChatMessage chatMessage = new ChatMessage(uid,"",
                            username.getText().toString(),
                            urlAvatar, "", false);
                    iProfile.clickChat(chatMessage);
                    dismiss();
                }
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().equals("Follow")) {
                    follow.setText("Unfollow");
                } else {
                    follow.setText("Follow");
                }
                toggleFollow(uid);
            }
        });
        builder.setView(view);
        return builder.create();
    }
    private void toggleFollow(int uid) {

    }
    private void getProfile(int uid) {
        String url = "https://soc.q2k.dev/api/" + uid + "/info";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user = response.getJSONObject("user");
                            urlAvatar = user.getString("avatar");
                            Glide.with(getActivity()).load(urlAvatar).into(avatar);
                            name.setText(user.getString("name"));
                            username.setText(user.getString("username"));
                            videos.setText(user.getString("videos"));
                            followers.setText(user.getString("followers"));
                            following.setText(user.getString("following"));
                            likes.setText(user.getString("likes"));
                            if (user.getBoolean("is_premium")) {
                                premium_icon.setVisibility(View.VISIBLE);
                            } else {
                                premium_icon.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + Funk.get_token(getContext()));
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }

    public interface IProfile{
         void clickChat(ChatMessage chatMessage);
    }
}