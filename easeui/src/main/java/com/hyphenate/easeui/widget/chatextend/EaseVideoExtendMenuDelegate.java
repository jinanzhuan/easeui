package com.hyphenate.easeui.widget.chatextend;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseChatInputMenu;

public class EaseVideoExtendMenuDelegate extends EaseChatMenuDelegate{

    @Override
    public boolean isForViewType(EaseChatExtendMenu.ChatMenuItemModel item, int position) {
        return item != null && item.id == EaseChatInputMenu.ITEM_VIDEO;
    }

    @Override
    public EaseChatExtendMenu.ChatMenuItemModel getExtendItemMenu() {
        EaseChatExtendMenu.ChatMenuItemModel item = new EaseChatExtendMenu.ChatMenuItemModel();
        item.name = EMClient.getInstance().getContext().getString(R.string.attach_video);
        item.image = R.drawable.em_chat_video_selector;
        item.id = EaseChatInputMenu.ITEM_VIDEO;
        return item;
    }
}

