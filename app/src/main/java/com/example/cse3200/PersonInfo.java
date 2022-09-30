package com.example.cse3200;

public class PersonInfo {
    String name;
    String age;
    String country;
    String email;
    String id;

    public PersonInfo() {

    }

    public PersonInfo(String name,  String age, String country, String email, String id) {
        this.name = name;
        this.age = age;
        this.country = country;
        this.email = email;
        this.id = id;
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

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}

