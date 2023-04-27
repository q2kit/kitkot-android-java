package com.example.toptop.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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

public class CommentDialogFragment extends DialogFragment {

    private int video_id;
    private int owner_id;
    private List<Comment> comments;

    public CommentDialogFragment(int video_id, int owner_id) {
        this.video_id = video_id;
        this.owner_id = owner_id;

    }
    CommentAdapter adapter;
    public void changeComments(List<Comment> comments){
        Log.e("Change comment", "OK");
        this.comments = comments;
        adapter.setCommentItems(comments);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        Log.d("QUAN", "HEREEEEEEEEEEEEEE");
        // Inflate the layout for this dialog
        View view = inflater.inflate(R.layout.fragment_comment, null);

        // Set up the comment view and adapter
        RecyclerView recyclerView = view.findViewById(R.id.comment_list);
        comments = new ArrayList<Comment>();
        adapter = new CommentAdapter(comments);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Add a comment form and submit button
        EditText commentInput = view.findViewById(R.id.comment_input);
        Button submitButton = view.findViewById(R.id.comment_send);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = commentInput.getText().toString();
                Socket socket = SocketRoot.getInstance();
                JSONObject data = new JSONObject();
                try {
                    data.put("video_id", video_id);
                    data.put("content", content);
                    data.put("owner_id", owner_id);
                    socket.emit("comment", data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        builder.setView(view);

        return builder.create();
    }
    private void getComments(){
        String url = "https://soc.q2k.dev/api/"+video_id+"/comments/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("comments");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject comment = jsonArray.getJSONObject(i);
                                Comment comment1 = new Comment(comment.getString("owner_avatar"), comment.getString("owner_name"), comment.getString("content"));
                                comments.add(comment1);
                            }
                            adapter.setCommentItems(comments);
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

    private void postComment(String content, int video_id) {
        String url = "https://soc.q2k.dev/api/post-comment/";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("QUAN", error.toString());
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

    public void addComments(List<Comment> comments) {
        this.comments.add(comments.get(0));
        adapter.setCommentItems(this.comments);
    }
}
