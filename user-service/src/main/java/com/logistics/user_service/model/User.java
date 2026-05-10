package com.logistics.user_service.model;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public User(){

    }
    public User(long id , String firstName , String lastName , String Email , String Role){
        this.id  = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = Email;
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
        id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        role = role;
    }
}
