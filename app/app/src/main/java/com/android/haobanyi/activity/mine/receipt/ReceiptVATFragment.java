package com.android.haobanyi.activity.mine.receipt;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.city.AreaList;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.KeyBoardUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.util.citypickerview.model.CityModel;
import com.android.haobanyi.util.citypickerview.model.DistrictModel;
import com.android.haobanyi.util.citypickerview.model.ProvinceModel;
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
 * Created by fWX228941 on 2016/8/5.
 *
 * @作者: 付敏
 * @创建日期：2016/08/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：增值税发票
 */
public class ReceiptVATFragment extends BaseFragment {
    @BindView(R.id.edit_companyname)
    EditText editCompanyname;
    @BindView(R.id.edit_identitycode)
    EditText editIdentitycode;
    @BindView(R.id.edit_registeraddress)
    EditText editRegisteraddress;
    @BindView(R.id.edit_bankname)
    EditText editBankname;
    @BindView(R.id.edit_registerphone)
    EditText edit_registerphone;
    @BindView(R.id.edit_bankaccount)
    EditText editBankaccount;
    @BindView(R.id.edit_ConsigneeName)
    EditText edit_ConsigneeName;
    @BindView(R.id.edit_ConsigneePhone)
    EditText edit_ConsigneePhone;
    @BindView(R.id.service_add)
    RelativeLayout service_add;
    @BindView(R.id.edit_ConsigneeAddress)
    EditText edit_ConsigneeAddress;
    @BindView(R.id.textView51)
    TextView editadd_;

    @BindView(R.id.id_test)
    TextView idTest;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.id_hint)
    TextView idHint;
    private String edit_Companyname;

    /*省市区*/
    private ArrayList<ProvinceModel> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<CityModel>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<DistrictModel>>> options3Items = new ArrayList<>();
    private String CityID;
    private String DistrictID;
    private String ProvinceID;
