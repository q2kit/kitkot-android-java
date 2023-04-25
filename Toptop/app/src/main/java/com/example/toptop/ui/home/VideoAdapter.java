package com.example.toptop.ui.home;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.R;
import com.example.toptop.model.Video;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private List<Video> videos;
    Context context;
    private RecyclerView recyclerView;

    public VideoAdapter(List<Video> videos) {
        this.videos = videos;
        this.context = context;
        Log.d("KITKOT", "Loaded " + videos.size() + " videos to adapter");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_video, parent, false);
        Log.d("KITKOT", "onCreateViewHolder called");
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.bind(video);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private VideoView video;
        private CircleImageView profileImage;
        private TextView likedCount, commentCount, username, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.video);
            profileImage = itemView.findViewById(R.id.profile_image);
            likedCount = itemView.findViewById(R.id.likedCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
        }

        public void bind(Video item) {
            video.setVideoPath(item.getLink());
            Glide.with(itemView.getContext())
                    .load(item.getOwner_avatar())
                    .into(profileImage);
            likedCount.setText(String.valueOf(item.getLiked()));
            commentCount.setText(String.valueOf(item.getComment()));
            username.setText(item.getOwner_name());
            description.setText(item.getDescription());
        }
    }
}
