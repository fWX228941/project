package com.android.haobanyi.activity.shopping.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.mine.contact.AddContactActivity;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.adapter.product.ProductSatisfySendAdapter;
import com.android.haobanyi.adapter.product.ProductShopAttrAdapter;
import com.android.haobanyi.adapter.product.ProductVouchersTemplateAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackQuadrupleListener;
import com.android.haobanyi.model.bean.city.AreaList;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.KeyBoardUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.util.citypickerview.model.CityModel;
import com.android.haobanyi.util.citypickerview.model.DistrictModel;
import com.android.haobanyi.util.citypickerview.model.ProvinceModel;
import com.android.haobanyi.util.citypickerview.widget.CityPickerView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created by fWX228941 on 2016/6/22.
 *
 * @作者: 付敏
 * @创建日期：2016/06/22
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务首页
 */
public class ShoppingServiceFragment extends BaseFragment {

    @BindView(R.id.product_img)
    ImageView productImg;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.id_tv_comprehensiveScore)
    TextView comprehensiveScore;
    @BindView(R.id.minPrice)
    TextView minPrice;
    @BindView(R.id.discount_price)
    TextView discountPrice;
    @BindView(R.id.order_num)
    TextView orderNum;
    @BindView(R.id.shop_logo)
    ImageView shopLogo;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.shop_add)
    TextView shopAdd;
    @BindView(R.id.id_share)
    ImageView share;
    @BindView(R.id.coupon)
    LinearLayout coupon;
    @BindView(R.id.tv_coupon)
    TextView tv_coupon;
    @BindView(R.id.shop_bonus)
    LinearLayout shop_bonus;
    @BindView(R.id.haobanyi_bonus)
    LinearLayout haobanyi_bonus;
    @BindView(R.id.full_to_discount)
    LinearLayout full_to_discount;
    @BindView(R.id.tv_full_to_discount)
    TextView tv_full_to_discount;
    @BindView(R.id.other_service)
    LinearLayout other_service;
    @BindView(R.id.tv_other_service)
    TextView tv_other_service;
    @BindView(R.id.service_add)
    LinearLayout service_add;//tv_service_add
    @BindView(R.id.tv_service_add)
    TextView tv_service_add;
    @BindView(R.id.id_goto_shop)
    LinearLayout goto_shop;
    @BindView(R.id.id_discount_package)
    LinearLayout discount_package;
    @BindView(R.id.discount_item)
    LinearLayout discount_item;
    private long shopID;
    private long productID;
    ProductDetailsBean productBean;

    /*满即送*/
    private ProductSatisfySendAdapter satisfySendAdapter;
    private List<SatisfySendBean> satisfySendlistData = new ArrayList<SatisfySendBean>();//不初始化，默认就是空了
    private DialogPlus satisfySendDialog; //弹框

    /*优惠券*/
    private ProductVouchersTemplateAdapter voucherstemplateadapter = new ProductVouchersTemplateAdapter(getActivity(),null);
    private List<VouchersTemplateBean> vouchersTemplalistData = new ArrayList<VouchersTemplateBean>();
    private DialogPlus vouchersTemplateDialog; //弹框

    /*其他服务*/
    private ProductShopAttrAdapter shopAttradapter  = new ProductShopAttrAdapter(getActivity(), null);
    private List<ShopAttrBean> shopAttrlistData = new ArrayList<ShopAttrBean>();
    private DialogPlus shopAttrDialog; //弹框
    private long tempId = -1;
    private VouchersTemplateBean bean = null;
    private ShopAttrBean shopattrbean = null;
    private long shopAttrID = -1;

    List<ShopAttrBean> shopAttrIDlist = new  ArrayList<ShopAttrBean>();
    private static final int SELECTION = 1;

    /*省市区*/
    private ArrayList<ProvinceModel> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<CityModel>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<DistrictModel>>> options3Items = new ArrayList<>();
    OptionsPickerView pvOptions;
    private  List<DetailsBean.DataBean.CLBean> citylistData = new ArrayList<DetailsBean.DataBean.CLBean>();//不初始化，默认就是空了



    /*城市选择的逻辑需要完善下*/
    @Override
    protected void lazyload() {
        /*
        *
        *   java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Object android.content.Context.getSystemService(java.lang.String)' on a null object reference
        * */


    }

    private void initCitySelector() {
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
  /*               String ProvinceID =options1Items.get(options1).getProvinceID() ;
                 String CityID = options2Items.get(options1).get(option2).getCityID();*/
                 String DistrictID = options3Items.get(options1).get(option2).get(options3).getDistrictID() ;
                tv_service_add.setText(tx);
                service_add.setEnabled(true);
                basePreference.putString("DistrictID_ADD",DistrictID);
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_shopping_service;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        productID = getActivity().getIntent().getLongExtra(Constants.PRODUCT_ID, -1);
        shopID = getActivity().getIntent().getLongExtra(Constants.SHOP_ID, -1);
        if (productID !=-1){
            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(activity, "product");
            productBean = complexPreferences.getObject("productid" + productID, ProductDetailsBean.class);
            shopID = productBean.getShopID();

            satisfySendlistData = complexPreferences.getListObject("productid" + productID + "SSList",
                    SatisfySendBean.class);
            vouchersTemplalistData = complexPreferences.getListObject("productid" + productID+"VTList",VouchersTemplateBean.class);
            shopAttrlistData = complexPreferences.getListObject("productid" + productID + "SAList", ShopAttrBean.class);

            if (shopID ==-1){
                requestCount = 5;
                getProduct();
            }else{
                loadData(productBean);
            }
        }
        setUpListener();
    }

    private void setUpListener() {
        ShoppingRadioGroupActivity activity = (ShoppingRadioGroupActivity) this.getActivity();
        activity.setOnCitySelectedBackListener(new ShoppingRadioGroupActivity.OnCitySelectedBackListener() {
            @Override
            public boolean onCitySelectedBack() {
                if (pvOptions.isShowing()) {
                    pvOptions.dismiss();
                    service_add.setEnabled(true);
                    return true;//明明很简答的逻辑，自己想复杂了，这个也是标识
                }else {
                    return false;//正常逻辑处理
                }

            }
        });

        /*恢复到初始状态*/
        activity.setOnRefreshListener(new ShoppingRadioGroupActivity.OnRefreshListener() {
            @Override
            public void onRefresh() {
                minPrice.setText("￥" + Double.toString(productBean.getMinPrice()));
                initOtherService();
                tv_service_add.setText("请选择服务区域");
                if (basePreference.contains("DistrictID_ADD")) {

                    basePreference.removeItem("DistrictID_ADD");
                }

/*                if ((null!=shopAttradapter)&&(!shopAttradapter.isEmpty())){
                    for (int i=0;i<shopAttradapter.getCount();i++){
                        shopattrbean = (ShopAttrBean)shopAttradapter.getItem(i);
                        shopattrbean.setIsSelected(false);
                    }
                }*/
            }
        });
    }

    private void getProduct() {
        this.appAction.getProduct01(productID, new ActionCallbackFivefoldListener<ProductDetailsBean,
                        List<SatisfySendBean>, List<VouchersTemplateBean>, List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>>() {
            @Override
            public void onSuccess(ProductDetailsBean data, List<SatisfySendBean> data01, List<VouchersTemplateBean>
                    data02, List<ShopAttrBean> data03, List<DetailsBean.DataBean.CLBean> data04) {
                satisfySendlistData = data01;
                vouchersTemplalistData = data02;
                shopAttrlistData = data03;
                shopID = data.getShopID();
                productBean = data;
                loadData(data);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getProduct();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.showShort("网络异常，请检查网络");
                } else {
                    Toast.makeText(getActivity(), "异常代码：" + errorCode +
                            " 异常说明: " + errorMessage, Toast
                            .LENGTH_SHORT).show();
                    ToastUtil.showShort("加载失败，请重新点击加载");
                }
            }
        });
    }

    private void loadData(ProductDetailsBean productBean) {
        initCitySelector();
        this.productBean = productBean;
        Glide.with(activity)
                .load(productBean.getImagePath())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.big_image)// 也就是网络加载失败时出现的图片
                .into(productImg);
        Glide.with(activity)
                .load(productBean.getShopLogo())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.logo)// 也就是网络加载失败时出现的图片
                .into(shopLogo);
        /* childViewHolder.id_tv_price.setText(String.format(context.getString(R.string.price), shoppingChildItemBean.getPrice())); 采用格式化的方式 <string name="price">￥%s</string>*/
        productName.setText(productBean.getProductName());

        //上面是优惠价，下面是原价，逻辑当 优惠价< 原价时都显示，当优惠价 = 原价时 ，显示原价，并且去掉斜线,并且隐藏商家优惠和优惠价discount_item
        if (productBean.getMinPrice()>productBean.getDiscountPrice()){
            discount_item.setVisibility(View.VISIBLE);
            discountPrice.setText("￥" + Double.toString(productBean.getDiscountPrice()));
            minPrice.setText("￥" + Double.toString(productBean.getMinPrice()));//红色的是优惠价，黑色的才是原价
            minPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 设置中划线并抗锯齿\
        }else {
            minPrice.setText("￥" + Double.toString(productBean.getMinPrice()));//红色的是优惠价，黑色的才是原价
            discount_item.setVisibility(View.GONE);
        }

        orderNum.setText("销量：" + Integer.toString(productBean.getOrderNum()));
        shopName.setText(productBean.getShopName());
        shopAdd.setText("地址："+productBean.getShopAdd());
        comprehensiveScore.setText(Double.toString(productBean.getComprehensiveScore()));

        /*初始化 服务选项*/
        initOtherService();
        /*满即送*/
        if (null == satisfySendlistData || satisfySendlistData.isEmpty()){
/*            full_to_discount.setClickable(false);//不可点击，说明文字变更
            tv_full_to_discount.setText("暂无满即送活动");*/
            full_to_discount.setVisibility(GONE);

        } else {
            full_to_discount.setVisibility(View.VISIBLE);
            full_to_discount.setClickable(true);
            tv_full_to_discount.setText("单笔订单满" + satisfySendlistData.get(0).getPrice() + "元，减" +
                        satisfySendlistData.get(0).getMoney() + "元");

        }

        /*优惠活动*/
        if (null == vouchersTemplalistData || vouchersTemplalistData.isEmpty()){
/*            coupon.setClickable(false);//不可点击，说明文字变更
            tv_coupon.setText("暂无优惠券可领取");*/
            coupon.setVisibility(GONE);
        } else {
            coupon.setVisibility(View.VISIBLE);
            coupon.setClickable(true);
            tv_coupon.setText("请点击领取店铺优惠券");
        }
        if (tv_service_add.getText().equals("请选择服务区域")){
            if (basePreference.contains("DistrictID_ADD")) {
                basePreference.removeItem("DistrictID_ADD");
            }
        }

    }

    private void initOtherService() {
        if (null == shopAttrlistData || shopAttrlistData.isEmpty()){
  /*          other_service.setClickable(false);//不可点击，说明文字变更
            tv_other_service.setText("暂无服务可选");*/
            other_service.setVisibility(GONE);
        } else {
            other_service.setVisibility(View.VISIBLE);
            other_service.setClickable(true);
            StringBuffer sb = new StringBuffer(256);
            for (int i = 0;i<shopAttrlistData.size();i++){
                sb.append(shopAttrlistData.get(i).getName() + " ");
            }
            tv_other_service.setText(sb.toString());
//            tv_other_service.setText("请点击选择其他服务");
        }
    }


    @OnClick({R.id.id_share, R.id.coupon, R.id.shop_bonus, R.id.haobanyi_bonus, R.id.full_to_discount, R.id
            .other_service, R.id.service_add, R.id.id_goto_shop, R.id.id_discount_package})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_share:
                share();
                break;
            case R.id.coupon:
                getCoupon();
                break;
            case R.id.shop_bonus:
                getShopBonus();
                break;
            case R.id.haobanyi_bonus:
                getHanbaoyiBonus();
                break;
            case R.id.full_to_discount:
                getDiscount();
                break;
            case R.id.other_service:
                chooseOtherService();
                break;
            case R.id.service_add:
                chooseServiceAdd();
                break;
            case R.id.id_goto_shop:
                Log.d("ShoppingServiceFragment", "进入到店铺");
                requestCount = 5;
                gotoShop();
                break;
            case R.id.id_discount_package:
                chooseDiscountPkg();
                break;
        }
    }
    //09.选择优惠套餐
    private void chooseDiscountPkg() {

    }
    /*
    * 参考设计：http://blog.csdn.net/zml_2015/article/details/50650226
    * Activity和fragment相互跳转
    *
    *先暂时通过文件，然后在替换成一个框架
    * */
    //08.进入店铺
    private void gotoShop() {
        Log.d("ShoppingServiceFragment", "shopID:" + shopID);
        if (shopID != -1) {

            Log.d("ShoppingServiceFragment", "shopID:难道进来后shopId 变了" + shopID);
            this.appAction.getShopwithoutId(shopID, new ActionCallbackDoubleListener<ShopBean, List<SortDataBean>>() {
                @Override
                public void onSuccess(ShopBean data01, List<SortDataBean> data02) {
                    Log.d("ShoppingServiceFragment", "传进来的是：shopID:" + shopID);
                    basePreference.putLong(Constants.SHOP_ID, shopID);
                    startActivity(new Intent(getActivity(), StorePageActivity.class));
                }

                @Override
                public void onFailure(String errorCode, String errorMessage) {
                    if ("998".equals(errorCode) && requestCount > 0 ){
                        requestCount--;
                        gotoShop();
                    }else if ("003".equals(errorCode)) {
                        ToastUtil.showShort("网络异常，请检查网络");
                    }else {
                        Toast.makeText(getActivity(), "异常代码：" + errorCode +
                                " 异常说明: " + errorMessage, Toast
                                .LENGTH_SHORT).show();
                        ToastUtil.showShort("加载失败，请重新点击加载");
                    }
                }
            });

        }

    }

    //07.选择服务地址
    private void chooseServiceAdd() {
        service_add.setEnabled(false);
        pvOptions.show();
//        KeyBoardUtil.getInstance(getActivity()).hide();  会导致程序崩溃
        /*参考文档：
        * http://blog.csdn.net/u011403718/article/details/51130640?locationNum=1
        * http://blog.csdn.net/yeah0126/article/details/51660429?locationNum=9
        * */


    }
    /*初始化城市列表的数据*/
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
    @SuppressLint("LongLogTag")
    private ArrayList<ProvinceModel> getDataList() {
        //List<ProvinceModel> provinceList = null;
//        AssetManager asset = getActivity().getAssets();
        try {
//            InputStream input = asset.open("preferences_city.json");
            /*
            *
            * http://www.tuicool.com/articles/6vUfEj
            * http://blog.csdn.net/randyjiawenjie/article/details/6589509
            * http://blog.sina.com.cn/s/blog_a000da9d010121bl.html
            *把 InputStream 转换成 String
            * */
/*            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            String json = buffer.toString();
            Gson GSON = new Gson();
            AreaList resourcelist = GSON.fromJson(json, AreaList.class);
            List<AreaList.DataBean> resourcelist01= resourcelist.getData();//从这里开始替换*/

            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(activity, "product");
            productBean = complexPreferences.getObject("ProductDetailsBean" , ProductDetailsBean.class);
//            Log.d("ShoppingServiceFragment01", "productBean:" + productBean);
            productID = productBean.getProductID();
//            Log.d("ShoppingServiceFragment02", "productID:" + productID);
            citylistData = complexPreferences.getListObject("productid" + productID + "CLList",
                    DetailsBean.DataBean.CLBean.class);
//            Log.d("ShoppingServiceFragment03", citylistData.toString());


            ArrayList<ProvinceModel> provinceList01 = new ArrayList<ProvinceModel>();
            //应该在解析的时候
            for (int i =0;i<citylistData.size();i++){
                List<DetailsBean.DataBean.CLBean.CityListBean> citylist01 =  citylistData.get(i).getCityList();
//                Log.d("ShoppingServiceFragment04", "citylist01:" + citylist01);
                List<CityModel> cityList = new ArrayList<CityModel>();
                for (int j=0;j<citylist01.size();j++){
                    List<DetailsBean.DataBean.CLBean.CityListBean.DistrictListBean> districtlist01 =   citylist01.get(j).getDistrictList();
//                    Log.d("ShoppingServiceFragment05", "districtlist01:" + districtlist01);
                    List<DistrictModel> districtList = new ArrayList<DistrictModel>();
                    for (int k =0;k<districtlist01.size();k++){
                        String DistrictID = districtlist01.get(k).getDistrictID();
                        String Name = districtlist01.get(k).getName();
                        String CityID = districtlist01.get(k).getCityID();
                        DistrictModel districtbean = new  DistrictModel(Name, DistrictID, CityID);
//                        Log.d("ShoppingServiceFragment06", "districtbean:" + districtbean);
                        districtList.add(districtbean);
                    }
//                    Log.d("ShoppingServiceFragment07", "districtList:" + districtList);
                    String CityID =citylist01.get(j).getCityID() ;
                    String Name =citylist01.get(j).getName();
                    String ProvinceID =citylist01.get(j).getProvinceID();
                    CityModel cityBean = new  CityModel(Name, districtList,CityID, ProvinceID);
//                    Log.d("ShoppingServiceFragment08", "cityBean:" + cityBean);
                    cityList.add(cityBean);
                }
//                Log.d("ShoppingServiceFragment09", "cityList:" + cityList);
                String ProvinceID = citylistData.get(i).getProvinceID();
                String Name = citylistData.get(i).getName() ;
                ProvinceModel provinceBean = new ProvinceModel(Name, cityList, ProvinceID);
//                Log.d("ShoppingServiceFragment10", "provinceBean:" + provinceBean);
                provinceList01.add(provinceBean);
            }
//            Log.d("ShoppingServiceFragment11", provinceList01.toString());
            return provinceList01;

        }catch(Exception e){
            e.printStackTrace();
        }finally{
        }
        return null;
    }

    //06.选择其他服务
    private void chooseOtherService() {

        shopAttradapter = new ProductShopAttrAdapter(getActivity(), shopAttrlistData);
        Holder holder = new GridHolder(shopAttrlistData.size());
        showShopAttrDialog(holder);
    }

    private void showShopAttrDialog(Holder holder) {
        shopAttrDialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setAdapter(shopAttradapter)
                .setCancelable(true)
                .setHeader(R.layout.item_fragment_product_header_sa)
                .setFooter(R.layout.item_fragment_product_footer)
                .setExpanded(false)
                .setOverlayBackgroundResource(com.orhanobut.dialogplus.R.color.dialogplus_black_overlay)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                    }
                })
                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialogPlus) {

                        if (basePreference.contains("shopAttrIDlist")) {
                            basePreference.removeItem("shopAttrIDlist");

                        }
                    }
                })
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("ShoppingServiceFragment", "position:" + position);

                        shopattrbean = (ShopAttrBean) shopAttradapter.getItem(position);
                        Log.d("ShoppingServiceFragment", "shopattrbean.isSelected():" + shopattrbean.isSelected());
                        if (shopattrbean.isSelected()) {
                            shopAttrIDlist.remove(shopattrbean);
                            shopattrbean.setIsSelected(false);
                            shopAttradapter.notifyDataSetChanged();

                        } else {
                            shopAttrID = shopattrbean.getShopAttrID();
                            shopAttrIDlist.add(shopattrbean);
                            shopattrbean.setIsSelected(true);
                            shopAttradapter.notifyDataSetChanged();
                        }
                    }
                })
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.close:
//                                Toast.makeText(application, "点击close", Toast.LENGTH_SHORT).show();
                                double sum = 0.0;

                                if (!shopAttrIDlist.isEmpty()) {
                                    for (int i = 0; i < shopAttrIDlist.size(); i++) {
                                        ShopAttrBean bean = (ShopAttrBean) shopAttrIDlist.get(i);
                                        sum = Double.parseDouble(bean.getPrice());
                                    }
                                    minPrice.setText("￥" + Double.toString(productBean.getMinPrice() + sum));
                                    basePreference.putListObject("shopAttrIDlist", shopAttrIDlist);//覆盖添加

                                } else {
                                    if (basePreference.contains("shopAttrIDlist")) {
                                        basePreference.removeItem("shopAttrIDlist");
                                    }
                                }
                                shopAttrDialog.dismiss();
                                break;
                        }
                    }
                })
                .create();
        shopAttrDialog.show();  //设置为不可滑动

    }
    //1.调用数据，不讲进度
