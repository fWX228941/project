package com.android.haobanyi.activity.mine.receipt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/8/5.
 *
 * @作者: 付敏
 * @创建日期：2016/08/05
 * @邮箱：466566941@qq.com
 * @当前文件描述： 普通发票
 */
public class ReceiptGIFragment extends BaseFragment {
    @BindView(R.id.rb_enterprise)
    RadioButton rbEnterprise;
    @BindView(R.id.rb_personal)
    RadioButton rbPersonal;
    @BindView(R.id.radio_group)
    RadioGroup group_tab;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    private  String title;
    boolean isEnterpriseReceipt = false;
    private static final int LOGIN_TO_RECEIPT_GI = 3;
    @Override
    protected void lazyload() {

    }

    private void handleLogic() {
        //变更状态
        checkUserStateLisener();
    }

    private void getToken() {
        this.appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {

            @Override
            public void onSuccess(LoginResponseResult data) {
                handleLogic();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getToken();
                }  else {
                    handleLogic();
                }
            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(getActivity(), LoginActivity.class, LOGIN_TO_RECEIPT_GI);//跳转到登录界面
    }
    @Override
    protected int setLayout() {
        return R.layout.fragment_receipt_gi;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000L);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                Log.d(TAG, "执行");
                getToken();
            } else {
                handleLogic();
            }

        } else {
            handleLogic();
        }






    }

    private void checkUserStateLisener() {
        group_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //个人
                    case R.id.rb_personal:
                        editName.setHint("请输入个人名字");
                        isEnterpriseReceipt = false;
                        break;
                    //单位
                    case R.id.rb_enterprise:
                        Log.d(TAG, "2");
                        editName.setHint("请输入企业名字");
                        isEnterpriseReceipt = true ;
                        break;
                }

            }
        });

    }

    @OnClick(R.id.btn_finish)
    public void onClick() {
        title = editName.getText().toString();
        /*
        * 这里分三种情况进行考虑：
        * 01.个人有发票
        * 02.企业有发票
        * 03.没有发票
        * */
        if (!TextUtils.isEmpty(title) && isEnterpriseReceipt ){
            requestCount = 45;
            saveGeneralInvoice();

        } else if (!TextUtils.isEmpty(title) && !isEnterpriseReceipt ){
            backToReceipt();//抬头为个人的不添加至列表中,是说不调用咯
        } else {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
        }
    }

    private void saveGeneralInvoice() {
        this.appAction.saveGeneralInvoice(title, true, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                /**
                 * http://blog.csdn.net/yaya_soft/article/details/42711027
                 *
                 */
                ToastUtil.showSuccessfulMessage(data.getMessage());
                backToReceipt();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    saveGeneralInvoice();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage( errorCode , errorMessage);
                }

            }
        });
    }

    private void backToReceipt() {
        /*一个是界面相关的toast，一个是界面无关的toast*/
        ToastUtil.showSuccessfulMessage(getActivity(),"成功保存");
        Intent intent = new Intent();
        intent.putExtra(Constants.RECEIPT_NAME, title);
        intent.putExtra(Constants.INVOICE_TYPE, 1);
        getActivity().setResult(getActivity().RESULT_OK, intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_TO_RECEIPT_GI:
                handleLogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;

        }
    }
}
