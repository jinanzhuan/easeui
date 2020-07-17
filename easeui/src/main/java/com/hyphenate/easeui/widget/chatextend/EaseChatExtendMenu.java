package com.hyphenate.easeui.widget.chatextend;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseBaseChatExtendMenuAdapter;
import com.hyphenate.easeui.adapter.EaseChatExtendMenuAdapter;
import com.hyphenate.util.DensityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Extend menu when user want send image, voice clip, etc
 *
 */
public class EaseChatExtendMenu extends RecyclerView {
    protected Context context;
    private List<ChatMenuItemModel> itemModels = new ArrayList<ChatMenuItemModel>();
    private EaseChatExtendMenuAdapter adapter;
    private int numColumns;
    private int numRows;
    private PagingScrollHelper helper;
    private EaseChatExtendMenuItemClickListener listener;

    public EaseChatExtendMenu(Context context) {
        this(context, null);
    }

    public EaseChatExtendMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EaseChatExtendMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseChatExtendMenu);
        numColumns = ta.getInt(R.styleable.EaseChatExtendMenu_numColumns, 4);
        numRows = ta.getInt(R.styleable.EaseChatExtendMenu_numRows, 2);
        ta.recycle();
    }
    
    /**
     * init
     */
    public void init(){
        HorizontalPageLayoutManager manager = new HorizontalPageLayoutManager(numRows, numColumns);
        manager.setItemHeight(DensityUtil.dip2px(context, 90));
        setLayoutManager(manager);
        setHasFixedSize(true);
        adapter = new EaseChatExtendMenuAdapter();
        addDelegate();
        adapter.setData(itemModels);
        setAdapter(adapter);

        helper = new PagingScrollHelper();
        helper.setUpRecycleView(this);
        helper.updateLayoutManger();
        helper.scrollToPosition(0);
        setHorizontalFadingEdgeEnabled(true);
    }

    private void addDelegate() {
        adapter.setEaseChatExtendMenuItemClickListener(listener);

    }

    /**
     * 注册条目布局
     * @param listener
     */
    public void registerMenuItemListener(EaseChatExtendMenuItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * register menu item
     * 
     * @param name
     *            item name
     * @param drawableRes
     *            background of item
     * @param itemId
     *             id
     * @param listener
     *            on click event of item
     */
    public void registerMenuItem(String name, int drawableRes, int itemId, EaseChatExtendMenuItemClickListener listener) {
        ChatMenuItemModel item = new ChatMenuItemModel();
        item.name = name;
        item.image = drawableRes;
        item.id = itemId;
        item.clickListener = listener;
        itemModels.add(item);
    }

    /**
     * register menu item
     * 
     * @param nameRes
     *            resource id of item name
     * @param drawableRes
     *            background of item
     * @param itemId
     *             id
     * @param listener
     *             on click event of item
     */
    public void registerMenuItem(int nameRes, int drawableRes, int itemId, EaseChatExtendMenuItemClickListener listener) {
        registerMenuItem(context.getString(nameRes), drawableRes, itemId, listener);
    }

    /**
     * extend menu item click listener
     */
    public interface EaseChatExtendMenuItemClickListener{
        /**
         * item click
         * @param itemId
         * @param view
         */
        void onChatExtendMenuItemClick(int itemId, View view);
    }
    
    public static class ChatMenuItemModel{
        public String name;
        public int image;
        public int id;
        public EaseChatExtendMenuItemClickListener clickListener;
    }
    
    class ChatMenuItem extends LinearLayout {
        private ImageView imageView;
        private TextView textView;

        public ChatMenuItem(Context context, AttributeSet attrs, int defStyle) {
            this(context, attrs);
        }

        public ChatMenuItem(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }

        public ChatMenuItem(Context context) {
            super(context);
            init(context, null);
        }

        private void init(Context context, AttributeSet attrs) {
            LayoutInflater.from(context).inflate(R.layout.ease_chat_menu_item, this);
            imageView = (ImageView) findViewById(R.id.image);
            textView = (TextView) findViewById(R.id.text);
        }

        public void setImage(int resid) {
            imageView.setBackgroundResource(resid);
        }

        public void setText(int resid) {
            textView.setText(resid);
        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
