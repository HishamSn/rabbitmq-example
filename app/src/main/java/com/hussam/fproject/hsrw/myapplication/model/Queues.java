package com.hussam.fproject.hsrw.myapplication.model;

import com.squareup.moshi.Json;

public class Queues {

    @Json(name = "name")
    private String name;

    private String major;


    public Queues(String name, String major) {
        this.name = name;
        this.major = major;
    }

    public Queues(String name) {
        this.name = name;
    }

    public Queues() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
