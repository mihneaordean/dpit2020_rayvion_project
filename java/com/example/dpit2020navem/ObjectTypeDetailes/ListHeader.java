package com.example.dpit2020navem.ObjectTypeDetailes;

public class ListHeader {
    private String type;
    private Integer picture;

    public ListHeader(String type, Integer picture) {
        this.type = type;
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPicture() {
        return picture;
    }

    public void setPicture(Integer picture) {
        this.picture = picture;
    }
}
