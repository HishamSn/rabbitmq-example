package com.hussam.fproject.hsrw.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private final static String QUEUE_NAME = "chat_queue";
    private Thread subscribeThread;
    private Thread publishThread;
    private ConnectionFactory factory = new ConnectionFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupConnectionFactory();

        try {
            createProducer1();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


//        publishToAMQP();
//        setupPubButton();
//

    }

    private void createProducer1() throws java.io.IOException,
            TimeoutException {

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //declare a queue to send messages to
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //publish a message to the queue
        String message = "Well, that's no ordinary rabbit.";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
    }

    private void setupConnectionFactory() {
        factory.setAutomaticRecoveryEnabled(false);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
    }
}
