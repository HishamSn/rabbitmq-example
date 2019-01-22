package com.hussam.fproject.hsrw.myapplication.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.hussam.fproject.hsrw.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        rvMain.setAdapter(new MainAdapter());


    }

    private void init() {
        rvMain = findViewById(R.id.rv_main);
    }
}
