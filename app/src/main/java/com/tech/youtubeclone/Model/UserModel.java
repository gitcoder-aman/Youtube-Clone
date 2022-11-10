package com.tech.youtubeclone.Model;

public class UserModel {
    private String name;
    private String email;
    private String pass;
    private int followCount;

    public UserModel(String name, String email, String pass) {
        this.email = email;
        this.name = name;
        this.pass = pass;
    }

    public UserModel() {
    }


    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
