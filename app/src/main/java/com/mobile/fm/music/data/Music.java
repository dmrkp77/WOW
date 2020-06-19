package com.mobile.fm.music.data;

public class Music {
    private String albumCover;
    private String title;
    private String name;
    private String url;

    public Music(String albumCover, String title, String name, String url) {
        this.albumCover = albumCover;
        this.title = title;
        this.name = name;
        this.url =url;
    }
    public Music(){

    }

    public String getAlbumCover() {
        return albumCover;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
