package com.app.knbs.adapter.model;

import java.io.Serializable;

/**
 * Developed by Rodney on 22/11/2016.
 */

public class News implements Serializable {
    private String news_id,title,content,image,date;


    public News() {
    }

    public News(String news_id, String content,String image, String date, String title){
        this.news_id = news_id;
        this.content = content;
        this.image = image;
        this.title = title;
        this.date=date;
    }

    public String getNewsID() {
        return news_id;
    }

    public void setNewsID(String news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {return content;}

    public void setContent(String content) { this.content = content; }

}

