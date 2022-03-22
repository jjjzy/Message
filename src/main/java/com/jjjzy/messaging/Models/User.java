package com.jjjzy.messaging.Models;

import com.jjjzy.messaging.Enums.Gender;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String Password;
    private String Nickname;
    private String Email;
    private String address;
    private Gender Gender;
    private Date RegistrationTime;
    private Date LastLoginTime;
    private String LoginToken;

    private boolean isValid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public com.jjjzy.messaging.Enums.Gender getGender() {
        return Gender;
    }

    public void setGender(com.jjjzy.messaging.Enums.Gender gender) {
        Gender = gender;
    }

    public Date getRegistrationTime() {
        return RegistrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        RegistrationTime = registrationTime;
    }

    public Date getLastLoginTime() {
        return LastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        LastLoginTime = lastLoginTime;
    }

    public String getLoginToken() {
        return LoginToken;
    }

    public void setLoginToken(String loginToken) {
        LoginToken = loginToken;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + Password + '\'' +
                ", email='" + Email + '\'' +
                ", nickname='" + Nickname + '\'' +
                ", gender=" + Gender +
                ", address='" + address + '\'' +
                ", registerTime=" + RegistrationTime +
                ", loginToken='" + LoginToken + '\'' +
                ", lastLoginTime=" + LastLoginTime +
                ", isValid=" + isValid +
                '}';
    }

}
