package com.hussam.fproject.hsrw.myapplication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

public class Producer {

    private final static String QUEUE_NAME = "messenger";
    private Thread subscribeThread;
    private Thread publishThread;
    private ConnectionFactory factory = new ConnectionFactory();
    public static void createProducer() throws java.io.IOException,
            TimeoutException {

        //create a connection to the server:
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("client");
        factory.setPassword("client"); // insert the IP of the machine where the server is running ( you can check the IP via the command line utilities IPCONFIG or IFCONFIG)
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //declare a queue to send messages to
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);


        //publish a message to the queue
        String message = "Well, that's no ordinary rabbit.";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        //Close the channel and then the connection:
        channel.close();
        connection.close();
    }
}

