package com.hussam.fproject.hsrw.myapplication.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.ui.FablabActivity;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    AppCompatEditText etUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    private void login() {
        if (CachedUtil.getInstance().queueNameList.contains(etUserName.getText().toString())) {
            Intent intent = new Intent(this, FablabActivity.class);
            intent.putExtra("user_name", etUserName.getText().toString());
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
