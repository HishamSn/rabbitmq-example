package com.hussam.fproject.hsrw.myapplication.model;

import com.squareup.moshi.Json;

/*
 * Created by Hisham Snaimeh on 5/6/2018.
 */

public class BaseGenericWrapper<T> {

    @Json(name = "code")
    private int code;
    @Json(name = "status")
    private String status;
    @Json(name = "message")
    private String message;

    @Json(name = "data")
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