//    private String ProvinceName;
//    private String CityName;
//    private String DistrictName;
    OptionsPickerView pvOptions;


    private static final int LOGIN_TO_RECEIPT_VA = 3;
    @Override
    protected void lazyload() {
        pvOptions = new OptionsPickerView(getActivity());
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
//                ProvinceName = options1Items.get(options1).getPickerViewText();
//                CityName = options2Items.get(options1).get(option2).getPickerViewText();
//                DistrictName = options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                service_add.setEnabled(true);
            }
        });

    }

    private void initCityData() {
        try {
            options1Items = getDataList();
            if (null!=options1Items){
                for (int i = 0; i < options1Items.size(); i++) {
                    ArrayList<CityModel>  cityList = (ArrayList<CityModel>) options1Items.get(i).getCityList();//这个地方是不是需要修改一下呢！
                    options2Items.add(cityList);
                    ArrayList<ArrayList<DistrictModel>> options3Items_01 = new ArrayList<>();
                    ArrayList<DistrictModel> options3Items_01_01;
                    for (int j=0;j<cityList.size();j++){
                        options3Items_01_01 = (ArrayList<DistrictModel>)cityList.get(j).getDistrictList();
                        options3Items_01.add(options3Items_01_01);
                    }
                    options3Items.add(options3Items_01);
                }
            }


        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private ArrayList<ProvinceModel> getDataList() {
        AssetManager asset = getActivity().getAssets();
        try {
            InputStream input = asset.open("preferences_city.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            String json = buffer.toString();
            Gson GSON = new Gson();
            AreaList resourcelist = GSON.fromJson(json, AreaList.class);
            List<AreaList.DataBean> resourcelist01= resourcelist.getData();
            ArrayList<ProvinceModel> provinceList01 = new ArrayList<>();
            //应该在解析的时候
            for (int i =0;i<resourcelist01.size();i++){
                List<AreaList.DataBean.CityListBean> citylist01 =  resourcelist01.get(i).getCityList();
                List<CityModel> cityList = new ArrayList<>();
                for (int j=0;j<citylist01.size();j++){
                    List<AreaList.DataBean.CityListBean.DistrictListBean> districtlist01 =   citylist01.get(j).getDistrictList();
                    List<DistrictModel> districtList = new ArrayList<>();
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
        }
        return null;
    }


    @Override
    protected int setLayout() {
        return R.layout.fragment_receipt_vat;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000L);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }

        } else {
            handleLogic();
        }


    }

    private void handleLogic() {

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
        IntentUtil.gotoActivityForResult(getActivity(), LoginActivity.class, LOGIN_TO_RECEIPT_VA);//跳转到登录界面
    }
    /*
    * 有些是字段是没有添加的，已经在联系人信息中了
    *
    * */
    @OnClick({R.id.btn_login,R.id.service_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_add:
                chooseServiceAdd();
                break;
            case R.id.btn_login:
                toSaveContactList();
                break;
        }
    }
    private void chooseServiceAdd() {
        service_add.setEnabled(false);
        pvOptions.show();
        // 把键盘给去掉
        KeyBoardUtil.getInstance(getActivity()).hide();//这个还是有点悬的
    }
    private void toSaveContactList() {
        edit_Companyname = editCompanyname.getText().toString();
        String edit_Identitycode = editIdentitycode.getText().toString();
        String edit_Registeraddress = editRegisteraddress.getText().toString();
        String edit_Bankname = editBankname.getText().toString();
        String edit_Bankaccount = editBankaccount.getText().toString();
        String edit_RegisterPhone = edit_registerphone.getText().toString();
        String consigneeName = edit_ConsigneeName.getText().toString();
        String consigneePhone = edit_ConsigneePhone.getText().toString();
        String consigneeAddress = edit_ConsigneeAddress.getText().toString();

        if (TextUtils.isEmpty(edit_RegisterPhone)){
            setHintMessage("注册电话不能为空");
            return;
        }
        if (TextUtils.isEmpty(consigneeName)){
              setHintMessage("收票人名称不能为空");
              return;
        }
        if (TextUtils.isEmpty(consigneePhone)){
          setHintMessage("收票人电话不能为空");
          return;
        }
        if (TextUtils.isEmpty(consigneeAddress)){
          setHintMessage("收票人详细地址不能为空");
          return;
        }
        if (editadd_.getText().toString().equals("省/市/区")){
          setHintMessage("省市区地址还没有选择");
          return;
        }
        //监听字符长度，这个我也是醉了
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.COMPANYNAME,edit_Companyname);
        params.put(Constants.IDENTITYCODE, edit_Identitycode);
        params.put(Constants.REGISTERADDRESS, edit_Registeraddress);
        params.put(Constants.BANKNAME, edit_Bankname);
        params.put(Constants.BANKACCOUNT, edit_Bankaccount);
        params.put("RegisterPhone", edit_RegisterPhone);
        params.put("ConsigneeProvinceId",ProvinceID);
        params.put("ConsigneeCityId",CityID);
        params.put("ConsigneeDistrictId",DistrictID);
        params.put("ConsigneeAddress", consigneeAddress);
        params.put("ConsigneePhone", consigneePhone);
        params.put("ConsigneeName", consigneeName);
        if (btnLogin.isEnabled()){
            btnLogin.setEnabled(false);
        }

        requestCount = 45;
        saveVatInvoice(params);
    }

    private void saveVatInvoice(final HashMap<String, String> params) {
        this.appAction.saveVatInvoice(params, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                if (!btnLogin.isEnabled()){
                    btnLogin.setEnabled(true);
                }
                setHintMessage(" ");
                backToReceipt();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btnLogin.isEnabled()){
                    btnLogin.setEnabled(true);
                }

                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    saveVatInvoice(params);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    setHintMessage(errorMessage);
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    setHintMessage("请再次点击确认");
                }


            }
        });
    }

    private void setHintMessage(String errorMessage) {
        if (!btnLogin.isEnabled()){
            btnLogin.setEnabled(true);
        }
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(errorMessage);


    }

    private void backToReceipt() {
        ToastUtil.showSuccessfulMessage(getActivity(),"成功保存");
        Intent intent =new Intent();
        intent.putExtra(Constants.RECEIPT_NAME, edit_Companyname);
        intent.putExtra(Constants.INVOICE_TYPE, 2);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
    }

    //LOGIN_TO_RECEIPT_VA

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_TO_RECEIPT_VA:
                handleLogic();
                break;

        }
    }
}
