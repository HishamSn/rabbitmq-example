package com.hussam.fproject.hsrw.myapplication.model;

import com.squareup.moshi.Json;

public class Queues {

    @Json(name = "name")
    private String name;

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
}
