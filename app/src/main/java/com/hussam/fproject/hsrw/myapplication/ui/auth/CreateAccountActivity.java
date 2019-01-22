package com.hussam.fproject.hsrw.myapplication.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.widget.Toast;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.model.Queues;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hussam.fproject.hsrw.myapplication.constant.AppConstant.EXCHANGE_FANOUT;
import static com.hussam.fproject.hsrw.myapplication.constant.AppConstant.EXCHANGE_TOPIC;
import static com.hussam.fproject.hsrw.myapplication.util.FactoryUtils.connectionFactory;

public class CreateAccountActivity extends AppCompatActivity {

    @BindView(R.id.et_chat_content)
    AppCompatEditText etUsername;
    @BindView(R.id.et_password)
    AppCompatEditText etPassword;
    @BindView(R.id.sp_majors)
    AppCompatSpinner spMajors;
    private Thread createAccountThread;
    Connection connection;
    Channel channel;
    private List<String> majorList;


    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        majorList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.major)));
        spMajors.setAdapter(new SpMajorAdapter(context, majorList));

    }


    void createQueue(String major, String userName) {
        createAccountThread = new Thread(() -> {
            try {
                //TODO(Hussam) : fix channel problem
                createConnection();
                channel.basicQos(1);
                AMQP.Queue.DeclareOk queue = channel.queueDeclare(major + "_" + userName, false, false, false, null);
                channel.queueBind(queue.getQueue(), EXCHANGE_FANOUT, "all");//fanout
                channel.queueBind(queue.getQueue(), EXCHANGE_TOPIC, major);//group
                CachedUtil.getInstance().queueList.add(new Queues(userName, major));
                CachedUtil.getInstance().queueNameList.add(userName);
                runOnUiThread(() -> {
                    Toast.makeText(context, userName + " Created", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                });


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
                createQueue(spMajors.getSelectedItem().toString(), etUsername.getText().toString());
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
