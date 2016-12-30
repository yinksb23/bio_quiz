package com.yinkash.bioquiz;

public class Contact {

    String name, email, uname, pass;

    public Contact() {
    }

    public Contact(String userName, String name, String email, String password) {
        this.uname = userName;
        this.email = email;
        this.pass = password;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return this.uname;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return this.pass;
    }
}
