package com.example.toptop.ui.home;


import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.R;
import com.example.toptop.socket.SocketRoot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Socket;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    List<Video> videoItems;
    static CommentDialogFragment dialog;
    static FragmentManager fragmentManager;
    static ProfileDialogFragment.IProfile iProfile;
    // Add this constructor:
    public VideoAdapter(List<Video> videoItems, FragmentManager fragmentManager,ProfileDialogFragment.IProfile iProfile){
        this.videoItems = videoItems;
        this.fragmentManager = fragmentManager;
        this.iProfile=  iProfile;
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

    public void setComments(List<Comment> comments) {
        if(dialog != null){
            dialog.changeComments(comments);
        }
    }

    public void addComments(List<Comment> comments) {
        if(dialog != null){
            dialog.addComments(comments);
        }
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {


        VideoView videoView;
        CircleImageView profileImage;
        TextView likedCount, commentCount, username, description;
        ProgressBar progressBar;
        ImageView play_icon;
        ImageView imHeart, comment, share;
        View view3;



        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video);
            profileImage = itemView.findViewById(R.id.profile_image);
            likedCount = itemView.findViewById(R.id.likedCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            username = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
            progressBar = itemView.findViewById(R.id.progressBar);
            imHeart = itemView.findViewById(R.id.heart);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
            play_icon = itemView.findViewById(R.id.play_icon);
            view3 = itemView.findViewById(R.id.view3);
        }

        void setVideoData(Video videoItem){
            Glide.with(itemView.getContext())
                    .load(videoItem.getOwner_avatar())
                    .into(profileImage);
            likedCount.setText(String.valueOf(videoItem.getLiked()));
            commentCount.setText(String.valueOf(videoItem.getComment()));
            username.setText(videoItem.getOwner_name());
            description.setText(videoItem.getDescription());
            if(!videoItem.isIs_played()){
                videoView.setVideoPath(videoItem.getLink());
                videoItem.setIs_played(true);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                       try {
                           progressBar.setVisibility(View.GONE);
                           mp.start();
                       }catch (Exception e){

                       }
                    }
                });

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {
                            mediaPlayer.start();
                        }catch (Exception e){

                        }
                    }
                });
                 videoView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         try {
                             if(videoView.isPlaying()){
                                 play_icon.setVisibility(View.VISIBLE);
                                 videoView.pause();
                             }else{
                                 play_icon.setVisibility(View.GONE);
                                 videoView.start();
                             }
                         }catch (Exception e){
                         }
                     }
                 });
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickShare(videoItem);
                    }
                });
                view3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if(videoView.isPlaying()){
                                play_icon.setVisibility(View.VISIBLE);
                                videoView.pause();
                            }else{
                                play_icon.setVisibility(View.GONE);
                                videoView.start();
                            }
                        }catch (Exception e){
                        }
                    }
                });
            }

            if(videoItem.isIs_liked()){
                imHeart.setImageResource(R.drawable.heart_active);
            }else{
                imHeart.setImageResource(R.drawable.heart);
            }

            imHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHeart(videoItem);
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickComment(videoItem.getId(), videoItem.getOwner_id());
                }
            });

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileDialogFragment profile = new ProfileDialogFragment(videoItem.getOwner_id(), iProfile);
                    profile.show(fragmentManager,"profile "+ videoItem.getId());
                }
            });
        }

        private void clickShare(Video videoItem) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String body = videoItem.getLink();
            String sub = "Your Subject";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
            myIntent.putExtra(Intent.EXTRA_TEXT,body);
            itemView.getContext().startActivity(Intent.createChooser(myIntent, "Share Using"));
        }

        public void clickHeart(Video video){
            Socket socket = SocketRoot.getInstance();
            JSONObject data = new JSONObject();
            try {
                data.put("video_id", video.getId());
                data.put("owner_id", video.getOwner_id());
                socket.emit("like", data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        public void clickComment(int video_id, int owner_id){
            Log.d("QUAN", "click comment" + video_id);
            dialog = new CommentDialogFragment(video_id,owner_id );
            dialog.show(fragmentManager, "comment_dialog");
            Socket socket = SocketRoot.getInstance();
            try {
                JSONObject data = new JSONObject();
                data.put("video_id", video_id);
                socket.emit("list-comment",data);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
