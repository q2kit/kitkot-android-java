package com.example.toptop.socket;

import android.util.Log;

import java.net.URISyntaxException;
import java.util.TreeMap;

import io.socket.client.IO;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketRoot {
    public static Socket mSocket;
    public static String token;
    public static  Socket getInstance(){
        Log.e("SOcket",token);
        if(mSocket == null){
            try {
                IO.Options options = new IO.Options();
                options.auth = new TreeMap<>();
                options.auth.put("token", token);
                options.path = "/message";
                mSocket = IO.socket("http://192.168.1.6:3005", options);
            } catch (URISyntaxException e) {
                Log.e("Socket connect error",e.toString());
            }
        }
        return mSocket;
    }

}
