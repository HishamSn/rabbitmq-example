package com.hussam.fproject.hsrw.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    private final static String QUEUE_NAME = "chat_queue3";
    private Thread subscribeThread;
    private Thread publishThread;
    private ConnectionFactory factory = new ConnectionFactory();
    Connection connection;
    Channel channel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupConnectionFactory();

        publishToAMQP();
        setupPubButton();

        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                TextView tvMessages = findViewById(R.id.messages_received);
                TextView tvDate = findViewById(R.id.tv_date);
                Date now = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                tvDate.append('\n' + ft.format(now));
                tvMessages.append('\n' + message);
            }
        };
        subscribe(incomingMessageHandler);
    }

    private void createConnection() {
        try {
            Log.d("hussam_create", "create conection");

            connection = factory.newConnection();
            channel = connection.createChannel();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    void setupPubButton() {
        Button button = findViewById(R.id.publish_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText et = findViewById(R.id.message_to_publish);
                publishMessage(et.getText().toString());
                et.setText("");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        publishThread.interrupt();
        subscribeThread.interrupt();
    }

    private BlockingDeque<String> queue = new LinkedBlockingDeque<>();

    void publishMessage(String message) {
        //Adds a message to internal blocking queue
        try {
            Log.d("", "[q] " + message);
            queue.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void setupConnectionFactory() {
//        factory.setAutomaticRecoveryEnabled(false);
//        factory.setUsername("ADMIN_USERNAME");
//        factory.setPassword("PASSWORD");
//        factory.setHost("xxx.xxx.xxx.xxx");
//        factory.setPort(5672);


        factory.setAutomaticRecoveryEnabled(false);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("ddd");
        factory.setHost("192.168.1.4");
        factory.setPort(5672);
    }

    void subscribe(final Handler handler) {
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
//                while(true) {
                Log.d("hussam_subscribe", "subscribe success while");
                try {
//                    Connection connection = factory.newConnection();
//                    Channel channel = connection.createChannel();
                    if (connection == null && channel == null) {
                        createConnection();

                    }
                    channel.basicQos(1); //Todo whats mean
                    AMQP.Queue.DeclareOk queue = channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    createExchange(channel);

                    channel.queueBind(queue.getQueue(), "husi3", "key0");


                    DefaultConsumer consumer = new DefaultConsumer(channel) {

                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope, AMQP.BasicProperties properties,
                                                   byte[] body) {
                            String message = new String(body);
                            Log.d("", "[r] " + message);

                            Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();

                            bundle.putString("msg", message);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    };

                    channel.basicConsume(queue.getQueue(), true, consumer);

                } catch (Exception e1) {
                    Log.d("", "Connection broken: " + e1.getClass().getName());
                    try {
                        Thread.sleep(4000); //sleep and then try again
                    } catch (InterruptedException e) {
//                            break;
                    }
                }
//                }
            }
        });
        subscribeThread.start();
    }

    private void createExchange(Channel channel) throws IOException {
        channel.exchangeDeclare("husi3", "fanout");
    }

    public void publishToAMQP() {
        publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (connection == null && channel == null) {
                            createConnection();

                        }

//                        Connection connection = factory.newConnection();
//                        Channel channel = connection.createChannel();
                        channel.confirmSelect();

                        while (true) {
                            String message = queue.takeFirst();
                            try {
                                channel.basicPublish("husi3", "key0", null, message.getBytes());
                                Log.d("", "[s] " + message);
                                channel.waitForConfirmsOrDie();
                            } catch (Exception e) {
                                Log.d("", "[f] " + message);
                                queue.putFirst(message);
                                throw e;
                            }
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        Log.d("", "Connection broken: " + e.getClass().getName());
                        try {
                            Thread.sleep(5000); //sleep and then try again
                        } catch (InterruptedException e1) {
                            break;
                        }
                    }
                }
            }
        });
        publishThread.start();
    }
}
