package com.tuyp.restaurantapp.Model;

import java.io.Serializable;

public class Order implements Serializable {
    String name,image;
    int qty,price;

    public Order(String name, int qty, int price, String image) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
