package com.example.toptop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.toptop.chat.ChatAdapter;
import com.example.toptop.chat.ChatDetailFragment;
import com.example.toptop.chat.ChatHandle;
import com.example.toptop.chat.ChatMessage;
import com.example.toptop.chat.Message;
import com.example.toptop.firebase.Firebase;
import com.example.toptop.notification.Notification;
import com.example.toptop.notification.NotificationFragment;
import com.example.toptop.notification.NotificationHandle;
import com.example.toptop.socket.SocketRoot;
import com.example.toptop.ui.home.Video;
import com.example.toptop.ui.home.VideoHandle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private int _currentPage = 0;
    private ArrayList<Video> videos = new ArrayList<>();
    private  ViewPageAdapter adapter;

    // for chat
    final int T_CHAT_ID = 1;
    final int T_CHAT_INIT = 2;
    final int T_CHAT_LIST= 6;
    final int T_CHAT_MORE= 7;
    final int T_NOTI_LIST= 10;
    final int T_VIDEO= 5;
    final int T_VIDEO_LIKE= 3;

    private List<ChatMessage> chatMessages;
    private List<Notification> notifications;
    private ChatAdapter chatAdapter;
    private FragmentManager fragmentManager;
    private ChatDetailFragment chatDetailFragment;
    private NotificationFragment notificationFragment;
    private Socket socket;
    private FrameLayout frameLayout;
    private LinearLayout btNotification;
    private Firebase firebase;

    private int userId;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try {
                HomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            int type = Integer.parseInt(data.getString("type"));
                            ChatHandle handle = new ChatHandle();
                            NotificationHandle notificationHandle = new NotificationHandle();
                            VideoHandle videoHandle = new VideoHandle();
                            Log.e("Socket type", type+"");

                            switch(type){
                                case T_CHAT_LIST:
                                    chatMessages = handle.exactListChatSection(data.getJSONArray("data"), userId);
                                    chatAdapter.setMessages(chatMessages);
                                    break;
                                case T_CHAT_INIT:
                                    JSONArray arr = data.getJSONArray("data");
                                    if(arr != null && chatDetailFragment != null){
                                        chatDetailFragment.setMessages(handle.exactListMessage(arr, userId));
                                    }
                                    break;
                                case T_CHAT_ID:
                                    if(chatDetailFragment != null){
                                        chatDetailFragment.addMessage(handle.exactListMessage(data.getJSONArray("data"), userId), false);
                                    }
                                    socket.emit("list-chat");
                                    break;
                                case T_CHAT_MORE:
                                    if(chatDetailFragment != null){
                                        List<Message> messages = handle.exactListMessage(data.getJSONArray("data"), userId);

                                        chatDetailFragment.addMessage(messages, true);
                                    }
                                    break;
                                case  T_NOTI_LIST:
                                    if(notificationFragment != null){
                                        notifications = notificationHandle.exactListNoti(data.getJSONArray("data"));
                                        notificationFragment.setNotifications(notifications);
                                    }
                                    break;
                                case T_VIDEO_LIKE:
                                case  T_VIDEO:
                                    Video v = videoHandle.exactVideoInfo(data.getJSONObject("data"));
                                    adapter.updateVideo(v);
                                    Log.e("video get", v.toString());
                                    break;

                            }

                        } catch (JSONException e) {
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
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("BackPress", "OK");
        socket.emit("list-chat");
        if(frameLayout!= null){
            frameLayout.setVisibility(View.INVISIBLE);
        }
        if(fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.viewPager);

        // for chat
        SharedPreferences sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);;
        Log.e("TOKEN", sharedPreferences.getString("token",""));
        userId = sharedPreferences.getInt("uid", 0);
        SocketRoot.token = sharedPreferences.getString("token","");
        socket = SocketRoot.getInstance();
        socket.on("data",onNewMessage);
        socket.connect();

        firebase = new Firebase();
        firebase.getNumberNotification(bottomNavigationView.getMenu().findItem(R.id.mInbox), userId);
        //

        adapter = new ViewPageAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.mHome).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.mDiscover).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.mPost).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.mInbox).setChecked(true);
                        fragmentManager = getSupportFragmentManager();
                        btNotification = findViewById(R.id.btNotification);
                        btNotification.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                frameLayout.setVisibility(View.VISIBLE);
                                notificationFragment =  new NotificationFragment();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.chatDetailFrame, notificationFragment);
                                fragmentTransaction.addToBackStack("notification");
                                fragmentTransaction.commit();
                                Log.e("Socket emit", "list-notification");
                                socket.emit("list-notification");
                            }
                        });
                        chatMessages = new ArrayList<>();
                        chatAdapter = new ChatAdapter(new ChatSelection());
                        frameLayout = findViewById(R.id.chatDetailFrame);
                        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
                        chatRecyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                        chatRecyclerView.setAdapter(chatAdapter);
                        Log.e("Socket emit", "list-chat");
                        socket.emit("list-chat");
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.mMe).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mHome:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mDiscover:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mPost:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.mInbox:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.mMe:
                        viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

    }
    class ChatSelection implements ChatAdapter.IChat{

        @Override
        public void onChatClicked(View v, int position) {
            ChatMessage chatMessage =  chatMessages.get(position);
            Log.e("Position ", position+"");
            Toast.makeText(HomeActivity.this,"Click "+position,Toast.LENGTH_SHORT ).show();
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