package com.example.toptop.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.toptop.R;
import com.example.toptop.socket.SocketRoot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    final int T_CHAT_ID = 1;
    final int T_CHAT_INIT = 2;
    final int T_CHAT_LIST= 6;
    final int T_CHAT_MORE= 7;

    private List<ChatMessage> chatMessages;
    private ChatAdapter adapter;
    private FragmentManager fragmentManager;
    private ChatDetailFragment chatDetailFragment;
    private Socket socket;
    private FrameLayout frameLayout;

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                ChatActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            int type = Integer.parseInt(data.getString("type"));
                            ChatHandle handle = new ChatHandle();
                            Log.e("Socket type", type+"");

                            switch(type){
                                case T_CHAT_LIST:
                                    chatMessages = handle.exactListChatSection(data.getJSONArray("data"));
                                    adapter.setMessages(chatMessages);
                                    break;
                                case T_CHAT_INIT:
                                    chatDetailFragment.setMessages(handle.exactListMessage(data.getJSONArray("data")));
                                    break;
                                case T_CHAT_ID:
                                    if(chatDetailFragment != null){
                                        chatDetailFragment.addMessage(handle.exactListMessage(data.getJSONArray("data")), false);
                                    }
                                    socket.emit("list-chat");
                                    break;
                                case T_CHAT_MORE:
                                    if(chatDetailFragment != null){
                                        List<Message> messages = handle.exactListMessage(data.getJSONArray("data"));

                                        chatDetailFragment.addMessage(messages, true);
                                    }
                            }

                        } catch (JSONException  e) {
                            Log.e("Socket run error", e.toString());
                            return;
                        }

                    }
                });
            }catch (Exception e){
                Log.e("Socket call error", e.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//
//
//        Firebase f = new Firebase();
//        f.getNotifications();
        fragmentManager = getSupportFragmentManager();
        chatMessages = new ArrayList<>();
        adapter = new ChatAdapter(new ChatSelection());
        frameLayout = findViewById(R.id.chatDetailFrame);
        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        socket = SocketRoot.getInstance();
        socket.on("data",onNewMessage);
        socket.connect();

        //get list chat
        socket.emit("list-chat");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("BackPress", "OK");
        socket.emit("list-chat");
        frameLayout.setVisibility(View.INVISIBLE);
        if(fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        }
    }

    class ChatSelection implements ChatAdapter.IChat{

        @Override
        public void onChatClicked(View v, int position) {
            ChatMessage chatMessage =  chatMessages.get(position);
            Log.e("Position ", position+"");
            Toast.makeText(ChatActivity.this,"Click "+position,Toast.LENGTH_SHORT ).show();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            chatDetailFragment = new ChatDetailFragment(chatMessage);
            frameLayout.setVisibility(View.VISIBLE);
            fragmentTransaction.add(R.id.chatDetailFrame, chatDetailFragment);
            fragmentTransaction.addToBackStack(chatMessage.getUserId()+"");
            fragmentTransaction.commit();

            JSONObject data = new JSONObject();
            try {
                data.put("user_id", chatMessage.getUserId());
                socket.emit("select-chat", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}