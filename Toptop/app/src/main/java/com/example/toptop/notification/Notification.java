package com.example.toptop.notification;

public class Notification {
    private String id;
    private String username;
    private String avatar;
    private String sendTime;
    private int objectReceiveId;
    private int objectSendId;
    private int type;
    private int status;
    private String content;

    public Notification(String id, int objectReceiveId, int objectSendId, int type, int status, String content) {
        this.id = id;
        this.objectReceiveId = objectReceiveId;
        this.objectSendId = objectSendId;
        this.type = type;
        this.status = status;
        this.content = content;
    }

    public Notification(String id, String username, String avatar, String content, String sendTime) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.content = content;
        this.sendTime = sendTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getObjectReceiveId() {
        return objectReceiveId;
    }

    public void setObjectReceiveId(int objectReceiveId) {
        this.objectReceiveId = objectReceiveId;
    }

    public int getObjectSendId() {
        return objectSendId;
    }

    public void setObjectSendId(int objectSendId) {
        this.objectSendId = objectSendId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", objectReceiveId=" + objectReceiveId +
                ", objectSendId=" + objectSendId +
                ", type=" + type +
                ", status=" + status +
                ", content='" + content + '\'' +
                '}';
    }
}
