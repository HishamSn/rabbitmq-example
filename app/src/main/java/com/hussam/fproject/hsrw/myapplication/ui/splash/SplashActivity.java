package com.hussam.fproject.hsrw.myapplication.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.hussam.fproject.hsrw.myapplication.R;
import com.hussam.fproject.hsrw.myapplication.model.Queues;
import com.hussam.fproject.hsrw.myapplication.network.HttpStatus;
import com.hussam.fproject.hsrw.myapplication.remote.QueueRemoteDao;
import com.hussam.fproject.hsrw.myapplication.ui.auth.LoginActivity;
import com.hussam.fproject.hsrw.myapplication.util.CachedUtil;
import com.squareup.picasso.Picasso;

import static com.hussam.fproject.hsrw.myapplication.util.FactoryUtils.setupFactory;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivSplash;
    private AnimationListener.Stop stopAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivSplash = findViewById(R.id.iv_splash);
        setupFactory();
        Picasso.with(this).load(R.drawable.logo).into(ivSplash);
        openActivityAnimationStop();

        initAnimation();


        YoYo.with(Techniques.RotateInDownRight)
                .duration(3000)
                .playOn(findViewById(R.id.tv_app_name));
    }


    private void openActivityAnimationStop() {
        stopAnimation = () -> {
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
                    goToActivity(LoginActivity.class);
                    finish();
                }

            });


        };
    }

    private void goToActivity(Class activity) {
        startActivity(new Intent
                (this, activity));
    }

    private void initAnimation() {
        ViewAnimator
                .animate(ivSplash)
                .scale(0, 1)
                .onStop(stopAnimation)
                .start();
    }
}
