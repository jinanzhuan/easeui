package com.hyphenate.easeui.widget.chatextend;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;

public abstract class EaseChatMenuDelegate extends EaseBaseChatMenuDelegate<EaseChatExtendMenu.ChatMenuItemModel, EaseChatMenuDelegate.ViewHolder> {

    @Override
    protected int getLayoutId() {
        return R.layout.ease_chat_menu_item;
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    public static class ViewHolder extends EaseBaseRecyclerViewAdapter.ViewHolder<EaseChatExtendMenu.ChatMenuItemModel> {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            imageView = (ImageView) findViewById(R.id.image);
            textView = (TextView) findViewById(R.id.text);
        }

        @Override
        public void setData(EaseChatExtendMenu.ChatMenuItemModel item, int position) {
            imageView.setBackgroundResource(item.image);
            textView.setText(item.name);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(item.clickListener != null){
                        item.clickListener.onChatExtendMenuItemClick(item.id, v);
                    }
                }
            });
        }
    }
}

