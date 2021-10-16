package com.example.pasganjil11rpl2.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HistoryModel extends RealmObject {
    @PrimaryKey
    Integer id;
    String title, urlToImage;
    int time;

    public HistoryModel() {
    }

    public HistoryModel(String title, String urlToImage, int time) {
        this.title = title;
        this.urlToImage = urlToImage;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public int getTime() {
        return time;
    }
}
