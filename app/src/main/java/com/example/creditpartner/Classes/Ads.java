package com.example.creditpartner.Classes;

public class Ads {

    String customerName, numberOfClicks, adType, adImage;


    public Ads() {
    }

    public Ads(String customerName, String numberOfClicks, String adType, String adImage) {
        this.customerName = customerName;
        this.numberOfClicks = numberOfClicks;
        this.adType = adType;
        this.adImage = adImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getNumberOfClicks() {
        return numberOfClicks;
    }

    public void setNumberOfClicks(String numberOfClicks) {
        this.numberOfClicks = numberOfClicks;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }
}
