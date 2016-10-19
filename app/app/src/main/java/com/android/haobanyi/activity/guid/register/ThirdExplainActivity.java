package com.android.haobanyi.activity.guid.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;

import butterknife.BindView;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by fWX228941 on 2016/10/10.
 *
 * @作者: 付敏
 * @创建日期：2016/10/10
 * @邮箱：466566941@qq.com
 * @当前文件描述：第三方协议
 */

public class ThirdExplainActivity extends BaseActivity {
    @BindView(R.id.webview)
    AdvancedWebView mWebView;


    @Override
    protected int setLayout() {
        return R.layout.fragment_shopping_details;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

        try {
             mWebView.setInitialScale(0);//如何做到等比例缩放，自适应屏幕http://blog.csdn.net/gan303/article/details/50245121
             mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
             mWebView.getSettings().setLoadWithOverviewMode(true);//http://blog.sina.com.cn/s/blog_3e333c4a0101gtjx.html
             mWebView.loadUrl("http://www.haobanyi.com/Register/RegProtocol ");
        }catch (Exception e){

        }

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void registerEventListener() {

    }

    @Override
    protected void registerBroadCastReceiver() {

    }

    @Override
    protected void saveState(Bundle outState) {

    }
}
