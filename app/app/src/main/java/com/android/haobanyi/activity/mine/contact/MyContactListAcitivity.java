package com.android.haobanyi.activity.mine.contact;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.adapter.contact.ContactListAdapter;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.contact.ContactBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：管理我的联系人信息 acitivity_my_contact_list
 * item_my_contact
 * 暂时不存在加载更多，全部加载
 */
public class MyContactListAcitivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    @BindView(R.id.btn_save)
    Button btnSave;
    private static final int LOGIN_TO_CONTACT_LIST = 3;
    private int totalCount = 0 ;
    ContactListAdapter adapter = new ContactListAdapter(null);//先初始化，再来赋值
    private static final int TO_EDIT =1;//
    private int position_ = -1;
    private boolean ishasData = false;//当前是否有数据
    private DialogPlus dialog ; //弹框
    private boolean is_only_choose_contact = false;

    @Override
    protected int setLayout() {
        return R.layout.acitivity_my_contact_list;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }

        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
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
                } else {
                    handleLogic();
                }

            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(MyContactListAcitivity.this, LoginActivity.class, LOGIN_TO_CONTACT_LIST);//跳转到登录界面
    }

    private void handleLogic() {
        initTittleBar();
        initRecyclerView();
        is_only_choose_contact =this.getIntent().getBooleanExtra(Constants.IS_ONLY_CHOOSE_CONTACT,false);//默认情况下是没有的
        Log.d("MyContactListAcitivity", "is_only_choose_contact:" + is_only_choose_contact);

        if (isLogin){
            requestCount = 45;
            getDataFromServer(true);
        }else {
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }
            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看我的联系人！");
        }

    }

    private void getDataFromServer(final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            if (!progressDialog.isShowing()){
                progressDialog.show();
            }

        }
        this.appAction.getContactList(new ActionCallbackListener<List<ContactBean>>() {
            @Override
            public void onSuccess(List<ContactBean> Contactlist) {
                if (progressDialog.isShowing()){
                    progressDialog.cancel();
                }

                progressActivity.showContent();
                totalCount = Contactlist.size(); // 因为不是分页，所以这个就是全部数据了

                if (totalCount == 0) {
                    progressActivity.showEmpty("收件人信息为空，亲，请添加一个！");
                } else {
                    //判断数据是否到底了,没有数据了就到底了
                    if (adapter.getData().isEmpty()) {
                        initAdapter(Contactlist);//这个其实也只是会执行一次
                    } else {

                        if (!isRefreshOrFirstLoading) {
                            adapter.addData(Contactlist);
                        } else {
                            if (!Contactlist.isEmpty()) {
                                adapter.setNewData(Contactlist);//有数据才会更新，没有数据是不会更新的
                                adapter.removeAllFooterView();
                        }
                        }
                    }
                }
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
            }

            @Override
            public void onFailure(String errorEvent, String message) {


                if ("998".equals(errorEvent)&& requestCount > 0) {
                    requestCount--;
                    getDataFromServer(true);
//                    refresh();
                } else if ("003".equals(errorEvent)) {
                    ToastUtil.networkUnavailable();
                    handleError("易，请检查网络");
                }else if ("996".equals(errorEvent)){
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    progressActivity.showContent();
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorEvent, message);
                    handleError("加载失败，请重新点击加载");
                }

            }
        });

    }
    private void handleError(String Message) {
        if (progressDialog.isShowing()){
            progressDialog.cancel();
        }

        ishasData = (!adapter.getData().isEmpty())  ? true : false;
            if (!ishasData) {
                showError(Message);
            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }
    }


    private void showError(String message) {
        progressActivity.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCount = 45;
                getDataFromServer(true);
            }
        });
    }

    private void refresh() {
        ishasData = (!adapter.getData().isEmpty()) ?  true : false;
        if (!ishasData) {
            getDataFromServer(true);

        } else {
            //请求失效，数据不变
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }

    private void initAdapter(List<ContactBean> listData) {
        if (is_only_choose_contact){
            adapter = new ContactListAdapter(listData,true);
        }else {
            adapter = new ContactListAdapter(listData,false);
        }
        //测试下

        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            Log.d("MyContactListAcitivity", "执行");

            //如果是从设置界面过来，则对子item的子控件进行监听，如果是从确认订单过来则只对子item进行监听
            Log.d("MyContactListAcitivity", "is_only_choose_contact:" + is_only_choose_contact);
            if (is_only_choose_contact) {
                Log.d("MyContactListAcitivity", "整个注册监听");
                mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                    @Override
                    public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Log.d(TAG, "执行");
                        ContactBean bean = (ContactBean) adapter.getItem(position);
                        final long ContactManageID = bean.getContactManageID(); //联系人ID
                        SetDefaultContact(ContactManageID, bean);
                    }
                });
            }else {
                Log.d("MyContactListAcitivity", "单个注册监听");

                mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemChildClickListener() {
                    @Override
                    public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, final int
                            position) {
                        ContactBean bean = (ContactBean) adapter.getItem(position);
                        final long ContactManageID = bean.getContactManageID(); //联系人ID
                        boolean isDefault = bean.IsDefault();
                        Log.d(TAG, "执行了吗");
                        Log.d(TAG, "view.getId():" + view.getId());
                        switch (view.getId()) {
                            case R.id.id_edit_:
                                Log.d("MyContactListAcitivity", "点击编辑");
                                //点击编辑
                                Bundle bundle = new Bundle();
                                /*
                                * http://blog.csdn.net/LauraChen93/article/details/51960250?locationNum=5
                                * 应该直接传递一个对象，这样写逻辑太不清晰了，改过来
                                *
                                * */
                                bundle.putString(Constants.CONTACT_NAME, bean.getContactName());
                                bundle.putString(Constants.CONTACT_ADDRESS, bean.getContactAddress());
                                bundle.putString(Constants.CONTACT_ADDRESS_, bean.getProvinceName() + bean.getCityName()
                                        + bean.getDistrictName());
                                bundle.putString(Constants.CONTACT_MOBILE, bean.getContactMobile());
                                bundle.putBoolean(Constants.IS_EDIT_CONTACT, true);
                                bundle.putString(Constants.PROVINCE_ID, Long.toString(bean.getProvinceID()));
                                bundle.putString(Constants.CITY_ID, Long.toString(bean.getCityID()));
                                bundle.putString(Constants.DISTRICT_ID, Long.toString(bean.getDistrictID()));
                                bundle.putLong(Constants.CONTACT_MANAGE_ID, bean.getContactManageID());
                                bundle.putString(Constants.PROVINCE_NAME, bean.getProvinceName());
                                bundle.putString(Constants.CITY_NAME, bean.getCityName());
                                bundle.putString(Constants.DISTRICT_NAME, bean.getDistrictName());
                                Log.d(TAG, "bundle.getBoolean(Constants.IS_EDIT_CONTACT):" + bundle.getBoolean
                                        (Constants.IS_EDIT_CONTACT));
                                IntentUtil.gotoActivityForResultWithData(MyContactListAcitivity.this, AddContactActivity
                                                .class, TO_EDIT,
                                        bundle);
                                break;
                            case R.id.id_delete_:
                                Log.d("MyContactListAcitivity", "点击删除");// 这几个地方当然需要验证下
                                final SweetAlertDialog pDialog = new SweetAlertDialog(MyContactListAcitivity.this, SweetAlertDialog.WARNING_TYPE);//只能这样写
                                pDialog.setTitleText("确定要删除吗？")
                                        .setConfirmText("删除")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                pDialog.dismissWithAnimation();
                                                requestCount = 10;
                                                delContact(ContactManageID, position);

                                            }
                                        })
                                        .setCancelText("取消")
                                        .show();
                                pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        pDialog.dismissWithAnimation();//其实不绘制也是可以的
                                    }
                                });




                                //点击删除，简单的逻辑自己来，麻烦的逻辑交给服务器，弹出一个确定要删除的对话框
                                /*dialog = DialogPlus.newDialog(MyContactListAcitivity.this)
                                        .setContentHolder(new ViewHolder(R.layout.item_dialog_delete))
                                        .setGravity(Gravity.CENTER)
                                        .setCancelable(true)   // 如果设置为不可取消，真的可以省去很多麻烦呢！
                                        .setExpanded(false)  //设置为不可滑动
                                        .setOnBackPressListener(new OnBackPressListener() {
                                            @Override
                                            public void onBackPressed(DialogPlus dialogPlus) {
                                                Log.d(TAG, " back button is pressed");
                                                dialog.dismiss();

                                            }
                                        })
                                        .setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(DialogPlus dialog, View view) {
                                                switch (view.getId()) {
                                                    case R.id.conform:
                                                        Log.d(TAG, "确定");
                                                        requestCount = 5;
                                                        delContact(ContactManageID, position);
                                                        break;
                                                    case R.id.cancel:
                                                        Log.d(TAG, "取消");
                                                        dialog.dismiss();
                                                        break;
                                                }
                                            }
                                        })
                                        .create();
                                dialog.show();*/
                                break;
                            case R.id.all_radio_button:
                                //设置默认联系人
                                Log.d(TAG, "设置默认联系人");
                                if (!isDefault) {
                                    requestCount = 10;
                                    SetDefaultContact(ContactManageID, bean);
                                }
                                break;
                        }
                    }
                });
            }
            mSuperRecyclerViewImg.setAdapter(adapter);
        }

    }
    /*
    *



    *
    * */

    private void delContact(final long contactManageID, final int position) {
        this.appAction.delContact(contactManageID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                adapter.remove(position);//在服务器也需要删除
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    delContact(contactManageID, position);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    adapter.notifyDataSetChanged();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showHintMessage("删除失败",errorCode,errorMessage);
                    adapter.notifyDataSetChanged();
                }


            }
        });


    }


    private void SetDefaultContact(final long contactManageID, final ContactBean bean) {
        this.appAction.setDefaultContact(contactManageID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                //控件checked
/*                bean.setIsDefault(1);
                adapter.notifyDataSetChanged();  //难道这个adapter 也是需要从被调用者手中传递过来*/
                if (is_only_choose_contact){
                    finish();
                }else {
                    requestCount = 45;
                    getDataFromServer(true);
                }

            }
            /*如果是这样就可以减大量的逻辑量了，真的是减少大量的逻辑啊！不重复写逻辑了*/
            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    SetDefaultContact(contactManageID, bean);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    bean.setIsDefault(0);
                    adapter.notifyDataSetChanged(); //如果失败则不变化
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    bean.setIsDefault(0);
                    loginForToken();
                    adapter.notifyDataSetChanged(); //如果失败则不变化

                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    bean.setIsDefault(0);
                    adapter.notifyDataSetChanged(); //如果失败则不变化
                }

            }
        });

    }


    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerViewImg.setRefreshListener(this);
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);

    }

    @Override
    protected void initViews() {

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
                MyContactListAcitivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("管理我的联系人信息");
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

    /*新建联系人*/
    @OnClick(R.id.btn_save)
    public void onClick() {
//        IntentUtil.gotoActivityWithoutData(MyContactListAcitivity.this, AddContactActivity.class, false);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_EDIT_CONTACT, false);
        IntentUtil.gotoActivityForResultWithData(MyContactListAcitivity.this,
                AddContactActivity.class, TO_EDIT, bundle);
    }

    @Override
    public void onRefresh() {
        requestCount = 45;
        getDataFromServer(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TO_EDIT:
                /*
                * 编辑的逻辑是只有真正添加或者编辑了才会刷新，
                * */
                //refreshContactList(data);
                Log.d("MyContactListAcitivity", "回调");
                requestCount = 45;
                getDataFromServer(true);
                break;
            case LOGIN_TO_CONTACT_LIST:
                handleLogic();
                break;
        }

    }

/*    private void refreshContactList( Intent data) {
        if (null !=data ){
            if (position_!=-1){
                ContactBean bean = (ContactBean) adapter.getItem(position_);
                bean.setContactName(data.getStringExtra(Constants.CONTACT_NAME));
                bean.setContactMobile(data.getStringExtra(Constants.CONTACT_MOBILE));
                bean.setContactAddress(data.getStringExtra(Constants.CONTACT_ADDRESS));
                bean.setCityID(Long.parseLong(data.getStringExtra(Constants.CITY_ID)));
                bean.setCityName(data.getStringExtra(Constants.CITY_NAME));
                bean.setProvinceID(Long.parseLong(data.getStringExtra(Constants.PROVINCE_ID)));
                bean.setProvinceName(data.getStringExtra(Constants.PROVINCE_NAME));
                bean.setDistrictID(Long.parseLong(data.getStringExtra(Constants.DISTRICT_ID)));
                bean.setDistrictName(data.getStringExtra(Constants.DISTRICT_NAME));
                adapter.notifyDataSetChanged();
            }
        }

    }*/
}
