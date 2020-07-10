package com.hyphenate.easeuisimpledemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplash = findViewById(R.id.iv_splash);

        Glide.with(this)
                .load(R.drawable.em_splash_bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder(500).setCrossFadeEnabled(true).build()))
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loginSDK();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loginSDK();
                        return false;
                    }
                })
                .into(ivSplash);
    }

    private void loginSDK() {
        new Thread(()-> {
            if(EMClient.getInstance().isLoggedInBefore()) {
                loadAllConversationsAndGroups();
                //跳转到主页面
                runOnUiThread(() -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                });
            }else {
                //跳转到登录页面
                runOnUiThread(()-> {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
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
