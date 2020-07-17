package com.hyphenate.easeui.widget.chatextend;

import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeui.adapter.EaseBaseDelegate;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;

public abstract class EaseBaseChatMenuDelegate<T, VH extends EaseBaseRecyclerViewAdapter.ViewHolder> extends EaseBaseDelegate<T, VH> {

    public abstract EaseChatExtendMenu.ChatMenuItemModel getExtendItemMenu();

}

