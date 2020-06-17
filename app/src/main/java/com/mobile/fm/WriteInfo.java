package com.mobile.fm;

import java.util.ArrayList;
import java.util.Date;

public class WriteInfo {
    private String title;
   private ArrayList<String> contents;
  //  private String contents;
    private String publisher;
    private Date createdAt;

    public WriteInfo(String title,ArrayList<String> contents,String publisher,Date createdAt){
        this.contents=contents;
        this.publisher=publisher;
        this.title=title;
        this.createdAt=createdAt;
    }
    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title=title;}
    public ArrayList<String> getContents(){return this.contents;}
    public void setContents(ArrayList<String> contents){this.contents=contents;}
    public String getPublisher(){return this.publisher;}
    public void setPublisher(String publisher){this.publisher=publisher;}
    public Date getCreatedAt(){return this.createdAt;}
//    public void setCreatedAt(Date createdAt){this.createdAt=createdAt;}
//public WriteInfo(String title,String contents,String publisher,Date createdAt){
//    this.contents=contents;
//    this.publisher=publisher;
//    this.title=title;
//    this.createdAt=createdAt;
//}
//    public String getTitle(){return this.title;}
//    public void setTitle(String title){this.title=title;}
//    public String getContents(){return this.contents;}
//    public void setContents(String contents){this.contents=contents;}
//    public String getPublisher(){return this.publisher;}
//    public void setPublisher(String publisher){this.publisher=publisher;}
//    public Date getCreatedAt(){return this.createdAt;}
//    public void setCreatedAt(Date createdAt){this.createdAt=createdAt;}

}
