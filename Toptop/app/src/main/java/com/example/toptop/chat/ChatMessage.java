package com.example.toptop.chat;

public class ChatMessage {
    private int userId;
    private String message;
    private String username;
    private String avatar;
    private String sendTime;
    private boolean isSentByMe;


    public ChatMessage(int userId,String message, String username, String avatar, String sendTime, boolean isSentByMe) {
        this.userId = userId;
        this.message = message;
        this.username = username;
        this.avatar = avatar;
        this.sendTime = sendTime;
        this.isSentByMe = isSentByMe;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setSentByMe(boolean sentByMe) {
        isSentByMe = sentByMe;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
