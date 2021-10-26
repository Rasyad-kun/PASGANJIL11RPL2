package com.example.pasganjil11rpl2.Model;

public class NewsModel {
    String title, publishedAt, name, description, urlToImage, url;

    public NewsModel(String title, String publishedAt, String name, String description, String urlToImage, String url) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.name = name;
        this.description = description;
        this.urlToImage = urlToImage;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getUrl() {
        return url;
    }
}
