package com.example.toptop.chat;

public class ChatSection {
    private int userId;
    private String avatar;
    private String username;
    private String lastMessage;
    private String sendTime;

    public ChatSection(int userId, String avatar, String username, String lastMessage, String sendTime) {
        this.userId = userId;
        this.avatar = avatar;
        this.username = username;
        this.lastMessage = lastMessage;
        this.sendTime = sendTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "ChatSection{" +
                "userId=" + userId +
                ", lastMessage='" + lastMessage + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
