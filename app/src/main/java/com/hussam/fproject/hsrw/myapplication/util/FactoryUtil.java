package com.hussam.fproject.hsrw.myapplication.util;

import com.rabbitmq.client.ConnectionFactory;

public class FactoryUtil extends ConnectionFactory {

    public static FactoryUtil instance;

    public FactoryUtil() {
        instance.setAutomaticRecoveryEnabled(false);
        instance.setUsername("admin");
        instance.setPassword("admin");
        instance.setVirtualHost("ddd");
        instance.setHost("192.168.1.4");
        instance.setPort(5672);
    }

    public static FactoryUtil getInstance() {
        if (instance == null) {
            instance = new FactoryUtil();
        }
        return instance;
    }
}
