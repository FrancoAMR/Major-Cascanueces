package com.majorcascanueces.psa.data.models;

public class User {
    private final String photoUrl;
    private final String name;
    private final String email;

    public User (String name, String email, String photo) {
        this.name = name;
        this.email = email;
        this.photoUrl = photo;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }
}
