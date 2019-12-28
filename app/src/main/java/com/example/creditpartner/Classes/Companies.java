package com.example.creditpartner.Classes;

public class Companies {

    String companyImage, companyName, value1, value2, field1, field2, features;

    public Companies() {
    }

    public Companies(String companyImage, String companyName, String value1, String value2, String field1, String field2, String features) {
        this.companyImage = companyImage;
        this.companyName = companyName;
        this.value1 = value1;
        this.value2 = value2;
        this.field1 = field1;
        this.field2 = field2;
        this.features = features;
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

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }
}