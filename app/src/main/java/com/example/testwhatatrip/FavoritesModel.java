package com.example.testwhatatrip;

public class FavoritesModel {
    private String image;
    private String Title;
    private String ShortDescription;
    private int Price;

    public FavoritesModel(String image, String title, String shortDescription, int price) {
        this.image = image;
        Title = title;
        ShortDescription = shortDescription;
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

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
