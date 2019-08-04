package com.example.travelmantics.models;

import java.io.Serializable;

public class Resort implements Serializable {
    private String id;
    private String price;
    private String description;
    private String title;
    private String imageUrl;
    private String imageName;


    public Resort(String price, String description, String title, String imageUrl, String imageName) {
        this.setImageName(imageName);
        this.setPrice(price);
        this.setDescription(description);
        this.setTitle(title);
        this.setId(id);
        this.setImageUrl(imageUrl);
    }

    public Resort() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
