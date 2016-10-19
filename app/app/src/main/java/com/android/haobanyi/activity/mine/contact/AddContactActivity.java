package com.android.haobanyi.activity.mine.contact;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.guid.register.BindMobileOrEmailActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.city.AreaList;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.RegexUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.KeyBoardUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.util.citypickerview.model.CityModel;
import com.android.haobanyi.util.citypickerview.model.DistrictModel;
import com.android.haobanyi.util.citypickerview.model.ProvinceModel;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/8/3.
 *
 * @作者: 付敏
 * @创建日期：2016/08/03
 * @邮箱：466566941@qq.com
 * @当前文件描述： 添加/编辑联系人模块
 *
 * 简化版逻辑：
 * 1.如果有地址存在，则跳转到我的地址列表MyContactListAcitivity中
 *  1）如果启动界面是ConformOrderActivity，则设置监听器，选择联系人信息后，一方面设置为默认地址，并修改勾选状态，另外一方面返回到ConformOrderActivity，并更新【去除勾选，添加一行说明文字】
 *  2）如果从设置界面启动过来，则不设置监听器，但是可以编辑【增加一个编辑按钮】
 * 2.如果没有地址存在，则跳转到添加联系人界面AddContactActivity中
 *
 *
 *
 *
 *
 */
public class AddContactActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_name)
    ClearEditText editName;
    @BindView(R.id.edit_phone)
    ClearEditText editPhone;
    @BindView(R.id.textView51)
    TextView editadd_;
    @BindView(R.id.imageView53)
    ImageView imageView53;
    @BindView(R.id.edit_add)
    ClearEditText editAdd;
    @BindView(R.id.service_add)
    RelativeLayout service_add;
    @BindView(R.id.btn_save)
    Button btnSave;
    private String contactName;
    private String contactMobile;
    private String contactAddress;
    private String ProvinceID;
    private String CityID;
    private String DistrictID;
    private String ProvinceName;
    private String CityName;
    private String DistrictName;
    private long ContactManageID = -1;
    @BindView(R.id.id_hint)
    TextView idHint; // 验证提示框
    private  boolean is_edit_contact=false;
    private String contactaddress_;
    private static final int LOGIN_TO_ADD_CONTACT = 3;
    /*省市区*/
    private ArrayList<ProvinceModel> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<CityModel>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<DistrictModel>>> options3Items = new ArrayList<>();
    OptionsPickerView pvOptions;
    @Override
    protected int setLayout() {
        return R.layout.activity_add_contact;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        /*
        * 第二次出现bundle 失效的情况了，只能先用文件传递了
        *
        * */

        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                Log.d(TAG, "执行");
                getToken();
            } else {
                handlelogic();
            }
        } else {
            handlelogic();
        }

    }

    private void getToken() {
        this.appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                handlelogic();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getToken();
                } else {
                    handlelogic();
                }
            }
        });
    }

    private void handlelogic() {

        initTittleBar();
        is_edit_contact =AddContactActivity.this.getIntent().getBooleanExtra(Constants.IS_EDIT_CONTACT,false);
        Log.d("AddContactActivity", "is_edit_contact:" + is_edit_contact);
        if (is_edit_contact){
            ContactManageID = this.getIntent().getLongExtra(Constants.CONTACT_MANAGE_ID,-1);
            contactName=this.getIntent().getStringExtra(Constants.CONTACT_NAME);
            contactAddress=this.getIntent().getStringExtra(Constants.CONTACT_ADDRESS);
            contactaddress_=this.getIntent().getStringExtra(Constants.CONTACT_ADDRESS_);
            contactMobile=this.getIntent().getStringExtra(Constants.CONTACT_MOBILE);
            ProvinceID=this.getIntent().getStringExtra(Constants.PROVINCE_ID);
            CityID=this.getIntent().getStringExtra(Constants.CITY_ID);
            DistrictID=this.getIntent().getStringExtra(Constants.DISTRICT_ID);
            ProvinceName =this.getIntent().getStringExtra(Constants.PROVINCE_NAME); ;
            CityName=this.getIntent().getStringExtra(Constants.CITY_NAME);;
            DistrictName=this.getIntent().getStringExtra(Constants.DISTRICT_NAME);;
        }

        if (is_edit_contact){
            editName.setText(contactName);
            editPhone.setText(contactMobile);
            editAdd.setText(contactAddress);
            editadd_.setText(contactaddress_);
        }

    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(AddContactActivity.this, LoginActivity.class, LOGIN_TO_ADD_CONTACT);//跳转到登录界面
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
                AddContactActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        if (is_edit_contact){
            titleBar.setTitle("编辑联系信息");
        }else {
            titleBar.setTitle("填写联系信息");
        }

        titleBar.setDividerColor(Color.GRAY);
    }

    @Override
    protected void loadData() {
        pvOptions = new OptionsPickerView(this);
        initCityData();
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, true, true);
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnDismissListener(new com.bigkoo.pickerview.listener.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                service_add.setEnabled(true);
            }
        });
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String tx = options1Items.get(options1).getPickerViewText()
                        + options2Items.get(options1).get(option2).getPickerViewText()
                        + options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                ProvinceID = options1Items.get(options1).getProvinceID();
                CityID = options2Items.get(options1).get(option2).getCityID();
                DistrictID = options3Items.get(options1).get(option2).get(options3).getDistrictID();
                editadd_.setText(tx);
                ProvinceName = options1Items.get(options1).getPickerViewText();
                CityName = options2Items.get(options1).get(option2).getPickerViewText();
                DistrictName = options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                service_add.setEnabled(true);
            }
        });

    }
    private void initCityData() {
        try {
            options1Items = getDataList();
            for (int i = 0; i < options1Items.size(); i++) {
                ArrayList<CityModel>  cityList = (ArrayList<CityModel>) options1Items.get(i).getCityList();//这个地方是不是需要修改一下呢！
                options2Items.add(cityList);
                ArrayList<ArrayList<DistrictModel>> options3Items_01 = new ArrayList<>();
                ArrayList<DistrictModel> options3Items_01_01 = new ArrayList<>();
                for (int j=0;j<cityList.size();j++){
                    options3Items_01_01 = (ArrayList<DistrictModel>)cityList.get(j).getDistrictList();
                    options3Items_01.add(options3Items_01_01);
                }
                options3Items.add(options3Items_01);
            }


        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }



    }
    private ArrayList<ProvinceModel> getDataList() {
        AssetManager asset = this.getAssets();
        try {
            InputStream input = asset.open("preferences_city.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            String json = buffer.toString();
            Gson GSON = new Gson();
            AreaList resourcelist = GSON.fromJson(json, AreaList.class);
            List<AreaList.DataBean> resourcelist01= resourcelist.getData();
            ArrayList<ProvinceModel> provinceList01 = new ArrayList<ProvinceModel>();
            //应该在解析的时候
            for (int i =0;i<resourcelist01.size();i++){
                List<AreaList.DataBean.CityListBean> citylist01 =  resourcelist01.get(i).getCityList();
                List<CityModel> cityList = new ArrayList<CityModel>();
                for (int j=0;j<citylist01.size();j++){
                    List<AreaList.DataBean.CityListBean.DistrictListBean> districtlist01 =   citylist01.get(j).getDistrictList();
                    List<DistrictModel> districtList = new ArrayList<DistrictModel>();
                    for (int k =0;k<districtlist01.size();k++){
                        String DistrictID = districtlist01.get(k).getDistrictID();
                        String Name = districtlist01.get(k).getName();
                        String CityID = districtlist01.get(k).getCityID();
                        DistrictModel districtbean = new  DistrictModel(Name, DistrictID, CityID);
                        districtList.add(districtbean);
                    }
                    String CityID =citylist01.get(j).getCityID() ;
                    String Name =citylist01.get(j).getName();
                    String ProvinceID =citylist01.get(j).getProvinceID();
                    CityModel cityBean = new  CityModel(Name, districtList,CityID, ProvinceID);
                    cityList.add(cityBean);
                }

                String ProvinceID = resourcelist01.get(i).getProvinceID();
                String Name = resourcelist01.get(i).getName() ;
                ProvinceModel provinceBean = new ProvinceModel(Name, cityList, ProvinceID);
                provinceList01.add(provinceBean);
            }

            return provinceList01;

        }catch(IOException e){
            e.printStackTrace();
        }finally{
        }
        return null;
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




    @OnClick({R.id.btn_save,R.id.service_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_add:
                chooseServiceAdd();
                break;
            case R.id.btn_save:
                toSaveContactList();
                break;
        }
    }
    /*
    * 逻辑一定要清晰：保存联系人信息
    * 方案一：不刷新确认订单界面，销毁当前界面，携带数据给确认订单界面
    * 方案二：刷新确认订单界面 【保证数据的一致性，】 可行性：关联性界面不需要写监听器
    *
    * */
    private void toSaveContactList() {
        contactName = editName.getText().toString();
        if (TextUtils.isEmpty(contactName)){
            setHintMessage("联系人姓名不能为空");
            return;
        }

        contactMobile= editPhone.getText().toString();
        if (TextUtils.isEmpty(contactName)){
            setHintMessage("手机号码不能为空");
            return;
        }
        if (!RegexUtil.validateMobile(contactMobile)){
            setHintMessage("手机号格式不正确");
            return;
        }

        if (editadd_.getText().toString().equals("省/市/区")){
            setHintMessage("省市区地址还没有选择");
            return;
        }

        contactAddress = editAdd.getText().toString();
        if (TextUtils.isEmpty(contactAddress)){
            setHintMessage("详细地址不能为空");
            return;
        }

        HashMap<String, String> params =new HashMap<String, String>();
        params.put("ContactName",contactName);
        params.put("ProvinceID",ProvinceID);
        params.put("CityID",CityID);
        params.put("DistrictID",DistrictID);
        params.put("ContactAddress",contactAddress);
        params.put("ContactMobile", contactMobile);
        if (is_edit_contact){
            params.put("ContactManageID",Long.toString(ContactManageID));  //这个还有一个逻辑就是要判断省市区是否变化了
        }

        btnSave.setEnabled(false);// 这一步就是为了防止用户反复点击。点击事情设置最迟最新的原则，有头有尾形成一个循环
        Log.d("AddContactActivity", "is_edit_contact:" + is_edit_contact);
        if (is_edit_contact) {
            //编辑联系人
            Log.d("AddContactActivity", "编辑联系人");
            requestCount = 10;
            editContact(params);
        }else {
            //新增联系人
            Log.d("AddContactActivity", "新增联系人");
            requestCount = 10;
            addContact(params);
        }
    }

    private void editContact(final HashMap<String, String> params) {
        this.appAction.editContact(params, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                if (!btnSave.isEnabled()){
                    btnSave.setEnabled(true);
                }
                setHintMessage(" ");
                finish();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btnSave.isEnabled()){
                    btnSave.setEnabled(true);
                }

                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    editContact(params);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    setHintMessage(errorMessage);
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    setHintMessage(errorMessage);
                }

            }});

    }

    private void setHintMessage(String message){
        if (!btnSave.isEnabled()){
            btnSave.setEnabled(true);
        }
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(message);


    }

    private void addContact(final HashMap<String, String> params) {
        this.appAction.addContact(params, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                if (!btnSave.isEnabled()){
                    btnSave.setEnabled(true);
                }

                setHintMessage(" ");
                //简单处理统一刷新
/*                if (is_edit_contact){
                    //那就直接跳转到返回界面，并且把数据传递过去
                    Intent intent = new Intent();
                    intent.putExtra(Constants.CONTACT_NAME,contactName);
                    intent.putExtra(Constants.CONTACT_ADDRESS,contactAddress);
                    intent.putExtra(Constants.CONTACT_MOBILE,contactMobile);
                    intent.putExtra(Constants.PROVINCE_ID,ProvinceID);
                    intent.putExtra(Constants.CITY_ID,CityID);
                    intent.putExtra(Constants.DISTRICT_ID,DistrictID);
                    intent.putExtra(Constants.PROVINCE_NAME,ProvinceName);
                    intent.putExtra(Constants.CITY_NAME,CityName);
                    intent.putExtra(Constants.DISTRICT_NAME, DistrictName);
                    setResult(RESULT_OK, intent);
                }*/
                Log.d("AddContactActivity", "确定");
                finish();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btnSave.isEnabled()){
                    btnSave.setEnabled(true);
                }

                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    addContact(params);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    setHintMessage(errorMessage);
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    setHintMessage(errorMessage);
                }

            }
        });
    }

    private void chooseServiceAdd() {
        service_add.setEnabled(false);
        pvOptions.show();
        // 把键盘给去掉
        KeyBoardUtil.getInstance(AddContactActivity.this).hide();
    }
    //LOGIN_TO_ADD_CONTACT

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_TO_ADD_CONTACT:
                handlelogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;
        }
    }
}
