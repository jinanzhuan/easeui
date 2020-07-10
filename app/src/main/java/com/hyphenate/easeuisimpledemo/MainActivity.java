package com.hyphenate.easeuisimpledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.ui.base.EaseBaseFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.easeuisimpledemo.permission.PermissionsManager;
import com.hyphenate.easeuisimpledemo.permission.PermissionsResultAction;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView navView;
    private EaseTitleBar mTitleBar;
    private EaseBaseFragment mConversationListFragment, mFriendsFragment, mDiscoverFragment, mAboutMeFragment;
    private EaseBaseFragment mCurrentFragment;
    private TextView mTvMainHomeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        navView = findViewById(R.id.nav_view);
        mTitleBar = findViewById(R.id.title_bar_main);
        navView.setItemIconTintList(null);
        // 可以动态显示隐藏相应tab
        //navView.getMenu().findItem(R.id.em_main_nav_me).setVisible(false);
        switchToHome();
    }

    private void initListener() {
        navView.setOnNavigationItemSelectedListener(this);
    }

    private void initData() {
        requestPermissions();
        checkUnreadMsg();
    }

    private void checkUnreadMsg() {
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        String count = getUnreadCount(unreadMessageCount);
//        if(!TextUtils.isEmpty(count)) {
//            mTvMainHomeMsg.setVisibility(View.VISIBLE);
//            mTvMainHomeMsg.setText(readCount);
//        }else {
//            mTvMainHomeMsg.setVisibility(View.GONE);
//        }
    }

    /**
     * 获取未读消息数目
     * @param count
     * @return
     */
    private String getUnreadCount(int count) {
        if(count <= 0) {
            return null;
        }
        if(count > 99) {
            return "99+";
        }
        return String.valueOf(count);
    }

    private void requestPermissions() {
        PermissionsManager.getInstance()
                .requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(String permission) {

                    }
                });
    }

    private void switchToHome() {
        if(mConversationListFragment == null) {
            mConversationListFragment = new EaseConversationListFragment();
        }
        replace(mConversationListFragment);
    }

    private void switchToFriends() {
        if(mFriendsFragment == null) {
            mFriendsFragment = new EaseContactListFragment();
        }
        replace(mFriendsFragment);
    }

//    private void switchToDiscover() {
//        if(mDiscoverFragment == null) {
//            mDiscoverFragment = new DiscoverFragment();
//        }
//        replace(mDiscoverFragment);
//    }
//
//    private void switchToAboutMe() {
//        if(mAboutMeFragment == null) {
//            mAboutMeFragment = new AboutMeFragment();
//        }
//        replace(mAboutMeFragment);
//    }

    private void replace(EaseBaseFragment fragment) {
        if(mCurrentFragment != fragment) {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            if(mCurrentFragment != null) {
                t.hide(mCurrentFragment);
            }
            mCurrentFragment = fragment;
            if(!fragment.isAdded()) {
                t.add(R.id.fl_main_fragment, fragment).show(fragment).commit();
            }else {
                t.show(fragment).commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mTitleBar.setVisibility(View.VISIBLE);
        switch (menuItem.getItemId()) {
            case R.id.em_main_nav_home :
                switchToHome();
                mTitleBar.setTitle(getResources().getString(R.string.em_main_title_home));
                return true;
            case R.id.em_main_nav_friends :
                switchToFriends();
                mTitleBar.setTitle(getResources().getString(R.string.em_main_title_friends));
                return true;
//            case R.id.em_main_nav_discover :
//                switchToDiscover();
//                mTitleBar.setTitle(getResources().getString(R.string.em_main_title_discover));
//                return true;
//            case R.id.em_main_nav_me :
//                switchToAboutMe();
//                mTitleBar.setVisibility(View.GONE);
//                return true;
        }
        return false;
    }
}
