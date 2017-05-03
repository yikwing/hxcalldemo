package com.lwkandroid.hxcalldemo;

import android.app.Application;

import com.lwkandroid.hxlibs.HxSdkHelper;

/**
 * Created by LWK
 * TODO Application入口
 * 2017/5/2
 */

public class DemoApplication extends Application {
    /**
     * TODO 填写环信后台的AppKey
     **/
    private static final String HX_APP_KEY = "1132170317178707#趣面视";
    /**
     * TODO 填写环信后台的小米推送AppId
     **/
    private static final String MI_PUSH_APP_ID = "2882303761517527011";
    /**
     * TODO 填写环信后台的小米推送AppKey
     **/
    private static final String MI_PUSH_APP_KEY = "5431752768011";
    /**
     * TODO 填写环信后台的华为推送AppId
     **/
    private static final String HW_PUSH_APP_ID = "10727579";

    @Override
    public void onCreate() {
        super.onCreate();

        //环信初始化Sdk
        HxSdkHelper.getInstance().initSdkOptions(this, HX_APP_KEY, MI_PUSH_APP_ID, MI_PUSH_APP_KEY, HW_PUSH_APP_ID);
        //TODO 如果需要在实时通话的界面中显示对方头像和名字，需要在你自己的工程中新建一个类来实现IHxUserData接口，然后设置在HxSdkHelper中
    }
}
