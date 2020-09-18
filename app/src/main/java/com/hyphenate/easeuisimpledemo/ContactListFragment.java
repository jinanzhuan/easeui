package com.hyphenate.easeuisimpledemo;

import android.view.View;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;

public class ContactListFragment extends EaseContactListFragment {
    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        EaseUser item = adapter.getItem(position);
        ChatActivity.actionStart(getActivity(), item.getUsername(), 1);
    }
}

