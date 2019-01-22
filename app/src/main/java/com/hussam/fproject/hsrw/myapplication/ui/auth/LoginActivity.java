package com.hussam.fproject.hsrw.myapplication.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.model.Queues;
import com.hussam.fproject.hsrw.myapplication.network.HttpStatus;
import com.hussam.fproject.hsrw.myapplication.remote.QueueRemoteDao;
import com.hussam.fproject.hsrw.myapplication.ui.main.MainActivity;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;
import com.hussam.fproject.hsrw.myapplication.util.SessionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_chat_content)
    AppCompatEditText etUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        refreshUserList();

    }

    private void refreshUserList() {
        QueueRemoteDao.getInstance().getQueuesList().enqueue(result -> {
            if (result.getStatus() == HttpStatus.SUCCESS) {
                CachedUtil.getInstance().queueNameList.clear();
                CachedUtil.getInstance().queueList.clear();
                for (int i = 0; i < result.getResult().size(); i++) {
                    String[] users = result.getResult().get(i).getName().split("_");
                    Queues user = new Queues(users[1], users[0]);
                    CachedUtil.getInstance().queueList.add(user);
                    CachedUtil.getInstance().queueNameList.add(users[1]);
                }
            }

        });
    }

    private void login() {
        if (CachedUtil.getInstance().queueNameList.contains(etUserName.getText().toString())) {
            Intent intent = new Intent(this, MainActivity.class);

            int index = CachedUtil.getInstance().queueNameList.indexOf(etUserName.getText().toString());


            Queues user = CachedUtil.getInstance().queueList.get(index);
            SessionUtil.getInstance().login(user.getMajor() + "_" + user.getName());

            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.user_name_not_exist), Toast.LENGTH_LONG).show();
        }
    }


    @OnClick({R.id.btn_login, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (!etUserName.getText().toString().isEmpty()) {
                    login();
                } else {
                    etUserName.setError("Please Enter UserName");
                }
                break;
            case R.id.btn_create:
                startActivity(new Intent(this, CreateAccountActivity.class));
                break;
        }
    }
}
