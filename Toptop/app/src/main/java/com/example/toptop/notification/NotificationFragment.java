package com.example.toptop.notification;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toptop.R;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    private NotificationAdapter notificationAdapter;
    private RecyclerView noticeRecyclerView;

    private  List<Notification> notifications;
    public NotificationFragment() {

    }

    public void setNotifications(List<Notification> notis){
        this.notifications = notis;
        notificationAdapter.setNotifications(notis);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        noticeRecyclerView = view.findViewById(R.id.noticeRecyclerView);
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notifications);
        noticeRecyclerView.setAdapter(notificationAdapter);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.e("Nofication ","fragment");
        return view;
    }
}