package com.hyphenate.easeui.adapter;

import com.hyphenate.easeui.widget.chatextend.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.chatextend.EaseBaseChatMenuDelegate;
import com.hyphenate.easeui.widget.chatextend.EaseChatExtendMenu.EaseChatExtendMenuItemClickListener;

import java.util.ArrayList;

public class EaseChatExtendMenuAdapter extends EaseBaseDelegateAdapter<EaseChatExtendMenu.ChatMenuItemModel> {
    private EaseChatExtendMenuItemClickListener listener;

    public EaseChatExtendMenuAdapter() {
        if(mData == null) {
            mData = new ArrayList<>();
        }
    }

    public EaseChatExtendMenuAdapter(EaseChatExtendMenuItemClickListener listener) {
        if(mData == null) {
            mData = new ArrayList<>();
        }
        this.listener = listener;
    }

    /**
     * 设置条目监听
     * @param listener
     */
    public void setEaseChatExtendMenuItemClickListener(EaseChatExtendMenuItemClickListener listener) {
        this.listener = listener;
        if(mData != null && !mData.isEmpty()) {
            for(int i = 0; i < mData.size(); i++) {
                mData.get(i).clickListener = listener;
            }
            notifyDataSetChanged();
        }
    }

    public EaseBaseDelegateAdapter addDelegate(EaseBaseChatMenuDelegate delegate) {
        getItemMenuData(delegate);
        return super.addDelegate(delegate);
    }

    public EaseBaseDelegateAdapter addDelegate(EaseBaseChatMenuDelegate delegate, String tag) {
        getItemMenuData(delegate);
        return super.addDelegate(delegate, tag);
    }

    public EaseBaseDelegateAdapter setFallbackDelegate(EaseBaseChatMenuDelegate delegate) {
        getItemMenuData(delegate);
        return super.setFallbackDelegate(delegate);
    }

    public EaseBaseDelegateAdapter setFallbackDelegate(EaseBaseChatMenuDelegate delegate, String tag) {
        getItemMenuData(delegate);
        return super.setFallbackDelegate(delegate, tag);
    }

    private void getItemMenuData(EaseBaseChatMenuDelegate delegate) {
        EaseChatExtendMenu.ChatMenuItemModel itemMenu = delegate.getExtendItemMenu();
        itemMenu.clickListener = listener;
        mData.add(itemMenu);
    }
}

