package com.project.stephencao.remusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.project.stephencao.remusic.R;

public class SplashActivity extends Activity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initAnimation();
    }

    // set the alpha animation
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setDuration(3000);
        mImageView.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView() {
        mImageView = findViewById(R.id.iv_splash_activity_image);
    }
}