/*        this.appAction.getProductSatisfySend(productID, new ActionCallbackListener<List<SatisfySendBean>>() {
            @Override
            public void onSuccess(List<SatisfySendBean> data) {
                if (data == null && data.isEmpty()) {
                    //赞无满即送活动，并且不可点击
                    full_to_discount.setClickable(false);//不可点击，说明文字变更
                    tv_full_to_discount.setText("暂无满即送活动");
                } else {
                    satisfySendlistData = data;
                    satisfySendAdapter = new ProductSatisfySendAdapter(getActivity(),satisfySendlistData);
                    showSatisfySendDialog();
                    full_to_discount.setClickable(true);
                    tv_full_to_discount.setText("请领取满即送获取优惠券");
                }

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                Toast.makeText(application, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });*/
    //05.满即送,这段逻辑，需要转移到初始化，有数据才会去调用并且更新最新数据，其实这也是一种实时更新的过程，其实是很有必要的
    private void getDiscount() {
        satisfySendAdapter = new ProductSatisfySendAdapter(getActivity(),satisfySendlistData);
        showSatisfySendDialog();

    }
    /*显示满即送对话框*/
    private void showSatisfySendDialog() {
        satisfySendDialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ListHolder())
                .setGravity(Gravity.BOTTOM)
                .setAdapter(satisfySendAdapter)
                .setCancelable(true)
                .setHeader(R.layout.item_fragment_product_header)
                .setFooter(R.layout.item_fragment_product_footer)
                .setExpanded(false)
                .setOverlayBackgroundResource(com.orhanobut.dialogplus.R.color.dialogplus_black_overlay)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                })
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        /**/
//                        Toast.makeText(application, "position:" + position, Toast.LENGTH_SHORT).show();


                    }
                })
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.close:
//                                Toast.makeText(application, "点击close", Toast.LENGTH_SHORT).show();
                                satisfySendDialog.dismiss();
                                break;
                        }
                    }
                })
                .create();
        satisfySendDialog.show();  //设置为不可滑动

    }

    //04.领取平台红包
    private void getHanbaoyiBonus() {

    }

    //03.领取店面红包
    private void getShopBonus() {

    }

    //02.领取优惠券，其他的先不用
    private void getCoupon() {
        voucherstemplateadapter = new ProductVouchersTemplateAdapter(getActivity(),vouchersTemplalistData);
        showVouchersTemplateDialog();
    }

    private void showVouchersTemplateDialog() {
        vouchersTemplateDialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ListHolder())
                .setGravity(Gravity.BOTTOM)
                .setAdapter(voucherstemplateadapter)
                .setCancelable(true)
                .setHeader(R.layout.item_fragment_product_header_vt)
                .setFooter(R.layout.item_fragment_product_footer)
                .setExpanded(false)
                .setOverlayBackgroundResource(com.orhanobut.dialogplus.R.color.dialogplus_black_overlay)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        /*点击当个item 是有反应的*/
