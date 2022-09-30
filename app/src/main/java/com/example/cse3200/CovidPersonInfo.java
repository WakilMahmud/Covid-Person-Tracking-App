package com.example.cse3200;

public class CovidPersonInfo {
    String name;
    String age;
    String email;
    String country;
    String COVID_ID;
    String lat;
    String lng;

    public CovidPersonInfo() {

    }

    public CovidPersonInfo(String name,  String age, String email, String country, String COVID_ID, String lat, String lng) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.country = country;
        this.COVID_ID = COVID_ID;
        this.lat = lat;
        this.lng = lng;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getCOVID_ID() {
        return COVID_ID;
    }
    public void setCOVID_ID(String id) {
        this.COVID_ID = id;
    }

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
}
