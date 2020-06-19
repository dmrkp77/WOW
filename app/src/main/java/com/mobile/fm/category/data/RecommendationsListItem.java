package com.mobile.fm.category.data;

public class RecommendationsListItem {
    private String cover;
    private String title;
    private String content;
    private String url;

    public RecommendationsListItem(String cover, String title, String content, String url) {
        this.cover = cover;
        this.title = title;
        this.content = content;
        this.url = url;
    }
    public RecommendationsListItem(){

    }

    public String getCover() {
        return cover;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
