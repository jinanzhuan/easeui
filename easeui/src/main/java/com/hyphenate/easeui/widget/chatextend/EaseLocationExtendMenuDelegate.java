package com.hyphenate.easeui.widget.chatextend;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseChatInputMenu;

public class EaseLocationExtendMenuDelegate extends EaseChatMenuDelegate{

    @Override
    public boolean isForViewType(EaseChatExtendMenu.ChatMenuItemModel item, int position) {
        return item != null && item.id == EaseChatInputMenu.ITEM_LOCATION;
    }

    @Override
    public EaseChatExtendMenu.ChatMenuItemModel getExtendItemMenu() {
        EaseChatExtendMenu.ChatMenuItemModel item = new EaseChatExtendMenu.ChatMenuItemModel();
        item.name = EMClient.getInstance().getContext().getString(R.string.attach_location);
        item.image = R.drawable.ease_chat_location_selector;
        item.id = EaseChatInputMenu.ITEM_LOCATION;
        return item;
    }
}

