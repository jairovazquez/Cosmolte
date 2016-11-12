package com.captech.cosmonauts.cosmolte;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by jvazquez on 8/14/16.
 */
public class ProfileInfoSingleton {

    private static ProfileInfoSingleton instance = null;
    private String firstName;
    private String lastName;
    private String email;
    private TreeSet<String> nameList = new TreeSet<>();


    private ProfileInfoSingleton(){

    }

    public static ProfileInfoSingleton getInstance(){
        if(instance == null){
            instance = new ProfileInfoSingleton();
        }

        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public void addName(String name){
        nameList.add(name);
    }

    public TreeSet<String> getNameList(){
        return nameList;
    }

}
