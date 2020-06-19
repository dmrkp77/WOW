package com.mobile.fm.category.data;

public class BoardListItem {
    private String title;
    private String content;
    private String time;
    private String name;
    private int comment_num=0;

    public BoardListItem(){

    }
    public BoardListItem(String title, String content, String time, String name) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.name = name;
    }
    public BoardListItem(String title, String content, String time, String name, int comment_num) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.name = name;
        this.comment_num=comment_num;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getComment_num() {
        return comment_num;
    }
}
