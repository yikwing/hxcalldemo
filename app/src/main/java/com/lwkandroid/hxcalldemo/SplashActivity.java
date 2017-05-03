package com.lwkandroid.hxcalldemo;

import android.content.Intent;
import android.view.View;

import com.lwkandroid.hxlibs.HxSdkHelper;
import com.lwkandroid.hxlibs.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected int setContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initUI() {
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (HxSdkHelper.getInstance().canAutoLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
        }, 2000);
    }

    @Override
    protected void onClick(int id, View v) {

    }

}
