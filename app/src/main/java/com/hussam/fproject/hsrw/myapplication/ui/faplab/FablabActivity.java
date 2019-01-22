package com.hussam.fproject.hsrw.myapplication.ui.faplab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FablabActivity extends AppCompatActivity {

    @BindView(R.id.rv_users)
    RecyclerView rvUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fablab);
        ButterKnife.bind(this);
        rvUsers.setAdapter(new FablabAdapter(CachedUtil.getInstance().queueList));


    }


}