//                        Toast.makeText(application, "position:" + position, Toast.LENGTH_SHORT).show();
                        bean = (VouchersTemplateBean) voucherstemplateadapter.getItem(position);
                        tempId = bean.getVouchersTemplateID();
                        switch (view.getId()) {
                            case R.id.tv_get_vt:
                                requestCount = 45;
                                receiveVouchers();
                                break;
                        }




                    }

                })
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.close:

                                vouchersTemplateDialog.dismiss();
                                break;

                        }
                    }
                })
                .create();
        vouchersTemplateDialog.show();  //设置为不可滑动

    }

    private void receiveVouchers() {
        this.appAction.ReceiveVouchers(tempId, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                //修改状态
                bean.setIsExist(false);
                voucherstemplateadapter.notifyDataSetChanged();

            }

            /*无论用户相关还是无关，统统添加*/
            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    receiveVouchers();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showShort("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                }

            }
        });
    }


    private void loginForToken() {

        basePreference.putBoolean(Constants.CHECK_FOR_RESULT,true);
        IntentUtil.gotoActivityForResult(getActivity(), LoginActivity.class, SELECTION);//跳转到登录界面
    }
    //01.分享
    /*
    * http://blog.csdn.net/u014748504/article/details/51039123?locationNum=4
    *
    * */
    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,productBean.getShareUrl());
        /*这个字段为空*/
//        Toast.makeText(getActivity(), productBean.getShareUrl(), Toast.LENGTH_SHORT).show();
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"请选择你的分享方式"));

    }

/*    OnShowCityChooserListener onShowCityChooserListener;
    public interface OnShowCityChooserListener{
        void onStartShow(boolean flag);
    }
    public void setOnShowCityChooserListener(OnShowCityChooserListener onShowCityChooserListener){
        this.onShowCityChooserListener = onShowCityChooserListener;
    }*/
}
