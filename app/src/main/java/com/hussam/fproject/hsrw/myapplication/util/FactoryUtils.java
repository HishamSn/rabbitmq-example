package com.hussam.fproject.hsrw.myapplication.util;

import com.rabbitmq.client.ConnectionFactory;

import static com.hussam.fproject.hsrw.myapplication.constant.AppConstant.BASE_SERVER;
import static com.hussam.fproject.hsrw.myapplication.constant.AppConstant.PASSWORD;
import static com.hussam.fproject.hsrw.myapplication.constant.AppConstant.USER_NAME;
import static com.hussam.fproject.hsrw.myapplication.constant.AppConstant.V_HOST;

public class FactoryUtils {

    public static ConnectionFactory connectionFactory = new ConnectionFactory();

    public static void setupFactory() {
        connectionFactory.setAutomaticRecoveryEnabled(false);
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setVirtualHost(V_HOST);
        connectionFactory.setHost(BASE_SERVER);
        connectionFactory.setPort(5672);
    }
}
