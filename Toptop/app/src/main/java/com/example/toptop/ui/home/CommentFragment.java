package com.example.toptop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.toptop.Funk;
import com.example.toptop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentFragment extends Fragment {
    int video_id;
    List<Comment> comments = new ArrayList<>();
    CommentAdapter commentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        getComments();
        return view;
    }

    public CommentFragment(int video_id) {
        this.video_id = video_id;
    }

    private void getComments(){
        String url = "https://soc.q2k.dev/api/"+video_id+"/comments/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject comment = jsonArray.getJSONObject(i);
                                Comment comment1 = new Comment(comment.getString("owner_avatar"), comment.getString("owner_name"), comment.getString("content"));
                                comments.add(comment1);
                            }
                            commentAdapter.setCommentItems(comments);
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
                params.put("Authorization", "Bearer " + Funk.get_token());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
