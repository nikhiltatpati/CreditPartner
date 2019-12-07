package com.example.creditpartner.Classes;

public class Slides {

    private String AdImage, AdText, AdLink;

    public Slides() {
    }

    public Slides(String adImage, String adText, String AdLink) {
        this.AdImage = adImage;
        this.AdText = adText;
        this.AdLink = AdLink;
    }

    public String getAdLink() {
        return AdLink;
    }

    public void setAdLink(String adLink) {
        AdLink = adLink;
    }

    public String getAdImage() {
        return AdImage;
    }

    public void setAdImage(String adImage) {
        AdImage = adImage;
    }

    public String getAdText() {
        return AdText;
    }

    public void setAdText(String adText) {
        AdText = adText;
    }
}
