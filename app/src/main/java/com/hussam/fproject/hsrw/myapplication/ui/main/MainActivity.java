package com.hussam.fproject.hsrw.myapplication.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.prefs.PrefsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_label_room)
    AppCompatTextView tvLabelRoom;
    private RecyclerView rvMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        String[] userName = PrefsUtils.getInstance().getUserName().split("_");

        tvLabelRoom.setText("Welcome "+userName[1] + " to HSRW Chat , Please Select your Room ");
        rvMain.setAdapter(new MainAdapter());


    }

    private void init() {
        rvMain = findViewById(R.id.rv_main);
    }
}
