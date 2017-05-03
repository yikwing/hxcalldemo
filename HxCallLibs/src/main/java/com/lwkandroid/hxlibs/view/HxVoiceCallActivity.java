package com.lwkandroid.hxlibs.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import com.lwkandroid.hxlibs.R;
import com.lwkandroid.hxlibs.presenter.HxVoiceCallPresenter;
import com.lwkandroid.hxlibs.utils.CommonUtils;
import com.lwkandroid.hxlibs.utils.HxMsgAttrConstant;
import com.lwkandroid.hxlibs.utils.StringUtil;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 环信实时语音通话界面
 */
@RuntimePermissions
public class HxVoiceCallActivity extends HxCallBaseActivity implements HxVoiceCallView
        , SensorEventListener
{
    private HxVoiceCallPresenter mPresenter;
    //距离传感器控制对象
    private SensorManager mSensorManager;
    private Sensor mSensor;
    //亮度控制
    private PowerManager.WakeLock mWakeLock;
    //背景模糊图
    private ImageView mImgBlurBg;

    /**
     * 跳转到该界面的公共方法
     *
     * @param context      启动context
     * @param phone        手机号
     * @param head         对方头像地址
     * @param name         对方名字
     * @param isComingCall 是否为接收方
     */
    public static void start(Context context, String phone, String head, String name, boolean isComingCall)
    {
        Intent intent = new Intent(context, HxVoiceCallActivity.class);
        intent.putExtra(INTENT_KEY_PHONE, phone);
        intent.putExtra(INTENT_KEY_IS_COMING_CALL, isComingCall);
        intent.putExtra(INTENT_KEY_CALL_TYPE, HxMsgAttrConstant.VOICE_CALL_RECORD);
        intent.putExtra(INTENT_KEY_HAED, head);
        intent.putExtra(INTENT_KEY_NAME, name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int setContentViewId()
    {
        mPresenter = new HxVoiceCallPresenter(this, mMainHandler);
        return R.layout.activity_hx_voice_call;
    }

    @Override
    protected void initUI()
    {
        super.initUI();
        mImgBlurBg = findView(R.id.img_voicecall_blur);
    }

    @Override
    public void setOpUserData()
    {
        mPresenter.setOpData(mOpPhone, mOpHead, mOpName);
    }

    @Override
    public void setHead(String url)
    {
        super.setHead(url);
        if (StringUtil.isNotEmpty(url) && mImgBlurBg != null)
            CommonUtils.getInstance().getImageDisplayer().displayBlurImage(this, mImgBlurBg, url, 25, 6);
    }

    @Override
    public void doOutgoingCall()
    {
        HxVoiceCallActivityPermissionsDispatcher.startVoiceCallWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void startVoiceCall()
    {
        mPresenter.startVoiceCall(mOpPhone);
    }

    @Override
    public void doAfterAccepted()
    {
        //监听距离传感器
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "VoiceCallScreenOff");
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager != null)
        {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void doAnswercall()
    {
        HxVoiceCallActivityPermissionsDispatcher.answerCallWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void answerCall()
    {
        mPresenter.answerCall();
    }

    @Override
    public void doRejectCall()
    {
        mPresenter.rejectCall();
    }

    @Override
    public void doEndCall()
    {
        mPresenter.endCall();
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float value = event.values[0];
        if (value == mSensor.getMaximumRange())
            setScreenOn();
        else
            setScreenOff();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //没用
    }

    //点亮屏幕
    private synchronized void setScreenOn()
    {
        if (mWakeLock != null)
        {
            if (mWakeLock.isHeld())
                return;
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
        }
    }

    //熄灭屏幕
    private synchronized void setScreenOff()
    {
        if (mWakeLock != null)
        {
            if (mWakeLock.isHeld())
                return;
            mWakeLock.acquire();
        }
    }

    //释放屏幕锁
    private void releaseWakeLock()
    {
        if (mWakeLock != null)
        {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    public void showRationaleForRecordAudio(final PermissionRequest request)
    {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle(R.string.dialog_permission_title)
                .setMessage(R.string.dialog_permission_voice_call_message)
                .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        request.proceed();
                    }
                })
                .create().show();
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    public void onRecordAudioPermissionDenied()
    {
        Toast.makeText(this, R.string.warning_permission_voice_call_denied, Toast.LENGTH_LONG).show();
        if (mIsComingCall)
            mPresenter.rejectCall();
        else
            mPresenter.endCall();
        finish();
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    public void onNeverAskRecordAudio()
    {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle(R.string.dialog_permission_title)
                .setMessage(R.string.dialog_permission_voice_call_nerver_ask_message)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        if (mIsComingCall)
                            mPresenter.rejectCall();
                        else
                            mPresenter.endCall();
                        finish();
                    }
                })
                .setPositiveButton(R.string.dialog_permission_nerver_ask_confirm, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                        startActivity(intent);
                        if (mIsComingCall)
                            mPresenter.rejectCall();
                        else
                            mPresenter.endCall();
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HxVoiceCallActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //释放距离传感器
        if (mSensorManager != null)
        {
            if (mSensor != null)
            {
                mSensorManager.unregisterListener(this, mSensor);
                mSensor = null;
            }
            mSensorManager = null;
        }
        //挂机后亮屏再释放锁
        setScreenOn();
        releaseWakeLock();
    }
}
