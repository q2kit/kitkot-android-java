package com.example.toptop.model;

public class Notification<T> {
    private String id;
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

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", objectReceiveId=" + objectReceiveId +
                ", objectSendId=" + objectSendId +
                ", type=" + type +
                ", status=" + status +
                ", content='" + content + '\'' +
                '}';
    }
}
