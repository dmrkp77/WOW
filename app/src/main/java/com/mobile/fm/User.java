package com.mobile.fm;

import java.util.ArrayList;

public class User {
    public String username;
    public String email;
    public String password;
    public String uid;
    public String registerDate;
    public ArrayList<String> bookmark;

    public User(String registerDate, String username, String email, String password,String uid,ArrayList<String> bookmark) {
        this.registerDate = registerDate;
        this.username = username;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.bookmark=bookmark;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String rigisterDate) {
        this.registerDate = rigisterDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getBookmark() { return bookmark; }

    public void setBookmark(ArrayList<String> bookmark) { this.bookmark = bookmark; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
