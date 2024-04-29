package com.example.hospital_management_app;

public class ReadWriteUserDetails {
    public String Username,dob,gender;
    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textdob,String textGender){

        this.dob=textdob;
        this.gender=textGender;
    }
}
