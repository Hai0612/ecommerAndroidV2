package com.example.ecomapplication.models;


import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name ;
    private String img_url;
    private String id_category;
    private int price;
    private String size;
    private int quantity;
    private String description;
    private String rating;

    public Product() {}

    public Product(String id, String name, String img_url, String id_category, int price,
                   String size, int quantity, String description, String rating) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.id_category = id_category;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.description = description;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
