package com.example.testwhatatrip;

public class latesttoursmodel {
    private String tourImage;
    private String tourTitle;
    private  int tourPrize;

    public latesttoursmodel(String tourImage, String tourTitle, int tourPrize) {
        this.tourImage = tourImage;
        this.tourTitle = tourTitle;
        this.tourPrize = tourPrize;
    }

    public String getTourImage() {
        return tourImage;
    }

    public void setTourImage(String tourImage) {
        this.tourImage = tourImage;
    }

    public String getTourTitle() {
        return tourTitle;
    }

    public void setTourTitle(String tourTitle) {
        this.tourTitle = tourTitle;
    }

    public int getTourPrize() {
        return tourPrize;
    }

    public void setTourPrize(int tourPrize) {
        this.tourPrize = tourPrize;
    }
}
