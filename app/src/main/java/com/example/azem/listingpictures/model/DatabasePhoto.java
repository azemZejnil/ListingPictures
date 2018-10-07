package com.example.azem.listingpictures.model;

public class DatabasePhoto {

    private byte[] data;
    private String title;

    public DatabasePhoto() {
    }

    public DatabasePhoto(byte[] data, String title) {
        this.data = data;
        this.title = title;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
