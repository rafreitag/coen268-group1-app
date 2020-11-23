package com.example.fitnessmatch;

public class Profile {
    private String name;
    private String email;

    public Profile(){
        this.name = "Error";
        this.email = "Error";
    }
    public Profile(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
