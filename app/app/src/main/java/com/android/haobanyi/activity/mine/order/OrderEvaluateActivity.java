package com.android.haobanyi.activity.mine.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/8/19.
 *
 * @作者: 付敏
 * @创建日期：2016/08/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：订单服务评价界面 activity_order_evaluate
 */
public class OrderEvaluateActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.ratingBar)
    SimpleRatingBar ratingBar;
    @BindView(R.id.edit_feedback)
    ClearEditText editFeedback;
    @BindView(R.id.btn_send_evaluation)
    Button btn_login;
    @BindView(R.id.id_hint)
    TextView idHint;
    private static final int LOGIN_TO_ORDER_EVALUATION = 3;
    private long OrderExtendID =-1;
    float serviceProduct =0;

    @Override
    protected int setLayout() {
        return R.layout.activity_order_evaluate;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (hasTokenRefreshed){
            handleLogic();
        } else {
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                //超过过期时间，更新token
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }
        }
    }

    private void getToken() {
        this.appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                handleLogic();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getToken();
                }else {
                    handleLogic();
                }

            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(OrderEvaluateActivity.this, LoginActivity.class, LOGIN_TO_ORDER_EVALUATION);//跳转到登录界面
    }

    private void handleLogic() {
        initTittleBar();
        //获取OrderExtendID的值
        OrderExtendID = this.getIntent().getLongExtra("OrderExtendID",-1);
        Log.d("OrderEvaluateActivity", "OrderExtendID:" + OrderExtendID);

    }

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
                    OrderEvaluateActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
                }
            });
            titleBar.setTitle("订单评价");
            titleBar.setDividerColor(Color.GRAY);
        }
    @Override
    protected void initViews() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void registerEventListener() {
        /*
        *     for (SimpleRatingBar srb : Arrays.asList(ratingBar1, ratingBar2, ratingBar3, ratingBar4, ratingBar5)) {
                    srb.setOnRatingBarChangeListener(this);}  一组
        *
        * */
        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                ToastUtil.showHintMessage(String.format("服务评分 : %.2f", rating));//最高分是5分 ,而且是带小数点的，以此为标准
                serviceProduct = rating;
            }
        });


    }

    @Override
    protected void registerBroadCastReceiver() {

    }

    @Override
    protected void saveState(Bundle outState) {

    }

    @OnClick(R.id.btn_send_evaluation)
    public void onClick() {
        /*这样就更加合理了*/
        HashMap<String, Object> params = new HashMap<String, Object>();
        btn_login.setEnabled(false);
        Log.d("OrderEvaluateActivity", "serviceProduct:" + serviceProduct);
        if (serviceProduct ==0){
            setHintMessage("请先评分");
            return; //在这里就拦截了

        }else {
            params.put("ServiceProduct", serviceProduct);
            idHint.setText(" ");
        }

        String Views =editFeedback.getText().toString();
        Log.d("OrderEvaluateActivity", Views);
        if (Views.isEmpty()){
            setHintMessage("评价不能为空");
            return; //在这里就拦截了

        }else {
            params.put("Views", Views);
            idHint.setText(" ");
        }
        params.put("OrderExtendID", OrderExtendID);
        if (OrderExtendID!=-1){
            requestCount = 45;
            addAssessment(params);
        }
    }

    private void addAssessment(final HashMap<String, Object> params) {
        this.appAction.addAssessment(params, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                setHintMessage(" ");
                ToastUtil.showHintMessage("成功添加评论");
                finish();
                OrderEvaluateActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btn_login.isEnabled()){
                    btn_login.setEnabled(true);
                }

// {"code":106,"message":"评价失败，可能已评价或暂不能评价！","data":null}  如果是这个问题，则表示状态不正确。需要看下返回值
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    addAssessment(params);
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    setHintMessage(errorMessage);
                    ToastUtil.networkUnavailable();
                } else {
                    setHintMessage(errorMessage);
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                }
            }
        });
    }

    private void setHintMessage(String errorMessage) {
        if (!btn_login.isEnabled()){
            btn_login.setEnabled(true);
        }

        idHint.setVisibility(View.VISIBLE);
        idHint.setText(errorMessage);

    }
    //LOGIN_TO_ORDER_EVALUATION


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_TO_ORDER_EVALUATION:
                handleLogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;

        }
    }
}
