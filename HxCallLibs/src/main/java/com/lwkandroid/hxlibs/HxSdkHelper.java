package com.lwkandroid.hxlibs;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.lwkandroid.hxlibs.utils.HxCallBack;
import com.lwkandroid.hxlibs.utils.HxError;
import com.lwkandroid.hxlibs.utils.IHxUserData;

import java.util.Iterator;
import java.util.List;

/**
 * Created by LWK
 * 环信sdk帮助类
 * 2016/8/4
 */
public class HxSdkHelper {
    private static final String TAG = "HxSdkHelper";

    private HxSdkHelper() {
    }

    private static final class HxSdkHelperHolder {
        public static HxSdkHelper instance = new HxSdkHelper();
    }

    public static HxSdkHelper getInstance() {
        return HxSdkHelperHolder.instance;
    }

    private Context mAppContext;
    private boolean mIsSdkInited;
    private IHxUserData mIHxUserDataImpl;


    /**
     * 初始化环信sdk
     * 放在Application的onCreate()
     */
    public void initSdkOptions(Context context, String hxAppKey
            , String miPushAppId, String MiPushAppKey, String HWPushAppId) {
        if (mIsSdkInited)
            return;

        mAppContext = context.getApplicationContext();

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(mAppContext.getPackageName()))
            return;

        EMOptions options = new EMOptions();
        //设置AppKey
        options.setAppKey(hxAppKey);
        //添加好友需要验证
        options.setAcceptInvitationAlways(false);
        //不需要阅读回执
        options.setRequireAck(false);
        //不需要发送服务器回执
        options.setRequireDeliveryAck(false);
        //可以自动登录
        options.setAutoLogin(true);
        //设置小米推送
        options.setMipushConfig(miPushAppId, MiPushAppKey);
        //设置华为推送
        options.setHuaweiPushAppId(HWPushAppId);

        EMClient.getInstance().init(mAppContext, options);
        //设置拨打实时通话时，对方不在线是否推送提醒【华为、小米在后台时能接收提醒】
        //TODO 目前版本sdk无法区分推送的消息和普通文本消息的区别，且没有扩展字段标识到底是语音通话还是视频通话，暂时不启用这个功能！
        EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(false);
        EMClient.getInstance().setDebugMode(false);
        mIsSdkInited = true;
        Log.i(TAG, "HuanXin Sdk has inited");
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = mAppContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    /**
     * 手动登录环信的方法
     *
     * @param phone    手机号
     * @param pwd      密码
     * @param callBack 回调
     */
    public void login(final String phone, final String pwd, final HxCallBack callBack) {
        EMClient.getInstance().login(phone, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "HxSdk login from server success");
                if (callBack != null)
                    callBack.onSuccess(null);
            }

            @Override
            public void onError(int code, String msg) {
                Log.e(TAG, "HxSdk login fail : hxErrCode = " + code + " , msg = " + msg);
                if (callBack != null)
                    callBack.onFail(HxError.LOGIN_FAIL, HxError.getErrorMsgIdFromCode(code));
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    /**
     * 判断是否能自动登录
     */
    public boolean canAutoLogin() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * 退出环信登录
     *
     * @param callBack 回调
     */
    public void logout(final HxCallBack callBack) {
        if (!EMClient.getInstance().isLoggedInBefore()) {
            if (callBack != null)
                callBack.onSuccess(null);
            return;
        }
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                if (callBack != null)
                    callBack.onSuccess(null);
            }

            @Override
            public void onError(int i, String s) {
                if (callBack != null)
                    callBack.onFail(HxError.LOGOUT_FAIL, HxError.getErrorMsgIdFromCode(i));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    /**
     * 获取当前登录的账号
     *
     * @return 当前登录账号
     */
    public String getCurLoginUser() {
        return EMClient.getInstance().getCurrentUser();
    }

    /**
     * 设置用户资料查询实现类
     *
     * @param userDataImpl
     */
    public void setUserDataImpl(IHxUserData userDataImpl) {
        this.mIHxUserDataImpl = userDataImpl;
    }

    /**
     * 获取资料查询实现类
     */
    public IHxUserData getUserDataImpl() {
        return mIHxUserDataImpl;
    }
}
