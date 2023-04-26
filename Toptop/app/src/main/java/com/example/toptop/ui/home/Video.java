package com.example.toptop.ui.home;

public class Video {
    private int id;
    private String description;
    private String link;
    private int owner_id;
    private String owner_name;

    public String getOwner_avatar() {
        return owner_avatar;
    }

    public void setOwner_avatar(String owner_avatar) {
        this.owner_avatar = owner_avatar;
    }
    public  boolean is_played =false;
    private String owner_avatar;
    private boolean is_premium;
    private int watched;
    private int liked;
    private int comment;
    private boolean is_liked;
    private boolean is_followed;

    public int getId() {
        return id;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public boolean isIs_premium() {
        return is_premium;
    }

    public void setIs_premium(boolean is_premium) {
        this.is_premium = is_premium;
    }

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public boolean isIs_followed() {
        return is_followed;
    }

    public void setIs_followed(boolean is_followed) {
        this.is_followed = is_followed;
    }

    public boolean isIs_played() {
        return is_played;
    }

    public void setIs_played(boolean is_played) {
        this.is_played = is_played;
    }

    public Video(int id, String description, String link, int owner_id, String owner_name, String owner_avatar, boolean is_premium, int watched, int liked, int comment, boolean is_liked, boolean is_followed) {
        this.id = id;
        this.description = description;
        this.link = link;
        this.owner_id = owner_id;
        this.owner_name = owner_name;
        this.owner_avatar = owner_avatar;
        this.is_premium = is_premium;
        this.watched = watched;
        this.liked = liked;
        this.comment = comment;
        this.is_liked = is_liked;
        this.is_followed = is_followed;
    }

    public Video(int id, int liked, int comment, boolean is_liked) {
        this.id = id;
        this.liked = liked;
        this.comment = comment;
        this.is_liked = is_liked;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", liked=" + liked +
                ", comment=" + comment +
                ", is_liked=" + is_liked +
                '}';
    }
}