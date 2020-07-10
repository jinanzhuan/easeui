package com.hyphenate.easeuisimpledemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEtLoginName;
    private EditText mEtLoginPwd;
    private Button mBtnLogin;

    private String mUserName;
    private String mPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtLoginName = findViewById(R.id.et_login_name);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mBtnLogin = findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login :
                loginToServer();
                break;
        }
    }

    private void loginToServer() {
        mUserName = mEtLoginName.getText().toString().trim();
        mPwd = mEtLoginPwd.getText().toString().trim();
        if(TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPwd)) {
            Toast.makeText(LoginActivity.this, "用户名或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        EMClient.getInstance().login(mUserName, mPwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                runOnUiThread(()-> {
                    Toast.makeText(LoginActivity.this, "error code："+code+ " error message:"+error, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
