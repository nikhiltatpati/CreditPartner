package com.example.creditpartner.Classes;

public class Slides {

    private String adImage, adText, adLink;

    public Slides() {
    }

    public Slides(String adImage, String adText, String adLink) {
        this.adImage = adImage;
        this.adText = adText;
        this.adLink = adLink;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getAdText() {
        return adText;
    }

    public void setAdText(String adText) {
        this.adText = adText;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }
}
