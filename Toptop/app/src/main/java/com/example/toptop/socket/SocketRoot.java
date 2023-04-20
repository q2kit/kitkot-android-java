package com.example.toptop.socket;

import android.util.Log;

import java.net.URISyntaxException;
import java.util.TreeMap;

import io.socket.client.IO;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketRoot {
    public static Socket mSocket;

    public static  Socket getInstance(){
        if(mSocket == null){
            try {
                IO.Options options = new IO.Options();
                options.auth = new TreeMap<>();
                options.auth.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MX0.p-J-1VzpKeTP8BBnUuZ9ckzGTSi3nQbIGUHIstay6kM");
                options.path = "/message";
                mSocket = IO.socket("http://10.1.48.35:3005", options);
            } catch (URISyntaxException e) {
                Log.e("Socket connect error",e.toString());
            }
        }
        return mSocket;
    }

}
