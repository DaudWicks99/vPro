package com.example.coba.activity_home.model;

public class MenuHome {
    String id;
    String idVote;
    String title;
    String group;
    String url;

    public String getIdVote(){
        return idVote;
    }

    public void setIdVote(String idVote) {
        this.idVote = idVote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
