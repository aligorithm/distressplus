package xyz.yisa.distressplus.models;

public class User {
    public int id;
    public String name;
    public String email;
    public String password;
    public String phone;
    public String player;
    public boolean status;

    public User(String fullName, String email, String phone, String password){
        this.name = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
}
