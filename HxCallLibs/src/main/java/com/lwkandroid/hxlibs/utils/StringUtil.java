package com.lwkandroid.hxlibs.utils;

/**
 * 字符串工具类
 */
public class StringUtil
{
    /**
     * 判断字符串是否为空?
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
        return (str == null || str.length() == 0);
    }

    /**
     * 判断字符串是否为空?
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str)
    {
        return (str != null && str.length() != 0);
    }

    /**
     * 判断文本是否相同
     *
     * @param actual
     * @param expected
     * @return
     */
    public static boolean isEquals(String actual, String expected)
    {
        return ObjectUtils.isEquals(actual, expected);
    }
}
