package com.hyphenate.easeui.manager;

import com.hyphenate.easeui.adapter.EaseChatExtendMenuAdapter;
import com.hyphenate.easeui.widget.chatextend.EaseCameraExtendMenuDelegate;
import com.hyphenate.easeui.widget.chatextend.EaseChatMenuDelegate;
import com.hyphenate.easeui.widget.chatextend.EaseFileExtendMenuDelegate;
import com.hyphenate.easeui.widget.chatextend.EaseImageExtendMenuDelegate;
import com.hyphenate.easeui.widget.chatextend.EaseLocationExtendMenuDelegate;
import com.hyphenate.easeui.widget.chatextend.EaseVideoExtendMenuDelegate;

import java.util.ArrayList;
import java.util.List;

public class EaseChatMenuExtendRegisterManager {
    private static EaseChatMenuExtendRegisterManager instance;
    private List<EaseChatMenuDelegate> delegates;

    private EaseChatMenuExtendRegisterManager(){
        delegates = new ArrayList<>();
    }

    public static EaseChatMenuExtendRegisterManager getInstance() {
        if(instance == null) {
            synchronized (EaseChatMenuExtendRegisterManager.class) {
                if(instance == null) {
                    instance = new EaseChatMenuExtendRegisterManager();
                }
            }
        }
        return instance;
    }

    public EaseChatMenuExtendRegisterManager registerChatMenuItem(EaseChatMenuDelegate delegate) {
        delegates.add(delegate);
        return this;
    }

    public void register(EaseChatExtendMenuAdapter adapter) {
        if(delegates == null || delegates.isEmpty()) {
            //采用默认的
            adapter.addDelegate(new EaseImageExtendMenuDelegate())
                    .addDelegate(new EaseCameraExtendMenuDelegate())
                    .addDelegate(new EaseVideoExtendMenuDelegate())
                    .addDelegate(new EaseLocationExtendMenuDelegate())
                    .addDelegate(new EaseFileExtendMenuDelegate());
        }else {
            for(EaseChatMenuDelegate delegate : delegates) {
                adapter.addDelegate(delegate);
            }
        }
    }
}

