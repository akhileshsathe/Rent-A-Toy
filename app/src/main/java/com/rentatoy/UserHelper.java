package com.rentatoy;
import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class UserHelper {
    public String uid;
    public String userType;
    public String name;

    public String contact;
    public String email;
    public String address;
    public String dob;
    public String city;
    public boolean isSuspended;
    public long registedOn;

    public UserHelper(){}


    public UserHelper(String uid, String userType, String name, String contact, String email,boolean isSuspended,long regisstedOn) {
        this.uid = uid;
        this.userType = userType;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.isSuspended=isSuspended;
        this.registedOn=regisstedOn;
    }


    public UserHelper(String uid, String userType, String name, String contact, String email, String address,String city,boolean isSuspended,long regisstedOn) {
        this.uid = uid;
        this.userType = userType;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.address = address;
        this.city=city;
        this.isSuspended=isSuspended;
        this.registedOn=regisstedOn;

    }

    public UserHelper(String uid, String userType, String name, String contact, String email,String city,boolean isSuspended,String dob,long regisstedOn) {
        this.uid = uid;
        this.userType = userType;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.city=city;
        this.dob=dob;
        this.isSuspended=isSuspended;
        this.registedOn=regisstedOn;
    }

}