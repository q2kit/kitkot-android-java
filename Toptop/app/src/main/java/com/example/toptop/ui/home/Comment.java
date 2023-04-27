package com.example.toptop.ui.home;

public class Comment {
    String avatar, name, content;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment(String avatar, String name, String content) {
        this.avatar = avatar;
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
