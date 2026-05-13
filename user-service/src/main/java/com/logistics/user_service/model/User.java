package com.logistics.user_service.model;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;

    public User(){

    }
    public User(long id , String firstName , String lastName , String Email ,String Password, String Role){
        this.id  = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = Email;
        this.password = Password;
        this.role = Role;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
