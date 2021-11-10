package com.example.pasganjil11rpl2.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HistoryModel extends RealmObject {
    @PrimaryKey
    Integer id;
    String title, urlToImage, name, currentTime, url;

    public HistoryModel() {
    }

    public HistoryModel(String title, String urlToImage, String name, String currentTime, String url) {
        this.title = title;
        this.urlToImage = urlToImage;
        this.name = name;
        this.currentTime = currentTime;
        this.url = url;
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

    public String getName() {
        return name;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getUrl() {
        return url;
    }
}
