package com.example.toptop.model;


public class User {
    public int uid;
    public String username;
    public String name;
    public String avatar;
    public boolean is_premium;
    public int videos;
    public int followers;
    public int following;
    public int likes;

    public User() {

    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIs_premium() {
        return is_premium;
    }

    public void setIs_premium(boolean is_premium) {
        this.is_premium = is_premium;
    }

    public int getVideos() {
        return videos;
    }

    public void setVideos(int videos) {
        this.videos = videos;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User(int uid, String username, String name, String avatar, boolean is_premium, int videos, int followers, int following, int likes) {
        this.uid = uid;
        this.username = username;
        this.name = name;
        this.avatar = avatar;
        this.is_premium = is_premium;
        this.videos = videos;
        this.followers = followers;
        this.following = following;
        this.likes = likes;
    }
}
