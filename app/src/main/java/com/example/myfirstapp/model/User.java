package com.example.myfirstapp.model;

public class User {
    private String mEmail;
    private Integer mPoints;

    public User(String email, int points) {
        mEmail = email;
        mPoints = points;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public Integer getmPoints() {
        return mPoints;
    }

    public void setmPoints(Integer mPoints) {
        this.mPoints = mPoints;
    }
}
