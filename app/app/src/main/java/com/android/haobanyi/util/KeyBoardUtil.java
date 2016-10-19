package com.android.haobanyi.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by fWX228941 on 2016/4/28.
 *
 * @作者: 付敏
 * @创建日期：2016/04/28
 * @邮箱：466566941@qq.com
 * @当前文件描述：操作输入法和软键盘的工具类。可以方便的关闭和显示输入法. 参考方案设计：http://www.cnblogs.com/tianzhijiexian/p/4460151.html
 * http://www.cnblogs.com/tianzhijiexian/p/4229410.html
 */
public class KeyBoardUtil {
    private static KeyBoardUtil instance;
    private InputMethodManager mInputMethodManager;
    private static Activity mActivity;

    private KeyBoardUtil() {
        mInputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static KeyBoardUtil getInstance(Activity activity) {
        mActivity = activity;
        if (instance == null) {
            instance = new KeyBoardUtil();
        }
        return instance;
    }

    /**
     * 强制显示输入法
     */
    public void show() {
        show(mActivity.getWindow().getCurrentFocus());
    }

    public void show(View view) {
        mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 强制关闭输入法
     */
    public void hide() {
        hide(mActivity.getWindow().getCurrentFocus());
    }

    public void hide(View view) {
        mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 如果输入法已经显示，那么就隐藏它；如果输入法现在没显示，那么就显示它
     */
    public void showOrHide() {
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
