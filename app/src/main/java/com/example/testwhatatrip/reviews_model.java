package com.example.testwhatatrip;

public class reviews_model {
    String review_image;
    String name;
    String review_description;
    String review_user_uid;

    public reviews_model(String review_image, String name, String review_description, String review_user_uid) {
        this.review_image = review_image;
        this.name = name;
        this.review_description = review_description;
        this.review_user_uid = review_user_uid;
    }

    public String getReview_image() {
        return review_image;
    }

    public void setReview_image(String review_image) {
        this.review_image = review_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview_description() {
        return review_description;
    }

    public void setReview_description(String review_description) {
        this.review_description = review_description;
    }

    public String getReview_user_uid() {
        return review_user_uid;
    }

    public void setReview_user_uid(String review_user_uid) {
        this.review_user_uid = review_user_uid;
    }
}
