package com.example.pasganjil11rpl2.Model;

public class NewsModel {
    String title, publishedAt, author, description, urlToImage, url;

    public NewsModel(String title, String publishedAt, String author, String description, String urlToImage, String url) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.author = author;
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

    public String getAuthor() {
        return author;
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
