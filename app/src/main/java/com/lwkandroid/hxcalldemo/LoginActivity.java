package com.lwkandroid.hxcalldemo;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lwkandroid.hxlibs.HxSdkHelper;
import com.lwkandroid.hxlibs.base.BaseActivity;
import com.lwkandroid.hxlibs.utils.HxCallBack;
import com.lwkandroid.hxlibs.utils.StringUtil;

public class LoginActivity extends BaseActivity {
    private EditText mEdPhone;
    private EditText mEdPwd;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initUI() {
        mEdPhone = findView(R.id.ed_phone);
        mEdPwd = findView(R.id.ed_pwd);
        addClick(R.id.btn_login);
    }

    @Override
    protected void onClick(int id, View v) {
        switch (id) {
            case R.id.btn_login:
                String phone = mEdPhone.getText().toString().trim();
                String pwd = mEdPwd.getText().toString().trim();

                if (StringUtil.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(pwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                //登录环信的方法
                //登出的方法是HxSdkHelper.getInstance().logout(new HxCallback(){});
                HxSdkHelper.getInstance().login(phone, pwd, new HxCallBack() {
                    @Override
                    public void onFail(int status, int errorMsgResId) {

                    }

                    @Override
                    public void onSuccess(Object o) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                });

                break;
        }
    }
}
