package com.example.testwhatatrip;

public class slider_model {
    private String banner;
    private String title;

    public slider_model(String banner) {
        this.banner = banner;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public slider_model(String banner, String title) {
        this.banner = banner;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
