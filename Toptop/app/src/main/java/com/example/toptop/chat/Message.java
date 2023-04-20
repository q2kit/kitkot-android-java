package com.example.toptop.chat;

public class Message {
    private String id;
    private String text;
    private boolean incoming;
    private boolean isLastMessage;
    private int userIdSend;
    private int userIdReceive;
    private String sendTime;
//    public Message(String text, boolean incoming) {
//        this.text = text;
//        this.incoming = incoming;
//    }


    public Message(String id, String text, int userIdSend, int userIdReceive, String sendTime) {
        this.id = id;
        this.text = text;
        this.userIdSend = userIdSend;
        this.userIdReceive = userIdReceive;
        this.sendTime = sendTime;
    }

    public String getText() {
        return text;
    }

    public boolean isIncoming() {
        return incoming;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    public boolean isLastMessage() {
        return isLastMessage;
    }

    public void setLastMessage(boolean lastMessage) {
        isLastMessage = lastMessage;
    }

    public int getUserIdSend() {
        return userIdSend;
    }

    public void setUserIdSend(int userIdSend) {
        this.userIdSend = userIdSend;
    }

    public int getUserIdReceive() {
        return userIdReceive;
    }

    public void setUserIdReceive(int userIdReceive) {
        this.userIdReceive = userIdReceive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", incoming=" + incoming +
                ", isLastMessage=" + isLastMessage +
                ", userIdSend=" + userIdSend +
                ", userIdReceive=" + userIdReceive +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
