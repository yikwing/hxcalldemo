package com.lwkandroid.hxlibs.presenter;

import android.os.Handler;
import android.util.Log;

import com.hyphenate.chat.EMCallManager;
import com.hyphenate.media.EMLocalSurfaceView;
import com.hyphenate.media.EMOppositeSurfaceView;
import com.lwkandroid.hxlibs.HxCallHelper;
import com.lwkandroid.hxlibs.view.HxCallView;

/**
 * Created by LWK
 * TODO 实时视频通话Presenter
 * 2016/10/27
 */
public class HxVideoCallPrenter extends HxCallPresenter
{
    private EMCallManager.EMVideoCallHelper mVideoCallHelper;

    public HxVideoCallPrenter(HxCallView viewImpl, Handler handler)
    {
        super(viewImpl, handler);
        mVideoCallHelper = HxCallHelper.getInstance().getVideoCallHelper();
        HxCallHelper.getInstance().enableFixedVideoResolution(true);
        HxCallHelper.getInstance().setVideoCallResolution(540, 960);
    }

    /**
     * 设置双方SurfaceView
     */
    public void setSurfaceView(EMLocalSurfaceView localSurfaceView, EMOppositeSurfaceView opSurfaceView)
    {
        HxCallHelper.getInstance().setSurfaceView(localSurfaceView, opSurfaceView);
    }

    /**
     * 停止录像
     */
    public void stopVideoRecord()
    {
        if (mVideoCallHelper != null)
        {
            try
            {
                mVideoCallHelper.stopVideoRecord();
            } catch (Exception e)
            {
                Log.e("HxVideoCallPresenter", "Can not stop video record:" + e);
            }
        }
    }
}
