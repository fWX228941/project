package com.android.haobanyi.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.android.haobanyi.BaseApplication;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

/**
 * Created by fWX228941 on 2016/3/29.
 *
 * @作者: 付敏
 * @创建日期：2016/03/29
 * @邮箱：466566941@qq.com
 * @当前文件描述：封装弹框提示类，如果需要增加样式，设置显示位置，添加线程则参考
 *
 *
 */
public class ToastUtil {
    //保留项， 客户端传递context参数
    /*分别根据资源ID 和具体的message 为参数来弹框*/
    public static void showShort(int resId) {
        Toast.makeText(BaseApplication.getApplication(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        Toast.makeText(BaseApplication.getApplication(), message, Toast.LENGTH_SHORT).show();
    }

    private static void showLong(int resId) {
        Toast.makeText(BaseApplication.getApplication(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        Toast.makeText(BaseApplication.getApplication(), message, Toast.LENGTH_LONG).show();
    }
    /*如果想要更短或者更长的提示时间，新建showShorter() 调用两次方法
    * eg。
    *    public static void showLongLong(String message) {
        showLong(message);
        showLong(message);
       这个类来设计
    }
    *
    * */
    /*
    * 定制化弹框类，比如位置，时间，样式等，参考：https://github.com/dersoncheng/Pumelo/blob/master/src/com/derson/pumelo/util/ToastUtil.java
    *
    *
    * */

    /*
    *
    * 界面相关的：网络连接不可用
    * 这种就是网络连接失败
    *
    * */
    public static void networkUnavailable() {
        new SuperToast(BaseApplication.getApplication())
                .setText("网络连接不可用")
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)//横条
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                .setGravity(Gravity.TOP)//上面
                .setAnimations(Style.ANIMATIONS_SCALE).show();
    }
    /*
    *
    *   java.lang.IllegalArgumentException: SuperActivityToast Context must be an Activity. 界面相关的周期短，而且是存在风险的
    * */
/*    public static void networkUnavailable(Context context) {


        new SuperToast(context)
                .setText("网络连接不可用")
//                .setTypefaceStyle(Style.TYPE_STANDARD)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)//横条
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                .setGravity(Gravity.TOP)//上面
                .setAnimations(Style.ANIMATIONS_SCALE).show();
    }*/
    /*界面无关的*/
    public static void showSuccessfulMessage(Context context,String message) {
        new SuperToast(context)
                .setText(message)
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_KITKAT)//半圆
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIGHT_BLUE))
                .setAnimations(Style.ANIMATIONS_POP).show();

    }
    public static void showSuccessfulMessage(String message) {
        new SuperToast(BaseApplication.getApplication())
                .setText(message)
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_KITKAT)//半圆
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_LIGHT_BLUE))
                .setAnimations(Style.ANIMATIONS_POP).show();

    }
    //提示说明：黑底白字
    public static void showHintMessage(String message) {
        new SuperToast(BaseApplication.getApplication())
                .setText(message)
                .setDuration(Style.DURATION_LONG)
                .setFrame(Style.FRAME_STANDARD)//半圆
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.BLACK))
                .setAnimations(Style.ANIMATIONS_SCALE).show();

    }
    /*使用这个呢！ToastUtil.showHintMessage("交易失败",errorCode,errorMessage);*/
    public static void showHintMessage(String hintmessage,String errorCode, String errorMessage) {
        new SuperToast(BaseApplication.getApplication())
                .setText(hintmessage+"原因："+errorMessage+","+errorCode+"!")
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_STANDARD)//半圆
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.BLACK))
                .setAnimations(Style.ANIMATIONS_SCALE).show();

    }


    public static void showErrorMessage(Context context,String message) {
        new SuperToast(context)
                .setText(message)
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_KITKAT)//半圆
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                .setAnimations(Style.ANIMATIONS_FLY).show();
    }
    /*这种就是加载失败
    * ToastUtil.showErrorMessage(" 加载失败: " + errorMessage + "," + errorCode);
    *
    * */
    public static void showErrorMessage(String message) {
        new SuperToast(BaseApplication.getApplication())
                .setText(message)
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_KITKAT)//半圆
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                .setAnimations(Style.ANIMATIONS_FLY).show();
    }

    public static void showErrorMessage(String errorCode, String errorMessage) {
        new SuperToast(BaseApplication.getApplication())
                .setText("请重新操作，"+"异常说明："+errorMessage+","+errorCode+"!")
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_KITKAT)//半圆
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                .setAnimations(Style.ANIMATIONS_FLY).show();
    }


}
