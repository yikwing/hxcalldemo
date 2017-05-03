package com.lwkandroid.hxcalldemo;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lwkandroid.hxlibs.base.BaseActivity;
import com.lwkandroid.hxlibs.receiver.HxCallReceiver;
import com.lwkandroid.hxlibs.utils.StringUtil;
import com.lwkandroid.hxlibs.view.HxVideoCallActivity;
import com.lwkandroid.hxlibs.view.HxVoiceCallActivity;

public class MainActivity extends BaseActivity {
    private EditText mEditText;
    private HxCallReceiver mReceiver;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUI() {
        mEditText = findView(R.id.ed_main);
        addClick(R.id.btn_main_voice);
        addClick(R.id.btn_main_video);

        //添加通话广播监听
        //TODO 在你自己app里找合适的时机绑定/解绑该监听
        mReceiver = HxCallReceiver.regist(this);
    }

    @Override
    protected void onClick(int id, View v) {
        String phone = mEditText.getText().toString().trim();
        if (StringUtil.isEmpty(phone)) {
            Toast.makeText(this, "请输入对方手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (id) {
            case R.id.btn_main_voice:
                //TODO 发起实时语音通话，最好传入对方头像和名字(第三、第四个参数)
                HxVoiceCallActivity.start(this, phone, null, null, false);
                break;
            case R.id.btn_main_video:
                //TODO 发起实时视频通话，最好传入对方头像和名字(第三、第四个参数)
                HxVideoCallActivity.start(this, phone, null, null, false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        //解绑广播监听
        HxCallReceiver.unregist(this, mReceiver);
        super.onDestroy();
    }
}
