package com.hyphenate.easeuisimpledemo;

import android.view.View;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;

public class ConversationListFragment extends EaseConversationListFragment {

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        Object item = listAdapter.getItem(position);
        if(item instanceof EMConversation) {
            ChatActivity.actionStart(mContext, ((EMConversation)item).conversationId(), EaseCommonUtils.getChatType((EMConversation) item));
        }
    }
}

