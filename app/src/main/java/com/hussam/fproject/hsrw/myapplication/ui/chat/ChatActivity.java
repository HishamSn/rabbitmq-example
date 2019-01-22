package com.hussam.fproject.hsrw.myapplication.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.model.Chat;
import com.hussam.fproject.hsrw.myapplication.prefs.PrefsUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.hussam.fproject.hsrw.myapplication.util.FactoryUtils.connectionFactory;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    AppCompatTextView tvName;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.et_chat_content)
    AppCompatEditText etChatContent;
    @BindView(R.id.iv_user)
    CircleImageView ivUser;
    private String userName;
    private String type;
    private String group;

    private List<Chat> chatList;
    private Context context = this;

    private Thread subscribeThread;
    private Thread publishThread;
    Connection connection;
    Channel channel;
    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
    SimpleDateFormat ft;
    Date date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        init();

        userName = getIntent().getExtras().getString("user_name");
        type = getIntent().getExtras().getString("type");
        group = getIntent().getExtras().getString("group");
        fillHeader();
        publishToAMQP();


        final Handler incomingPartnerMessagesHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                String message = msg.getData().getString("msg");
                Toast.makeText(context, "received " + message, Toast.LENGTH_SHORT).show();

                if (!type.equals("Direct")) {
                    String[] partsMsg = message.split("__#");
                    String[] userNameMsg = partsMsg[0].split("_");
                    userName = userNameMsg[1];

                    message = partsMsg[1];
                }


                Chat chat = new Chat(userName, message, ft.format(date));
                chatList.add(chat);
                rvChat.getAdapter().notifyDataSetChanged();
                rvChat.smoothScrollToPosition(rvChat.getAdapter().getItemCount()-1);

            }
        };


        subscribeMyMessagesFromPartner(incomingPartnerMessagesHandler);

    }

    private void fillHeader() {
        if (type.equals("Direct")) {
            tvName.setText(userName);
            ivUser.setBackground(getResources().getDrawable(R.drawable.ic_user));
        } else if (type.equals("Topic")) {
            tvName.setText("Library");
            ivUser.setBackground(getResources().getDrawable(R.drawable.ic_group));

        } else {
            tvName.setText("Mensa");
            ivUser.setBackground(getResources().getDrawable(R.drawable.ic_group));

        }
    }


    private void init() {
        chatList = new ArrayList<>();
        rvChat.setAdapter(new ChatAdapter(chatList));

        date = new Date();
        ft = new SimpleDateFormat("hh:mm");
        rvChat.setAdapter(new ChatAdapter(chatList));

    }


    void publishMessage(String message) {
        //Adds a message to internal blocking queue
        try {
            Log.d("", "[q] " + message);
            queue.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void publishToAMQP() {
        publishThread = new Thread(() -> {
            createConnection();
            while (true) {

                try {
                    channel.confirmSelect();

                    while (true) {
                        String message = queue.takeFirst();
                        try {

//                            channel.queueBind(queueName, EXCHANGE_NAME, "Routing_key");

                            if (type.equals("Direct")) {
                                channel.queueBind(userName, "key0", userName + "_" + PrefsUtils.getInstance().getUserName());
                                channel.basicPublish("key0", userName + "_" + PrefsUtils.getInstance().getUserName(), null, message.getBytes());
                            } else if (type.equals("Topic")) {
                                message = PrefsUtils.getInstance().getUserName() + "__#" + message;

                                channel.basicPublish("key2", group, null, message.getBytes());

                            } else {
                                message = PrefsUtils.getInstance().getUserName() + "__#" + message;
                                //Fanout
//                                channel.queueBind(userName, "key0", userName + "_" + PrefsUtils.getInstance().getUserName());
                                channel.basicPublish("key1", userName + "_all", null, message.getBytes());

                            }

                            Log.d("", "[s] " + message);

                            channel.waitForConfirmsOrDie();

                            String finalMessage = message;
                            runOnUiThread(() -> {
                                if (type.equals("Direct")) {

                                    String[] userNameMsg = PrefsUtils.getInstance().getUserName().split("_");
                                    userName = userNameMsg[1];
                                    Chat chat = new Chat(userName, finalMessage, ft.format(date));
                                    chatList.add(chat);
                                    rvChat.setAdapter(new ChatAdapter(chatList));
                                }

                                Toast.makeText(context, PrefsUtils.getInstance().getUserName() + " send to " + type, Toast.LENGTH_SHORT).show();
                            });

                        } catch (Exception e) {
                            Log.d("", "[f] " + message);
                            queue.putFirst(message);
                            throw e;
                        }
                    }
                } catch (InterruptedException e) {
//                    break;
                } catch (Exception e) {
                    Log.d("", "Connection broken: " + e.getClass().getName());
                    try {
                        Thread.sleep(5000); //sleep and then try again
                    } catch (InterruptedException e1) {
//                        break;
                    }
                }
            }
        });
        publishThread.start();
    }

    private void createConnection() {
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fab_send)
    public void onViewClicked() {
        publishMessage(etChatContent.getText().toString());
        etChatContent.setText("");
    }


    void subscribeMyMessagesFromPartner(final Handler handler) {
        subscribeThread = new Thread(() -> {
            try {
                createConnection();

//                while (true) {


                channel.basicQos(10); //Todo whats mean
//                    AMQP.Queue.DeclareOk queue = channel.queueDeclare(PrefsUtils.getInstance().getUserName(), false, false, false, null);


                if (type.equals("Direct")) {
                    channel.queueBind(PrefsUtils.getInstance().getUserName(), "key0", PrefsUtils.getInstance().getUserName() + "_" + userName);
                } else if (type.equals("Topic")) {
                    channel.queueBind(PrefsUtils.getInstance().getUserName(), "key2", group);

                } else {
                    //Fanout
                    channel.queueBind(PrefsUtils.getInstance().getUserName(), "key1", "SA");

                }


//                channel.queueBind(queue.getQueue(), "husi3", "key0");

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
                channel.basicConsume(PrefsUtils.getInstance().getUserName(), true, consumer);

//                }
            } catch (Exception e1) {
                Log.d("", "Connection broken: " + e1.getClass().getName());
                try {
                    Thread.sleep(4000); //sleep and then try again
                } catch (InterruptedException e) {
                }
            }
        });
        subscribeThread.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (publishThread != null) {
            publishThread.interrupt();
        }
        if (subscribeThread != null) {
            subscribeThread.interrupt();
        }
    }
}
