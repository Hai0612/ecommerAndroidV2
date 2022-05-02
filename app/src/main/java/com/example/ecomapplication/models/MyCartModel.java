package com.example.ecomapplication.models;

public class MyCartModel {
    private  String documentId;
    String img_url;
    String name;
    int price;
    int quantity;

    public MyCartModel() {
    }

    public MyCartModel(String img_url, String name, int price, int quantity,int id) {
        this.img_url = img_url;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
