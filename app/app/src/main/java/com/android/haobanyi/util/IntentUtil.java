package com.android.haobanyi.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.android.haobanyi.R;


/**
 * Created by fWX228941 on 2016/4/8.
 *
 * @作者: 付敏
 * @创建日期：2016/04/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：Activity界面之间跳转的工具类,到时全部替换
 */
public class IntentUtil {
    /*
    * 放在一个任务栈中
    * 跳转到指定activity，并且是否要保留原activity
    * @param gotoClass  即将跳转的activity
    * @param finishThis 关闭当前activity
    * */
    public static void gotoActivityWithoutData(Context context, Class<?> gotoClass, boolean finishThis) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        context.startActivity(intent);
        if (finishThis) ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }
    /*
    *
    * 携带数据跳转到指定activity，并且是否要保留原activity
    * @param gotoClass  即将跳转的activity
    * @param bundle 携带数据
    * @param finishThis 关闭当前activity
    * */
    public static void gotoActivityWithData(Context context, Class<?> gotoClass, Bundle bundle, boolean finishThis) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (finishThis) ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }
    /*带FLAG_ACTIVITY_NEW_TASK启动位的*/
    /*
    *
    * 使用场景案例分析：
    * 1.当我从首页-》选择城市-》首页：点击后腿，还是回到首页（标准） （不返回首页，直接退出FLAG_ACTIVITY_CLEAR_TOP）【所以入口必须是首页，最后回到首页这种情况就销毁其他界面】
    * 如果设置这个属性，是当要启动的Activity已经存在当前Task中，才会在启动的时候销毁其他的Activity。
    *If set, and the activity being launched is already running in the current task,
    * then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this Intent will be delivered to the (now on top) old activity as a new Intent.
    * 也就是提升优先级，把位于栈底的界面提升到栈顶，并把启动界面选择城市与 首页之间的界面全部清除掉【整个也就删除了】 【如果这样的话，finish 启动activity实际上就没有很大的意义了，
    * 反正也是会销毁的】
    * 【这是有的情况，那如果没有呢！】
    *  但是状态保存，但是如果是到非首页，其实是可以全部销毁，在新建
    *
    *
    * */

    // 被启动界面置顶
    public static void gotoTopActivityWithoutData(Context context, Class<?> gotoClass,boolean finishThis) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //这个就是一个一个启动模式，会出现这种情况，把启动的activity也一起删除了
        context.startActivity(intent);
        if (finishThis) ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }

    public static void gotoTopActivityWithData(Context context, Class<?> gotoClass, Bundle bundle,boolean finishThis) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (finishThis) ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }
    /*
    *
    * 带有放回结果的跳转，多了请求码
    *
    * */
    public static void gotoActivityForResult(Context context, Class<?> gotoClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }


//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void gotoActivityForResultWithData(Context context, Class<?> gotoClass, int requestCode,Bundle data) {
        /*
        *
        *         Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (finishThis) ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
        分析问题，解决问题，数据压根就是没有传递的
        *
        * */
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(data);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_exit, R.anim.enter_enter);
    }
    /*
    *
    * 带action事件的显式跳转到service
    * */
    public static void gotoServiceWtthAction(String action,Context context, Class<?> gotoService){
        Intent serviceIntent = new Intent(action,null,context,gotoService);
        context.startService(serviceIntent);
    }
    /*
    * 隐式跳转掉ServiceF服务
    *
    * */
//    public static void gotoServiceWtthAction(String action){
//        Intent intent = new Intent(action);
//
//    }
    /**
     * 判断intent和它的bundle是否为空
     *
     * @param intent
     * @return
     */
    public static boolean isBundleEmpty(Intent intent) {
        return (intent == null) && (intent.getExtras() == null);
    }
}
