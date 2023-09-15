package com.example.haider.resoluteaipeertopeercalling.userNameHOlder;

import java.security.SecureRandom;

public class userName {
    String user;
    String email;
    String password;
    userName(){
    }

    public userName(String user, String email, String password) {
        this.user = user;
        this.email = email;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
