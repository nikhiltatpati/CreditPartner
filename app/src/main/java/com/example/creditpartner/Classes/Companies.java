package com.example.creditpartner.Classes;

public class Companies {

    String companyImage, companyName;

    public Companies() {
    }

    public Companies(String companyImage, String companyName) {
        this.companyImage = companyImage;
        this.companyName = companyName;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public void setCompanyImage(String companyImage) {
        this.companyImage = companyImage;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
