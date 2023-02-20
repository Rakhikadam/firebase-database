package com.Firebase_databaseEx;

public class custoreview {
    String name;
    String comment;
    String profile;
    String user_id;

    public custoreview(String name, String comment, String profile, String user_id) {
        this.name = name;
        this.comment = comment;
        this.profile = profile;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
