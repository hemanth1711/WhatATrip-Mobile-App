package com.example.testwhatatrip;

import java.util.Date;

public class MyBookingsModel {
    private String image;
    private String Title;
    private Date date;
    private int Price;

    public MyBookingsModel(String image, String title, Date date, int price) {
        this.image = image;
        Title = title;
        this.date = date;
        Price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
