package com.example.creditpartner.Classes;

public class Users {

   String userName, userEmail, userNumber;

    public Users() {
    }


    public Users(String userName, String userEmail, String userNumber) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userNumber = userNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
