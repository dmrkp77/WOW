package com.mobile.fm.exerciseboard;

public class CommentListItem {

    private String username;
    private String createdAt;
    private String body;

    public CommentListItem(String username, String createdAt, String body) {
        this.username = username;
        this.createdAt = createdAt;
        this.body = body;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
