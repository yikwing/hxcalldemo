package com.lwkandroid.hxlibs.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by LWK
 * TODO 通用回调
 * 2016/8/4
 */
public abstract class HxCallBack<T>
{
    private Type mType;

    public HxCallBack()
    {
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof ParameterizedType)
            return ((ParameterizedType) superclass).getActualTypeArguments()[0];
        else
            return String.class;
    }

    //失败回调
    public abstract void onFail(int status, int errorMsgResId);

    //成功回调
    public abstract void onSuccess(T t);
}
