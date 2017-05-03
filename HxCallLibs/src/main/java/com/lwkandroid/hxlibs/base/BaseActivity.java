package com.lwkandroid.hxlibs.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lwkandroid.hxlibs.utils.ViewFinder;

/**
 * Function:通用基类Activity
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected View mRootLayout;
    protected Handler mMainHandler;
    protected ViewFinder mVFinder;

    protected void beforeOnCreate(Bundle savedInstanceState) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        mMainHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                onHandlerMessage(msg);
            }
        };
        if (mRootLayout == null)
            mRootLayout = getLayoutInflater().inflate(setContentViewId(), null);
        setContentView(mRootLayout);
        mVFinder = new ViewFinder(mRootLayout);//查找控件的帮助类
        beforeInitUI(savedInstanceState);
        initUI();
        initData();
    }

    protected abstract int setContentViewId();

    protected void beforeInitUI(Bundle savedInstanceState) {
    }

    protected abstract void initUI();

    protected void initData() {
    }

    protected void onHandlerMessage(Message msg) {
    }

    /**
     * 查找View
     */
    protected <T extends View> T findView(int resId) {
        return mVFinder.findView(resId);
    }

    /**
     * 添加点击监听到onClick()中
     */
    protected void addClick(View view) {
        if (view != null)
            view.setOnClickListener(this);
    }

    /**
     * 添加点击监听到onClick()中
     */
    protected void addClick(int id) {
        View view = mVFinder.findView(id);
        addClick(view);
    }

    @Override
    public void onClick(View v) {
        onClick(v.getId(), v);
    }

    protected abstract void onClick(int id, View v);
}
