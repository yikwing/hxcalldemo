package com.lwkandroid.hxlibs.view;

import com.hyphenate.chat.EMCallStateChangeListener;

/**
 * Created by LWK
 * TODO 环信实时通话接口基类
 * 2016/10/21
 */
public interface HxCallView
{
    void setHead(String url);

    void setName(String name);

    void connecting();

    void connected();

    void answering();

    void accepted();

    void beRejected();

    void noResponse();

    void busy();

    void offline();

    void onDisconnect(EMCallStateChangeListener.CallError callError);

    void onNetworkUnstable(EMCallStateChangeListener.CallError callError);

    void onNetworkResumed();

    void showError(int errResId);
}
