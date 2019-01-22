package com.hussam.fproject.hsrw.myapplication.ui.lib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.ui.faplab.FablabAdapter;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibGroupActivity extends AppCompatActivity {
    @BindView(R.id.rv_groups)
    RecyclerView rvGroups;


    private List<String> majorList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_group);
        ButterKnife.bind(this);
        init();

        for (int i = 0; i < CachedUtil.getInstance().queueList.size(); i++) {
            String major = CachedUtil.getInstance().queueList.get(i).getMajor();
            if (!majorList.contains(major)) {
                majorList.add(major);
            }

        }

        rvGroups.setAdapter(new LibGroupAdapter(majorList));

    }

    private void init() {
        majorList = new ArrayList<>();
    }

}
