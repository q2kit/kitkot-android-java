package com.example.toptop.notification;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.R;
import com.example.toptop.chat.ChatAdapter;
import com.example.toptop.chat.ChatMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        Log.e("Change noti", "OK");
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("Noti bind", "Ok");
        Notification notification = notifications.get(position);
        holder.tvUsername.setText(notification.getUsername());
        holder.tvInfo.setText(notification.getContent());
        holder.tvSendTime.setText(notification.getSendTime());
        Picasso.get().load(notification.getAvatar()).into(holder.imAvatar);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tvUsername, tvInfo, tvSendTime;
        ImageView imAvatar;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvUsername = view.findViewById(R.id.tvUsername);
            tvInfo = view.findViewById(R.id.tvInfo);
            tvSendTime = view.findViewById(R.id.tvSendTime);
            imAvatar = view.findViewById(R.id.imAvatar);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
