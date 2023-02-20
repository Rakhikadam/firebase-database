package com.Firebase_databaseEx;

public class review {
    String name;
    String comment;
    String profile;

    public review(String name, String comment, String profile) {
        this.name = name;
        this.comment = comment;
        this.profile = profile;
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
}
