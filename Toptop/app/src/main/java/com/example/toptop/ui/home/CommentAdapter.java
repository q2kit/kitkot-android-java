package com.example.toptop.ui.home;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.toptop.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    List<Comment> comments;

    public CommentAdapter(List<Comment> comments){
        this.comments = comments;
    }

    public void setCommentItems(List<Comment> comments){
        this.comments = comments;
        Log.d("QUAN", "setCommentItems: " + comments.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_comment, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.setCommentData(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        TextView content;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
        }

        void setCommentData(Comment comment){
            Glide.with(itemView.getContext()).load(comment.getAvatar()).into(avatar);
            name.setText(comment.getName());
            content.setText(comment.getContent());
        }

    }
}
