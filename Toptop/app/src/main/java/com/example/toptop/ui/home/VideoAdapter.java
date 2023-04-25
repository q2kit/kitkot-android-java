package com.example.toptop.ui.home;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
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
import com.example.toptop.model.Video;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    List<VideoItem> videoItems;

    public VideoAdapter(List<VideoItem> videoItems){
        this.videoItems = videoItems;
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
        TextView textVideoDes;
        ProgressBar videoProgressBar;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.video);
            textVideoDes = itemView.findViewById(R.id.description);
            videoProgressBar = itemView.findViewById(R.id.progressBar);

        }

        void setVideoData(VideoItem videoItem){
            textVideoDes.setText(videoItem.videoDes);
            videoView.setVideoPath(videoItem.videoUrl);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoProgressBar.setVisibility(View.GONE);
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / mp.getVideoHeight();
                    float screenRadio = videoView.getWidth() /mp.getVideoHeight();

                    float scale = videoRatio/screenRadio;

                    if(scale >= 1f){
                        videoView.setScaleX(scale);
                    }else{
                        videoView.setScaleY(1f/scale);
                    }
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

//    private List<Video> videos;
//    Context context;
//    private RecyclerView recyclerView;
//
//    public VideoAdapter(List<Video> videos) {
//        this.videos = videos;
//        this.context = context;
//
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_video, parent, false);
//
//        return new MyViewHolder(view);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        Video video = videos.get(position);
//        holder.bind(video);
//    }
//
//    @Override
//    public int getItemCount() {
//        return videos.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        private VideoView video;
//        private CircleImageView profileImage;
//        private TextView likedCount, commentCount, username, description;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            video = itemView.findViewById(R.id.video);
//            profileImage = itemView.findViewById(R.id.profile_image);
//            likedCount = itemView.findViewById(R.id.likedCount);
//            commentCount = itemView.findViewById(R.id.commentCount);
//            username = itemView.findViewById(R.id.username);
//            description = itemView.findViewById(R.id.description);
//        }
//
//        public void bind(Video item) {
//            video.setVideoPath(item.getLink());
//            Glide.with(itemView.getContext())
//                    .load(item.getOwner_avatar())
//                    .into(profileImage);
//            likedCount.setText(String.valueOf(item.getLiked()));
//            commentCount.setText(String.valueOf(item.getComment()));
//            username.setText(item.getOwner_name());
//            description.setText(item.getDescription());
//        }
//    }
}
