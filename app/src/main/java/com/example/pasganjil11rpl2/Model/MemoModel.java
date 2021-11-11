package com.example.pasganjil11rpl2.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MemoModel extends RealmObject {
    @PrimaryKey
    Integer id;
    String title, content;

    public MemoModel() {
    }

    public MemoModel(String title, String content) {
        this.title = title;
        this.content = content;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
