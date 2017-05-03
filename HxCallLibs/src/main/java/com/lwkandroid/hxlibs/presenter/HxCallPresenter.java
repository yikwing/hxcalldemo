package com.lwkandroid.hxlibs.presenter;

import android.os.Handler;
import android.util.Log;

import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.lwkandroid.hxlibs.HxCallHelper;
import com.lwkandroid.hxlibs.R;
import com.lwkandroid.hxlibs.utils.StringUtil;
import com.lwkandroid.hxlibs.view.HxCallView;

/**
 * Created by LWK
 * TODO 实时通话Presenter基类
 * 2016/10/25
 */
public abstract class HxCallPresenter
{
    protected HxCallView mViewImpl;

    protected Handler mMainHandler;

    public HxCallPresenter(HxCallView viewImpl, Handler handler)
    {
        this.mViewImpl = viewImpl;
        this.mMainHandler = handler;
    }

    public void setOpData(String phone, String head, String name)
    {
        setOpHead(head);
        setOpName(phone, name);
    }

    //设置对方名字
    private void setOpName(final String phone, final String name)
    {
        if (StringUtil.isNotEmpty(name))
        {
            mMainHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mViewImpl.setName(name);
                }
            });
        } else
        {
            mMainHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mViewImpl.setName(phone);
                }
            });
        }
    }

    //设置对方头像
    private void setOpHead(final String url)
    {
        if (StringUtil.isNotEmpty(url))
        {
            mMainHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    mViewImpl.setHead(url);
                }
            });
        }
    }

    /**
     * 拨打实时语音通话
     */
    public void startVoiceCall(String phone)
    {
        try
        {
            HxCallHelper.getInstance().startVoiceCall(phone);
        } catch (EMServiceNotReadyException e)
        {
            Log.e("callPresenter", "HxCallPresenter can not startVoiceCall:" + e.toString());
            mViewImpl.showError(R.string.call_state_unknow_error);
        }
    }

    /**
     * 拨打实时视频通话
     */
    public void startVideoCall(String phone)
    {
        try
        {
            HxCallHelper.getInstance().startVideoCall(phone);
        } catch (EMServiceNotReadyException e)
        {
            Log.e("callPresenter", "HxCallPresenter can not startVideoCall:" + e.toString());
        }
    }

    /**
     * 接听通话
     */
    public void answerCall()
    {
        try
        {
            HxCallHelper.getInstance().answerCall();
        } catch (EMNoActiveCallException e)
        {
            Log.e("callPresenter", "HxCallPresenter can not answerCall:" + e.toString());
            mViewImpl.showError(R.string.call_state_cannot_answer);
        }
    }

    /**
     * 结束通话
     */
    public void endCall()
    {
        try
        {
            HxCallHelper.getInstance().endCall();
        } catch (EMNoActiveCallException e)
        {
            Log.e("callPresenter", "HxCallPresenter can not endCall:" + e.toString());
            mViewImpl.showError(0);
        }
    }

    /**
     * 拒接通话
     */
    public void rejectCall()
    {
        try
        {
            HxCallHelper.getInstance().rejectCall();
        } catch (EMNoActiveCallException e)
        {
            Log.e("callPresenter", "HxCallPresenter can not rejectCall:" + e.toString());
            mViewImpl.showError(0);
        }
    }
}
