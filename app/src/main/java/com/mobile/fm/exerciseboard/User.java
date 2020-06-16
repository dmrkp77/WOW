package com.mobile.fm.exerciseboard;

public class User {
    private String title;
    private String content;
    private String time;
    private String userName;
    private int commentNumber;

    public User(String title, String content, String time, String userName, int commentNumber) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.userName = userName;
        this.commentNumber = commentNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }
}
