package com.lwkandroid.hxlibs.utils;

/**
 * Created by LWK
 * TODO 环信用户资料查询接口
 * 2017/5/2
 */

public interface IHxUserData
{
    /**
     * 实现此方法实现根据手机号查询某人头像
     *
     * @param phone 手机号
     * @return 头像
     */
    String queryHead(String phone);

    /**
     * 实现此方法实现根据手机号查询某人姓名
     *
     * @param name 手机号
     * @return 姓名
     */
    String queryName(String name);
}
