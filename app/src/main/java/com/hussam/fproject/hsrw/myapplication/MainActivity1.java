package com.hussam.fproject.hsrw.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity1 extends AppCompatActivity {
    private final static String QUEUE_NAME = "chat_queue";
    private Thread subscribeThread;
    private Context context = this;
    private Thread publishThread;
    private ConnectionFactory factory;
    private BlockingDeque<String> queue = new LinkedBlockingDeque<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            setupConnectionFactory();

        publishToAMQP();

        setupPubButton();

        incomingMessage();
    }

    private void incomingMessage() {
        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                TextView tv = findViewById(R.id.messages_received);
                Date now = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                tv.append(ft.format(now) + ' ' + message + '\n');
            }
        };
        subscribe(incomingMessageHandler);
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


    void publishMessage(String message) {
        //Adds a message to internal blocking queue
        try {
            Log.d("MainKey", "[q] " + message);
            queue.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void setupConnectionFactory() {
        factory = new ConnectionFactory();
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
                while (true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.basicQos(1);
                        AMQP.Queue.DeclareOk q = channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                        Log.d("MainKey", "amq.fanout.test " + "chat");
                        channel.queueBind(q.getQueue(), "amq.fanout.test", QUEUE_NAME);
                        DefaultConsumer consumer = new DefaultConsumer(channel) {

                            @Override
                            public void handleDelivery(String consumerTag,
                                                       Envelope envelope, AMQP.BasicProperties properties,
                                                       byte[] body) throws IOException {
                                String message = new String(body);
                                Log.d("MainKey", "[r] " + message);

                                Message msg = handler.obtainMessage();
                                Bundle bundle = new Bundle();

                                bundle.putString("msg", message);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        };

                        channel.basicConsume(q.getQueue(), true, consumer);

                    } catch (Exception e1) {
                        Log.d("MainKey", "Connection broken: " + e1.getClass().getName());
                        try {
                            Thread.sleep(4000); //sleep and then try again
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            }
        });
        subscribeThread.start();
    }

    public void publishToAMQP() {
        publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.confirmSelect();

                        while (true) {
                            String message = queue.takeFirst();
                            try {
                                channel.basicPublish("amq.fanout", "chat", null, message.getBytes());
                                Log.d("MainKey", "[s] " + message);
                                channel.waitForConfirmsOrDie();
                            } catch (Exception e) {
                                Log.d("MainKey", "[f] " + message);
                                queue.putFirst(message);
                                throw e;
                            }
                        }
                    } catch (InterruptedException e) {
                        Log.d("MainKey", "factory broken: " + e.getClass().getName());

                        break;
                    } catch (Exception e) {
                        Log.d("MainKey", "Connection broken: " + e.getClass().getName());
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
