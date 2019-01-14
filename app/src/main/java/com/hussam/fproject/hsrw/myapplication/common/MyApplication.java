package com.hussam.fproject.hsrw.myapplication.common;


import android.app.Application;



public class MyApplication extends Application {

    private static MyApplication instance;

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

}
