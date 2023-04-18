package com.example.toptop.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import com.example.toptop.R;
import com.example.toptop.firebase.Firebase;
import com.example.toptop.socket.SocketRoot;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    final int T_CHAT_ID = 1;
    final int T_CHAT_INIT = 2;
    final int T_CHAT_LIST= 6;

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
                                    handle.exactListChatSection(data.getJSONArray("data"));
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

        Firebase f = new Firebase();
        f.getNotifications();
        Socket socket = SocketRoot.getInstance();
        socket.on("data",onNewMessage);
        socket.connect();
        JSONObject data =new JSONObject();
        try {
            data.put("user_id",2);
            socket.emit("list-chat",data);
        } catch (JSONException e) {
            Log.e("Data emit error", e.toString());
        }

    }
}