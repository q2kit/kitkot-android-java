package com.example.toptop.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toptop.R;
import com.example.toptop.socket.SocketRoot;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;

public class ChatDetailFragment extends Fragment {
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView messageList;
    private List<Message> messages;
    private MessageAdapter messageAdapter;
    private ChatMessage chatMessage;
    private Socket socket;

    private ImageView imAvatar;
    private TextView tvUsername;

    public ChatDetailFragment(ChatMessage chatMessage) {
       this.chatMessage = chatMessage;
       this.socket = SocketRoot.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        messageAdapter.setMessages(messages);
        messageAdapter.notifyItemInserted(this.messages.size() - 1);
        messageList.smoothScrollToPosition(this.messages.size() - 1);
    }


    public void addMessage(List<Message> messages, boolean isMore ) {

        if(!isMore){
            for(Message message: messages){
                this.messages.add(message);
            }
            messageAdapter.notifyItemInserted(this.messages.size() - 1);
            messageList.smoothScrollToPosition(this.messages.size() - 1);
        }else{
            if(messages.size() > 0){
                for(Message message: messages){
                    Log.e("Add", message.toString());
                    this.messages.add(0, message);
                }
                Log.e("Length message",this.messages.size()+"");
                messageAdapter.notifyItemInserted(this.messages.size() - 1);
            }
        }
//        messageAdapter.notifyDataSetChanged();
//        messageList.smoothScrollToPosition(this.messages.size() - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_detail2, container, false);

        messageInput = view.findViewById(R.id.message_input);
        sendButton = view.findViewById(R.id.send_button);
        messageList = view.findViewById(R.id.message_list);
        imAvatar = view.findViewById(R.id.imAvatar);
        tvUsername = view.findViewById(R.id.tvUsername);
        messages = new ArrayList<>();

        Picasso.get().load(chatMessage.getAvatar()).into(imAvatar);
        tvUsername.setText(chatMessage.getUsername());
        messageAdapter = new MessageAdapter(messages);
        messageList.setAdapter(messageAdapter);
        messageList.setLayoutManager(new LinearLayoutManager(getContext()));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == sendButton) {
                    String text = messageInput.getText().toString();
                    if (!text.isEmpty()) {
                        messageInput.setText("");
                        try {
                            JSONObject data = new JSONObject();
                            data.put("user_id_receive", chatMessage.getUserId());
                            data.put("content", text);
                            socket.emit("chat", data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });

        messageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("Scroll", dx + "//"+dy);
                if (dy !=0 && !recyclerView.canScrollVertically(-1) && messages.size() > 0) {
                    JSONObject data = new JSONObject();
                    try {
                        Log.e("Top here", "ABC");
                        data.put("user_id", chatMessage.getUserId());
                        data.put("message_id", messages.get(0).getId() );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    socket.emit("select-chat",data);
                }
            }
        });


        return view;
    }
}