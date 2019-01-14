package com.hussam.fproject.hsrw.myapplication.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;

public class FablabActivity extends AppCompatActivity {
    private RecyclerView rvQueues;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fablab);
        init();
        rvQueues.setAdapter(new FablabAdapter(CachedUtil.getInstance().queueList));


    }

    private void init() {
        rvQueues = findViewById(R.id.rv_queues);
    }
}
