package com.app.creditpartner.Classes;

public class Products {

    String productName, productImage;

    public Products() {
    }

    public Products(String productName, String productImage) {
        this.productName = productName;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
