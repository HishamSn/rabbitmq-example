package com.hussam.fproject.hsrw.myapplication.util;

import com.rabbitmq.client.ConnectionFactory;

public class FactoryUtils {

   public static ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void setupFactory() {
        connectionFactory.setAutomaticRecoveryEnabled(false);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("ddd");
        connectionFactory.setHost("192.168.1.4");
        connectionFactory.setPort(5672);
    }
}
