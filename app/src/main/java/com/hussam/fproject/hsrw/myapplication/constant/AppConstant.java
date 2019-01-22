package com.hussam.fproject.hsrw.myapplication.constant;

public class AppConstant {
    public static final String BASE_SERVER = "192.168.1.5";
    public static final String BASE_URL_API = "http://" + BASE_SERVER + ":15672/api/";
    public static final String V_HOST = "ddd";
//    public static final String V_HOST = "h";
    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin";

    public static final String GET_QUEUES = "queues/" + V_HOST;
    public static final String EXCHANGE_DIRECT = "key0";
    public static final String EXCHANGE_FANOUT = "key1";
    public static final String EXCHANGE_TOPIC = "key2";

//    public static final String EXCHANGE_DIRECT = "amq.direct";
//    public static final String EXCHANGE_FANOUT = "amq.fanout";
//    public static final String EXCHANGE_TOPIC = "amq.topic";
}
