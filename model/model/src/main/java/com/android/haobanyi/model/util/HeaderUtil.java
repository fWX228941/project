package com.android.haobanyi.model.util;

import android.util.Log;

/**
 * Created by fWX228941 on 2016/5/24.
 *
 * @作者: 付敏
 * @创建日期：2016/05/24
 * @邮箱：466566941@qq.com
 * @当前文件描述：消息头工具类
 * 存在优化部分：提供一个单例模式，不然每次创建四个对象，浪费内存，看来优化的地方还不少啊！
 * 在线MD5刷新：http://pmd5.com/
 *
 *
 *
 */
public class HeaderUtil {

    public static String TimeStamp ;
    /*变化的就作为参数*/
    public static String getHeadindexNotUser1(String methodName){
        Log.d("HeaderUtil", "1");
        Log.d("CategoryListFragment", "methodName+\"hby.mobile.client\": "+methodName+"hby.mobile.client");
        Log.d("CategoryListFragment", "EncryptUtil.makeMD5(methodName+\"hby.mobile.client\")"+EncryptUtil.makeMD5(methodName+"hby.mobile.client"));
        return EncryptUtil.makeMD5(methodName+"hby.mobile.client");
    }


    public static String getHeadindexUser1(String methodName){
        Log.d("HeaderUtil", "2");
        Log.d("CategoryListFragment", "methodName+\"hby.mobile.client\": "+methodName+"hby.mobile.client");
        Log.d("CategoryListFragment", "EncryptUtil.makeMD5(methodName+\"hby.mobile.client\")"+EncryptUtil.makeMD5(methodName+"hby.mobile.client"));
        return EncryptUtil.makeMD5(methodName+"hby.mobile.client");
    }
    public static String getHeadindex2(long milliseconds){
        Log.d("HeaderUtil", "3");
        TimeStamp = Long.toString(milliseconds / 1000L); // 这个就是转化为秒
        Log.d("CategoryListFragment", "TimeStamp: "+TimeStamp);
        return TimeStamp;
    }
    /*一旦顺序出现问题，也是会报错的,保证时间戳的一致性*/
    public static String getHeadindex3(String methodName){
        Log.d("HeaderUtil", "4");
        Log.d("CategoryListFragment", "TimeStamp: "+TimeStamp);
        Log.d("CategoryListFragment", "TimeStamp+methodName+\"hby.mobile.client\": "+TimeStamp+methodName+"hby.mobile.client");
        Log.d("CategoryListFragment", "001: "+EncryptUtil.makeMD5(TimeStamp+methodName+"hby.mobile.client"));
        return EncryptUtil.makeMD5(TimeStamp+methodName+"hby.mobile.client");
    }
    public static String getHeadindex3b(String methodName){
        Log.d("HeaderUtil", "5");
        Log.d("CategoryListFragment", "TimeStamp: "+TimeStamp);
        Log.d("CategoryListFragment", "TimeStamp+methodName+\"hby.mobile.client\": "+TimeStamp+methodName+"hby.mobile.client");
        Log.d("CategoryListFragment", "001: "+EncryptUtil.makeMD5(TimeStamp+methodName+"hby.mobile.client"));
        return EncryptUtil.makeMD5(methodName+"hby.mobile.client");
    }
}
