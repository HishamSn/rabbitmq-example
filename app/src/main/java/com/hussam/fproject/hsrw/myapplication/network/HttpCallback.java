package com.hussam.fproject.hsrw.myapplication.network;


public interface HttpCallback<T> {

    void onResult(HttpResult<T> result);
}
