package com.example.toptop.ui.home;


import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    List<Video> videoItems;

    public VideoAdapter(List<Video> videoItems){
        this.videoItems = videoItems;
    }

    public void setVideoItems(List<Video> videoItems){
        this.videoItems = videoItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_video, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {


        VideoView videoView;
        CircleImageView profileImage;
        TextView likedCount, commentCount, username, description;
        ProgressBar progressBar;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video);
            profileImage = itemView.findViewById(R.id.profile_image);
            likedCount = itemView.findViewById(R.id.likedCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        void setVideoData(Video videoItem){
            videoView.setVideoPath(videoItem.getLink());
            Glide.with(itemView.getContext())
                    .load(videoItem.getOwner_avatar())
                    .into(profileImage);
            likedCount.setText(String.valueOf(videoItem.getLiked()));
            commentCount.setText(String.valueOf(videoItem.getComment()));
            username.setText(videoItem.getOwner_name());
            description.setText(videoItem.getDescription());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    mp.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        }
    }
}
