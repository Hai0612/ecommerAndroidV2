package com.example.ecomapplication.models;


import java.io.Serializable;

public class OrderModel implements Serializable {
    String orderAddress;
    String orderDate;
    String shippedDate;
    int total;

    public OrderModel() {
    }

    public OrderModel(String orderAddress, String orderDate, String shippedDate, int total) {
        this.orderAddress = orderAddress;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.total = total;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
