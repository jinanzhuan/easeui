package com.hyphenate.easeuisimpledemo;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.hyphenate.chat.EMClient;

public class SplashActivity extends AppCompatActivity {
    private ImageView ivSplash;
    private TextView tvProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplash = findViewById(R.id.iv_splash);
        tvProduct = findViewById(R.id.tv_product);

        ivSplash.animate()
                .alpha(1)
                .setDuration(500)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginSDK();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();

        tvProduct.animate()
                .alpha(1)
                .setDuration(500)
                .start();
    }

    private void loginSDK() {
        new Thread(()-> {
            if(EMClient.getInstance().isLoggedInBefore()) {
                loadAllConversationsAndGroups();
                //跳转到主页面
                runOnUiThread(() -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                });
            }else {
                //跳转到登录页面
                runOnUiThread(()-> {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                });
            }
        }).start();
    }

    private void loadAllConversationsAndGroups() {
        // 从本地数据库加载所有的对话及群组
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
    }

}
