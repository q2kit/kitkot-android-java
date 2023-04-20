package com.example.merchantDemo.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.merchantDemo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatMessage> messages;
    private IChat iChat;

    public ChatAdapter(IChat iChat) {
        this.iChat = iChat;
        this.messages = new ArrayList<>();
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageTextView.setText(message.getMessage());
        holder.usernameTextView.setText(message.getUsername());
        holder.sendTimeTextView.setText(message.getSendTime());
        Picasso.get().load(message.getAvatar()).into(holder.avatarImageView);
        if (message.isSentByMe()) {
            holder.messageTextView.setText("You: "+ message.getMessage());
        } else {
            holder.messageTextView.setText(message.getUsername() + ": "+ message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView messageTextView,usernameTextView, sendTimeTextView;
        ImageView avatarImageView;
        RelativeLayout chatItem;

        ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.chatMessageTextView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            sendTimeTextView = itemView.findViewById(R.id.sendTimeTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            chatItem = itemView.findViewById(R.id.chatItem);
            chatItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iChat.onChatClicked(view, getAdapterPosition());

        }
    }

    public interface IChat{
        void onChatClicked(View v, int position);
    }
}
