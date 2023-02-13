package com.inhatc.primrose;

public class User {
    private String email;
    private String passwd;
    private String name;
    private String phone;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswd() {
        return passwd;
    }
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(){ }

    public User(String email, String passwd, String name, String phone) {
        this.email = email;
        this.passwd = passwd;
        this.name = name;
        this.phone = phone;
    }
}
