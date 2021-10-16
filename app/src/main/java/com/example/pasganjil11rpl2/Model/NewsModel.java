package com.example.pasganjil11rpl2.Model;

public class NewsModel {
    String title, urlToImage, publishedAt;

    public NewsModel(String title, String urlToImage, String publishedAt) {
        this.title = title;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }
}
