package com.hyphenate.easeuisimpledemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.hyphenate.easeui.ui.EaseChatFragment;

public class ChatActivity extends AppCompatActivity {
    
    public static void actionStart(Context context, String userId, int chatType) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("chatType", chatType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initListener();
        initData();
    }

    private void initView() {

    }

    private void initListener() {

    }

    private void initData() {
        String userId = getIntent().getStringExtra("userId");
        int chatType = getIntent().getIntExtra("chatType", 1);
        Fragment chat = getSupportFragmentManager().findFragmentByTag("chat");
        if(chat == null) {
            chat = new EaseChatFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            bundle.putInt("chatType", chatType);
            chat.setArguments(bundle);
        }
        if(chat.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(chat).commit();
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment, chat, "chat").show(chat).commit();
        }

    }
}

