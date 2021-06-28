package util;

import android.app.Application;

public class NoteShareApi extends Application {
    private String fullname;
    private String username;
    private String userId;
    private static NoteShareApi instance;

    public static NoteShareApi getInstance() {
        if (instance == null)
            instance = new NoteShareApi();
        return instance;
    }

    public NoteShareApi() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
}
