package com.hussam.fproject.hsrw.myapplication.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.widget.Toast;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.model.Queues;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hussam.fproject.hsrw.myapplication.util.FactoryUtils.connectionFactory;

public class CreateAccountActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    AppCompatEditText etUsername;
    @BindView(R.id.et_password)
    AppCompatEditText etPassword;
    private Thread createAccountThread;
    Connection connection;
    Channel channel;


    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

    }


    void createQueue(String userName) {
        createAccountThread = new Thread(() -> {
            try {
                //TODO(Hussam) : fix channel problem
                createConnection();
                channel.basicQos(1);
                AMQP.Queue.DeclareOk queue = channel.queueDeclare(userName, false, false, false, null);
//                channel.queueBind(queue.getQueue(), "ExchangeName", "key");
                CachedUtil.getInstance().queueList.add(new Queues(userName));
                CachedUtil.getInstance().queueNameList.add(userName);
                Toast.makeText(context, userName + " Created", Toast.LENGTH_LONG).show();
                startActivity(new Intent(context, LoginActivity.class));

            } catch (Exception e1) {
                Log.d("", "Connection broken: " + e1.getClass().getName());
                try {
                    Thread.sleep(4000); //sleep and then try again
                } catch (InterruptedException e) {
                }
            }
        });
        createAccountThread.start();
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

    @OnClick(R.id.btn_create)
    public void onViewClicked() {
        if (!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
            if (etPassword.getText().toString().equals("1")) {
                createQueue(etUsername.getText().toString());
            } else {
                Toast.makeText(this, "Please Enter Correct Password", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (createAccountThread != null) {
            createAccountThread.interrupt();
        }
    }
}
