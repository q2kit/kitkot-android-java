package com.example.toptop.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.toptop.HomeActivity;
import com.example.toptop.R;
import com.example.toptop.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoListFragment extends Fragment {

    ViewPager2 viewPager2;
    List<VideoItem> videoItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager2 = view.findViewById(R.id.videos_view_pager);
        videoItems = new ArrayList<>();

        VideoItem videoItemCelebration = new VideoItem();
        videoItemCelebration.videoUrl = "https://drive.vnsvs.net/f/f0130199-bf3f-4e46-8bc4-70293ecedc03.mp4";
        videoItemCelebration.videoDes = "hehe";
        videoItems.add(videoItemCelebration);

        VideoItem videoItemCelebration1 = new VideoItem();
        videoItemCelebration1.videoUrl = "https://drive.vnsvs.net/f/f0130199-bf3f-4e46-8bc4-70293ecedc03.mp4";
        videoItemCelebration1.videoDes = "hee";
        videoItems.add(videoItemCelebration1);

        VideoItem videoItemCelebration111 = new VideoItem();
        videoItemCelebration111.videoUrl = "https://drive.vnsvs.net/f/f0130199-bf3f-4e46-8bc4-70293ecedc03.mp4";
        videoItemCelebration111.videoDes = "hehe";
        videoItems.add(videoItemCelebration111);

        viewPager2.setAdapter(new VideoAdapter(videoItems));
        return view;
    }


//    private void getVideos() {
//        String url = "https://soc.q2k.dev/api/videos/";
//        JSONObject jsonBody = new JSONObject();
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//            new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.d("Volley !!!", response.toString());
//                    try {
//                        JSONArray jsonVideos = response.getJSONArray("videos");
//                        for (int i = 0; i < jsonVideos.length(); i++) {
//                            JSONObject video = jsonVideos.getJSONObject(i);
//                            int id = video.getInt("id");
//                            String description = video.getString("description");
//                            String link = video.getString("link");
//                            int owner_id = video.getInt("owner_id");
//                            String owner_name = video.getString("owner_name");
//                            String owner_avatar = video.getString("owner_avatar");
//                            boolean is_premium = video.getBoolean("is_premium");
//                            int watched = video.getInt("watched_count");
//                            int liked = video.getInt("liked");
//                            int comment = video.getInt("comment");
//                            boolean is_liked = video.getBoolean("is_liked");
//                            boolean is_followed = video.getBoolean("is_followed");
//                            videos.add(new Video(id, description, link, owner_id, owner_name, owner_avatar, is_premium, watched, liked, comment, is_liked, is_followed));
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        Log.e("Volley @@@", "Invalid JSON Object." + e.getMessage());
//                        throw new RuntimeException(e);
//                    }
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("Volley", error.toString());
//                }
//            }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjIsImV4cCI6MTY4NDc2MzQ1NCwiaWF0IjoxNjgyMTcxNDU0fQ.gwBfL1nxlVeNHuPUx7s5q75730F-ECk3uGtfIdXITts";
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        queue.add(request);
//    }
}
