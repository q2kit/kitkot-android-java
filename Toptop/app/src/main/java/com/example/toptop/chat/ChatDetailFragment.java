package com.example.toptop.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.toptop.R;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailFragment extends Fragment {
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView messageList;
    private List<Message> messages;
    private MessageAdapter messageAdapter;

    public ChatDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        messageAdapter.setMessages(messages);
//        messageAdapter.notifyDataSetChanged();
//        messageList.smoothScrollToPosition(this.messages.size() - 1);
    }


    public void addMessage(Message message) {
        this.messages.add(message);
        messageAdapter.notifyItemInserted(messages.size() - 1);
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
        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        messageList.setAdapter(messageAdapter);
        messageList.setLayoutManager(new LinearLayoutManager(getContext()));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == sendButton) {
                    String text = messageInput.getText().toString();
                    if (!text.isEmpty()) {
                        Log.e("Random message", Math.floor(Math.random() *2)+ "");
                        messages.add(new Message(text, 1,2, "10/04/2022"));
                        messageAdapter.notifyItemInserted(messages.size() - 1);
                        messageList.smoothScrollToPosition(messages.size() - 1);

                        messageInput.setText("");
                    }
                }
            }
        });

        return view;
    }
}