package com.android.haobanyi.activity.guid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/7/5.
 *
 * @作者: 付敏
 * @创建日期：2016/07/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class QuickResponseLoginActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.textView11)
    TextView textView11;
    String  loginToken;

    @Override
    protected int setLayout() {
        return R.layout.activity_quick_response_login;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        loginToken = this.getIntent().getStringExtra("result");

    }

    @Override
    protected void initViews() {
        initTittleBar();

    }

    private void initTittleBar() {
        titleBar.setBackgroundResource(R.color.white);
        // 设置返回箭头和文字以及颜色
        titleBar.setLeftImageResource(R.drawable.back_icon);
        // 设置监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                QuickResponseLoginActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });

        titleBar.setTitle("扫码登录");
        titleBar.addAction(new TitleBar.ImageAction(R.drawable.searching_more) {
            @Override
            public void performAction(View view) {
                ToastUtil.showShort("点击操作");
            }
        });

        titleBar.setDividerColor(Color.GRAY);
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



    @OnClick({R.id.btn_login, R.id.textView11})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                ToastUtil.showShort("确认登录");

                break;
            case R.id.textView11:
                ToastUtil.showShort("取消");
                finish();
                QuickResponseLoginActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
                break;
        }
    }

}
