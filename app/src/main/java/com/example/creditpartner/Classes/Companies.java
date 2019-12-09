package com.example.creditpartner.Classes;

public class Companies {

    String companyImage, companyName, companyInterestRate,companyMinimumBalance, companyFeatures;

    public Companies() {
    }

    public Companies(String companyImage, String companyName, String companyInterestRate, String companyMinimumBalance, String companyFeatures) {
        this.companyImage = companyImage;
        this.companyName = companyName;
        this.companyInterestRate = companyInterestRate;
        this.companyMinimumBalance = companyMinimumBalance;
        this.companyFeatures = companyFeatures;
    }

    public String getCompanyFeatures() {
        return companyFeatures;
    }

    public void setCompanyFeatures(String companyFeatures) {
        this.companyFeatures = companyFeatures;
    }

    public String getCompanyInterestRate() {
        return companyInterestRate;
    }

    public void setCompanyInterestRate(String companyInterestRate) {
        this.companyInterestRate = companyInterestRate;
    }

    public String getCompanyMinimumBalance() {
        return companyMinimumBalance;
    }

    public void setCompanyMinimumBalance(String companyMinimumBalance) {
        this.companyMinimumBalance = companyMinimumBalance;
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
