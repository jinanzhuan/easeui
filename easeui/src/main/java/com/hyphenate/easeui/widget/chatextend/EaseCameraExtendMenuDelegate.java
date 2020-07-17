package com.hyphenate.easeui.widget.chatextend;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseChatInputMenu;

public class EaseCameraExtendMenuDelegate extends EaseChatMenuDelegate{

    @Override
    public boolean isForViewType(EaseChatExtendMenu.ChatMenuItemModel item, int position) {
        return item != null && item.id == EaseChatInputMenu.ITEM_TAKE_PICTURE;
    }

    @Override
    public EaseChatExtendMenu.ChatMenuItemModel getExtendItemMenu() {
        EaseChatExtendMenu.ChatMenuItemModel item = new EaseChatExtendMenu.ChatMenuItemModel();
        item.name = EMClient.getInstance().getContext().getString(R.string.attach_take_pic);
        item.image = R.drawable.ease_chat_takepic_selector;
        item.id = EaseChatInputMenu.ITEM_TAKE_PICTURE;
        return item;
    }
}

