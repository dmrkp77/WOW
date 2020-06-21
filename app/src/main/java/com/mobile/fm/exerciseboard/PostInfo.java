package com.mobile.fm.exerciseboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostInfo implements Serializable {
    private String title;
    private ArrayList<String> contents;
    private ArrayList<String> formats;
    private String publisher;//유저 아이디
    private Date createdAt;
    private String nid;//유저 닉네임
    private String id;//문서 이름(파이어베이스 문서 이름)
    private String boardSelect="Music";
    private int numComments=0;

    public PostInfo(String title, ArrayList<String> contents,String boardSelect, ArrayList<String> formats, String publisher, Date createdAt,String id, String nid){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.id = id;
        this.nid = nid;
        this.boardSelect = boardSelect;
    }
    public PostInfo(String title, ArrayList<String> contents,String boardSelect, ArrayList<String> formats, String publisher, Date createdAt, String nid){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.nid = nid;
        this.boardSelect = boardSelect;
    }
    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt, String id,String nid){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.nid = nid;
        this.id = id;
    }

    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt, String id,String nid,String boardSelect){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.nid = nid;
        this.id = id;
        this.boardSelect=boardSelect;
    }


    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }

    public Map<String, Object> getPostInfo(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("title",title);
        docData.put("contents",contents);
        docData.put("formats",formats);
        docData.put("publisher",publisher);
        docData.put("createdAt",createdAt);
        docData.put("nid",nid);
        docData.put("boardSelect",boardSelect);
        return  docData;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public ArrayList<String> getContents(){
        return this.contents;
    }
    public void setContents(ArrayList<String> contents){
        this.contents = contents;
    }
    public ArrayList<String> getFormats(){
        return this.formats;
    }
    public void setFormats(ArrayList<String> formats){
        this.formats = formats;
    }
    public String getPublisher(){
        return this.publisher;
    }
    public void setPublisher(String publisher){
        this.publisher = publisher;
    }
    public Date getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
    public String getNid(){
        return this.nid;
    }
    public void setNid(String nid){ this.nid = nid; }
    public String getId(){
        return this.id;
    }
    public void setId(String id){this.id = id; }
    public String getBoardSelect() {
        return boardSelect;
    }
    public void setBoardSelect(String boardSelect) {
        this.boardSelect = boardSelect;
    }
    public int getNumComments() {
        return numComments;
    }
    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }
}
