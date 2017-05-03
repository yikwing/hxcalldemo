package com.lwkandroid.hxlibs.listener;

import android.os.Handler;
import android.util.Log;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.lwkandroid.hxlibs.view.HxCallView;

/**
 * Created by LWK
 * TODO 环信实时通话监听
 * 2016/10/21
 */
public class HxCallStateChangeListener implements EMCallStateChangeListener
{
    private static final String TAG = "HxCallState";
    private Handler mMainHandler;
    private HxCallView mViewImpl;

    public HxCallStateChangeListener(Handler handler, HxCallView viewImpl)
    {
        this.mMainHandler = handler;
        this.mViewImpl = viewImpl;
    }

    @Override
    public void onCallStateChanged(CallState callState, final CallError callError)
    {
        switch (callState)
        {
            case IDLE:
                Log.i(TAG, "onCallStateChanged: Idle");
                break;
            case RINGING:
                Log.i(TAG, "onCallStateChanged: Ringing");
                break;
            case CONNECTING: // 正在连接对方
                Log.i(TAG, "onCallStateChanged: Connecting");
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mViewImpl != null)
                            mViewImpl.connecting();
                    }
                });
                break;
            case CONNECTED: // 双方已经建立连接
                Log.i(TAG, "onCallStateChanged: Connected");
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mViewImpl != null)
                            mViewImpl.connected();
                    }
                });
                break;
            case ANSWERING:
                Log.i(TAG, "onCallStateChanged: Answering");
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mViewImpl != null)
                            mViewImpl.answering();
                    }
                });
                break;
            case ACCEPTED: // 电话接通成功
                Log.i(TAG, "onCallStateChanged :Accpet");
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mViewImpl != null)
                            mViewImpl.accepted();
                    }
                });
                break;
            case DISCONNECTED: // 电话断了
                Log.i(TAG, "onCallStateChanged: Disconnectd callError=" + callError);
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mViewImpl != null)
                        {
                            if (callError == CallError.REJECTED)
                                mViewImpl.beRejected();
                            else if (callError == CallError.ERROR_BUSY)
                                mViewImpl.busy();
                            else if (callError == CallError.ERROR_NORESPONSE)
                                mViewImpl.noResponse();
                            else if (callError == CallError.ERROR_UNAVAILABLE || callError == CallError.ERROR_TRANSPORT)
                                mViewImpl.offline();
                            else
                                mViewImpl.onDisconnect(callError);
                        }
                    }
                });
                break;
            case NETWORK_UNSTABLE: //网络不稳定
                Log.i(TAG, "onCallStateChanged: Netword Unstable callError=" + callError);
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mViewImpl != null)
                            mViewImpl.onNetworkUnstable(callError);
                    }
                });
                break;
            case NETWORK_NORMAL: //网络恢复正常
                Log.i(TAG, "onCallStateChanged: Network resume");
                mMainHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mViewImpl != null)
                            mViewImpl.onNetworkResumed();
                    }
                });
                break;
            case NETWORK_DISCONNECTED:
                Log.i(TAG, "onCallStateChanged: Network disconnected");
                break;
            case VOICE_PAUSE: //暂停语音传输【静音】
                Log.i(TAG, "onCallStateChanged: Voice pause");
                break;
            case VOICE_RESUME://恢复语音传输【取消静音】
                Log.i(TAG, "onCallStateChanged: Voice resume");
                break;
            case VIDEO_PAUSE:
                Log.i(TAG, "onCallStateChanged: Video pause");
                break;
            case VIDEO_RESUME:
                Log.i(TAG, "onCallStateChanged: Video resume");
                break;
            default:
                break;
        }
    }
}
