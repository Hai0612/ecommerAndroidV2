package com.example.ecomapplication.models;


import java.io.Serializable;
import java.util.Date;

public class OrderModel implements Serializable {
    String orderAddress;
    Date orderDate;
    Date shippedDate;
    int total;

    public OrderModel() {
    }

    public OrderModel(String orderAddress, Date orderDate, Date shippedDate, int total) {
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
