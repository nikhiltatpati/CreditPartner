package com.example.creditpartner.Classes;

public class Companies {

    String companyImage, companyName, companyInterestRate,companyMinimumBalance;

    public Companies() {
    }

    public Companies(String companyImage, String companyName, String companyInterestRate, String companyMinimumBalance) {
        this.companyImage = companyImage;
        this.companyName = companyName;
        this.companyInterestRate = companyInterestRate;
        this.companyMinimumBalance = companyMinimumBalance;
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
