package com.saveetha.ayurnutrition;

public class Patient {
    public int id;
    public String firstName;
    public String lastName;
    public String age;
    public String gender;
    public String email;
    public String phone;
    public String dob;
    public String address;
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
