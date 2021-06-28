package com.spawner.self.model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thoughts;
    private String username;
    private String userId;
    private String imageUrl;
    private Timestamp timeAdded;

    public Journal() {
    }

    public Journal(String title, String thoughts, String username, String userId, String imageUrl, Timestamp timeAdded) {
        this.title = title;
        this.thoughts = thoughts;
        this.username = username;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.timeAdded = timeAdded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getTimeAddes() {
        return timeAdded;
    }

    public void setTimeAddes(Timestamp timeAddes) {
        this.timeAdded = timeAddes;
    }
}
