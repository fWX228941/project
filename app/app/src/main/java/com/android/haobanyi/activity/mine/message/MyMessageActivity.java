package com.android.haobanyi.activity.mine.message;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/6/20.
 *
 * @作者: 付敏
 * @创建日期：2016/06/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：我的消息界面
 */
public class MyMessageActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.id_order_message)
    CardView idOrderMessage;
    @BindView(R.id.id_refund_message)
    CardView idRefundMessage;
    @BindView(R.id.id_complaint_message)
    CardView idComplaintMessage;
    @BindView(R.id.id_inform_offense_message)
    CardView idInformOffenseMessage;
    @BindView(R.id.id_merchants_setted_message)
    CardView idMerchantsSettedMessage;
    @BindView(R.id.id_fund_message)
    CardView idFundMessage;
    @BindView(R.id.id_security_message)
    CardView idSecurityMessage;


    @Override
    protected int setLayout() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {


    }

    @Override
    protected void initViews() {
        /*01.设置标题栏*/
        initTittleBar();


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

    /*设置标题栏*/
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.white);
        // 设置返回箭头和文字以及颜色
        titleBar.setLeftImageResource(R.drawable.back_icon);
        // 设置监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyMessageActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("我的消息");
        titleBar.setDividerColor(Color.GRAY);
    }



    @OnClick({R.id.id_order_message, R.id.id_refund_message, R.id.id_complaint_message, R.id
            .id_inform_offense_message, R.id.id_merchants_setted_message, R.id.id_fund_message, R.id
            .id_security_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_order_message:
                goToMessageListActivity("1");
                break;
            case R.id.id_refund_message:
                goToMessageListActivity("2");
                break;
            case R.id.id_complaint_message:
                goToMessageListActivity("3");
                break;
            case R.id.id_inform_offense_message:
                goToMessageListActivity("4");
                break;
            case R.id.id_merchants_setted_message:
                goToMessageListActivity("5");
                break;
            case R.id.id_fund_message:
                goToMessageListActivity("6");
                break;
            case R.id.id_security_message:
                goToMessageListActivity("7");
                break;
        }
    }
    //每次跳入一个界面1到7
    public void goToMessageListActivity(String messageType){
        Log.d("MyMessageActivity", messageType);
        Bundle bundle = new Bundle();
        bundle.putString("messageType",messageType);
        IntentUtil.gotoActivityWithData(MyMessageActivity.this,MessageListActivity.class,bundle,false);
    }




}
