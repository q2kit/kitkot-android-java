package com.example.toptop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.toptop.Funk;
import com.example.toptop.R;
import com.example.toptop.socket.SocketRoot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;


public class VideoListFragment extends Fragment {

    ViewPager2 viewPager2;
    List<Video> videoItems = new ArrayList<>();
    VideoAdapter videoAdapter;
    static ProfileDialogFragment.IProfile iProfile;

    public VideoListFragment(ProfileDialogFragment.IProfile iProfile) {
        this.iProfile = iProfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getVideos();
        viewPager2 = view.findViewById(R.id.videos_view_pager);
        videoAdapter = new VideoAdapter(videoItems, getChildFragmentManager(), iProfile);
        viewPager2.setAdapter(videoAdapter);
        return view;
    }

    public void updateComments(List<Comment> comments){

        videoAdapter.setComments(comments);
    }

    public void updateVideo(Video video){
        for(Video v: videoItems){
            if(v.getId() == video.getId()){
                v.setLiked(video.getLiked());
                v.setComment(video.getComment());
                v.setIs_liked(video.isIs_liked());
                break;
            }
        }
        videoAdapter.setVideoItems(videoItems);
    }
    private void getVideos() {
        videoItems.clear();
        String url = "https://soc.q2k.dev/api/videos/";
        JSONObject jsonBody = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonVideos = response.getJSONArray("videos");
                        for (int i = 0; i < jsonVideos.length(); i++) {
                            JSONObject video = jsonVideos.getJSONObject(i);
                            int id = video.getInt("id");
                            String description = video.getString("description");
                            String link = video.getString("link");
                            int owner_id = video.getInt("owner_id");
                            String owner_name = video.getString("owner_name");
                            String owner_avatar = video.getString("owner_avatar");
                            boolean is_premium = video.getBoolean("is_premium");
                            int watched = video.getInt("watched_count");
                            int liked = video.getInt("liked");
                            int comment = video.getInt("comment");
                            boolean is_liked = video.getBoolean("is_liked");
                            boolean is_followed = video.getBoolean("is_followed");
                            Socket socket = SocketRoot.getInstance();
                            JSONObject data = new JSONObject();
                            data.put("video_id", id);
                            socket.emit("video",data);
                            videoItems.add(new Video(id, description, link, owner_id, owner_name, owner_avatar, is_premium, watched, liked, comment, is_liked, is_followed));
                        }
                        videoAdapter.setVideoItems(videoItems);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                String token = Funk.get_token();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    public void addComments(List<Comment> comments) {
        videoAdapter.addComments(comments);
    }
}
