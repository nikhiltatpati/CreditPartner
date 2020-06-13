package com.app.creditpartner.Classes;

public class Expenses {

    String categoryName, expenseValue, date;

    public Expenses() {
    }

    public Expenses(String categoryName, String expenseValue, String date) {
        this.categoryName = categoryName;
        this.expenseValue = expenseValue;
        this.date = date;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExpenseValue() {
        return expenseValue;
    }

    public void setExpenseValue(String expenseValue) {
        this.expenseValue = expenseValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
