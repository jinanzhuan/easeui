package com.hyphenate.easeui.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.base.EaseBaseActivity;
import com.hyphenate.player.EasyVideoCallback;
import com.hyphenate.player.EasyVideoPlayer;

import java.io.Serializable;

public class EaseShowLocalVideoActivity extends EaseBaseActivity implements EasyVideoCallback {
    private EasyVideoPlayer evpPlayer;
    private Uri uri;

    public static void actionStart(Context context, String path) {
        Intent intent = new Intent(context, EaseShowLocalVideoActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.ease_activity_show_local_video);
        setFitSystemForTheme(false, R.color.transparent, false);
        initIntent();
        initView();
        initListener();
        initData();
    }

    public void initIntent() {
        String path = getIntent().getStringExtra("path");
        if(!TextUtils.isEmpty(path)) {
            uri = Uri.parse(path);
        }
        if(uri == null) {
            finish();
        }
    }

    public void initView() {
        evpPlayer = findViewById(R.id.evp_player);
    }

    public void initListener() {
        evpPlayer.setCallback(this);
    }

    public void initData() {
        evpPlayer.setAutoPlay(true);
        if(uri != null) {
            evpPlayer.setSource(uri);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(evpPlayer != null) {
            evpPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(evpPlayer != null) {
            evpPlayer.release();
            evpPlayer = null;
        }
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onClickVideoFrame(EasyVideoPlayer player) {

    }
}

