package com.example.toptop.firebase;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.toptop.notification.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Firebase {
    private FirebaseFirestore firestore;
    public void addUser(Context cx){
        firestore = FirebaseFirestore.getInstance();
        Map<String, Object> users = new HashMap<>();
        users.put("username", "nghia");
        users.put("password", "abc");
        firestore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(cx, "Success Add User", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failure", e.toString());
                Toast.makeText(cx, "Fail Add User"+ e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getNumberNotification(MenuItem tvNotification, int userId){
        AtomicInteger notiNum = new AtomicInteger();
        firestore = FirebaseFirestore.getInstance();
                CollectionReference notificationQuery = firestore.collection("notifications")
                .document(userId+"")
                .collection("data");
        Query query = notificationQuery.orderBy("id", Query.Direction.DESCENDING).limit(10);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                value.getDocumentChanges().forEach(documentChange -> {
                    Log.e("Noti change", documentChange.getType()+"");
                    if(documentChange.getType().equals(DocumentChange.Type.ADDED)){
                        notiNum.addAndGet(1);
                        tvNotification.setTitle("Inbox "+notiNum.get());
                        Log.e("Notify change", notiNum.get()+"");
                    }
                });
            }
        });
        return notiNum.get();
    }

    public List<Notification> getNotifications(){
        List<Notification> notifications = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("notifications")
                .document("1")
                .collection("data")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot document: value){
                    Notification noti = new Notification(
                            document.getString("id"),
                            (int)Math.floor(document.getDouble("object_receive_id")),
                            (int)Math.floor(document.getDouble("object_send_id")),
                            (int)Math.floor(document.getDouble("type")),
                            (int)Math.floor(document.getDouble("status")),
                            document.getString("content")
                    );

                    Log.e("Noti",noti.toString());
                    notifications.add(noti);
                }
            }
        });
        return notifications;
    }

//    public List<Notification> getChangeNotifications(){
//        List<Notification> notifications = new ArrayList<>();
//        firestore = FirebaseFirestore.getInstance();
//        CollectionReference notificationQuery = firestore.collection("notifications")
//                .document("1")
//                .collection("data");
//
//        Query query = notificationQuery.orderBy("id", Query.Direction.DESCENDING).limit(10);
//
//        query.addSnapshotListener(new )
//        return notifications;
//    }
}
