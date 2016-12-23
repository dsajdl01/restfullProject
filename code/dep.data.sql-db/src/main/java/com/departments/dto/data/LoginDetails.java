package com.departments.dto.data;

/**
 * Created by david on 18/12/16.
 */
public class LoginDetails {

    private String email;

    private String password;

    public LoginDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
