package com.example.testwhatatrip;

public class categories_model {

    private String categories_icon;
    private String categories_title;

    public categories_model(String categories_icon, String categories_title) {
        this.categories_icon = categories_icon;
        this.categories_title = categories_title;
    }

    public String getCategories_icon() {
        return categories_icon;
    }

    public void setCategories_icon(String categories_icon) {
        this.categories_icon = categories_icon;
    }

    public String getCategories_title() {
        return categories_title;
    }

    public void setCategories_title(String categories_title) {
        this.categories_title = categories_title;
    }
}
