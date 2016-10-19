package com.android.haobanyi.activity.shopping.store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.home.SearchActivity;
import com.android.haobanyi.activity.zxing.CaptureActivity;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.bumptech.glide.Glide;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/6/20.
 *
 * @作者: 付敏
 * @创建日期：2016/06/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：商家店铺首页外框架
 */
public class StorePageActivity extends BaseActivity {
    private static final String TAG = "fumin";
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.id_textView_back)
    ImageView idTextViewBack;
    @BindView(R.id.id_editText_search)
    TextView idEditTextSearch;
    @BindView(R.id.id_textView_search_more01)
    ImageView idTextViewSearchMore01;
    @BindView(R.id.id_textView_search_more)
    LinearLayout idTextViewSearchMore;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.shop_logo)
    ImageView shopLogo;
    @BindView(R.id.shop_name)
    TextView shopName;
    @BindView(R.id.shop_mainservice)
    TextView shopMainservice;
    @BindView(R.id.tv_isfav)
    TextView tvIsfav;
    @BindView(R.id.index_home_memus_content)
    LinearLayout idHomeMemusContent;
    private long shopID;
    Bundle bundle = new Bundle();
    private String[] categoryArray; //测试数据
    private int STATE_FROMSRGA;
    private boolean isNotDisplay = false;
    private boolean isNotCollected = false;//是否收藏
    private static final int SELECTION = 1;//收藏
    private static final int LOGIN_TO_SPV = 3;

    @Override
    protected int setLayout() {
        return R.layout.view_search;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        //shopID = this.getIntent().getLongExtra(Constants.SHOP_ID, -1); 这个有问题,用框架替换  fragment到activity ，还有一个是activity到fragment的
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
        STATE_FROMSRGA = this.getIntent().getIntExtra(Constants.STATE_FROM_SRGA, 00);
        Log.d(TAG, "STATE_FROMSRGA:" + STATE_FROMSRGA);
        if (STATE_FROMSRGA == 00){
            shopID = basePreference.getLong(Constants.SHOP_ID, -1l);
            bundle.putLong(Constants.SHOP_ID, shopID);
            Log.d("ShoppingServiceFragment", "从fragment中传递过来的shopID:" + shopID);
        } else  {
            shopID = this.getIntent().getLongExtra(Constants.SHOP_ID, -1);
            bundle.putLong(Constants.SHOP_ID, shopID);
            Log.d("ShoppingServiceFragment", "从activ中传递过来的shopID:" + shopID);
        }

        Log.d("ShoppingServiceFragment", "这里变化了shopID:" + shopID);
        //01.获取分类字符串
        categoryArray = getResources().getStringArray(R.array.category_shop_list);

        //检查是否登录成功
        isLogin = basePreference.getBoolean(Constants.ISLOGIN,false);
        Log.d(TAG, "isLogin:" + isLogin);

    }

    @Override
    protected void initViews() {
        if (shopID!=-1){
            //初始化
            initHeaderView();
            //初始化轮播页面
            initViewPager();
        }


    }

    private void initHeaderView() {

            PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(context,
                    "shop");
            ShopBean shopBean = complexPreferences01.getObject("shopid" + shopID, ShopBean.class);
            loadView(shopBean);

    }

    private void loadView(ShopBean shopBean) {
        if (null!=shopBean){
            if (shopBean.getDistrict()==null){
                tvAdd.setText("地址："+shopBean.getShopAdd());
            } else {
                tvAdd.setText("地址："+shopBean.getDistrict());
            }

            shopName.setText(shopBean.getShopName());
            if (shopBean.getMainService()!=null){
                shopMainservice.setText("服务范围："+shopBean.getMainService());
            }else {
                shopMainservice.setText("服务范围：暂时没有简介");
            }

            Glide.with(this)
                    .load(shopBean.getShopLogo())
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.drawable.logo)// 也就是网络加载失败时出现的图片
                    .into(shopLogo);
            isNotCollected = shopBean.isFav();
            initCollection();
        }



    }

    private void initCollection() {
        if (isLogin){
            if (isNotCollected) {
                tvIsfav.setText("已收藏");
                tvIsfav.setTextColor(context.getResources().getColor(R.color.font_orange));
                tvIsfav.setBackgroundResource(R.color.tittle_view_background_white_);

            } else {
                tvIsfav.setText("收藏");
            }
        }else {
            tvIsfav.setText("收藏");
        }
    }

    private void initViewPager() {
        Log.d("ShoppingServiceFragment", "bunle中shopID:" + shopID);
        Log.d("ShoppingServiceFragment", "bundle:" + bundle);
        Log.d("ShoppingServiceFragment", "bundle.getLong(Constants.SHOP_ID):" + bundle.getLong(Constants.SHOP_ID));

        /*奇葩问题真的很多，bundle 又是怎么回事呢？*/
        /*
        * 这个bug我就特别不能理解了：  也是醉了。。只能曲线救国了，通信还是得用框架才是
07-26 11:36:51.409 3247-3247/? D/ShoppingServiceFragment: 从fragment中传递过来的shopID:6
07-26 11:36:51.409 3247-3247/? D/ShoppingServiceFragment: 这里变化了shopID:6
07-26 11:36:51.410 3247-3247/? D/ShoppingServiceFragment: bunle中shopID:6
07-26 11:36:51.410 3247-3247/? D/ShoppingServiceFragment: bundle:Bundle[{shop_id=6}]
07-26 11:36:51.410 3247-3247/? D/ShoppingServiceFragment: bundle.getLong(Constants.SHOP_ID):6
07-26 11:36:51.425 3247-3247/? D/ShoppingServiceFragment: 在storeFrontPageshopId:-1
07-26 11:36:51.427 3247-3247/? D/ShoppingServiceFragment: 在storeFrontPageshopId:-1

07-26 11:37:15.372 3247-3247/? D/ShoppingServiceFragment: 从activ中传递过来的shopID:6
07-26 11:37:15.372 3247-3247/? D/ShoppingServiceFragment: 这里变化了shopID:6
07-26 11:37:15.373 3247-3247/? D/ShoppingServiceFragment: bunle中shopID:6
07-26 11:37:15.373 3247-3247/? D/ShoppingServiceFragment: bundle:Bundle[{shop_id=6}]
07-26 11:37:15.373 3247-3247/? D/ShoppingServiceFragment: bundle.getLong(Constants.SHOP_ID):6
07-26 11:37:15.393 3247-3247/? D/ShoppingServiceFragment: 在storeFrontPageshopId:6
07-26 11:37:15.398 3247-3247/? D/ShoppingServiceFragment: 在storeFrontPageshopId:6


        *
        * */
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(categoryArray[0], StoreFrontPageFragment.class, bundle)
                .add(categoryArray[1], StoreServiceListFragment.class, bundle)
                .add(categoryArray[2], StoreActivityFragment.class, bundle)
                .add(categoryArray[3], StoreVoucherFragment.class, bundle)
                .create());

        viewpager.setAdapter(adapter);// 左右滑动页面关联fragment适配器
        //02.设置ViewPager缓存个数，意味这个多次调用onViewCreated方法
        viewpager.setOffscreenPageLimit(1);//adapter.getCount()
        //03.关联smartTabLayout和ViewPager
        smartTabLayout.setViewPager(viewpager);
        if(progressDialog.isShowing()){
            progressDialog.cancel();
        }
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

    //,R.id.tv_isfav  有一个奇葩问题就是收藏的
    @OnClick({R.id.id_textView_back, R.id.id_editText_search, R.id.id_textView_search_more, R.id.textview_home, R.id.textview_scan_code, R
            .id.textview_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_textView_back:
                goBack();//后退
                break;
            case R.id.id_editText_search:
                gotoSearch();
                break;
            case R.id.id_textView_search_more:
                pullList();//下拉菜单
                break;
      /*      case R.id.tv_isfav:
                collect();*/
            case R.id.textview_home:
                gotoHomePage();
                break;
            case R.id.textview_scan_code:
                gotoScanCode();
                break;
            case R.id.textview_search:
                gotoSearching();
                break;
        }
    }

    private void gotoSearching() {

        IntentUtil.gotoActivityWithoutData(this, SearchActivity.class, false);

    }

    private void gotoScanCode() {
        IntentUtil.gotoActivityWithoutData(this, CaptureActivity.class, false);

    }

    private void gotoHomePage() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.STATE_FROM_SHOPPING, 02);
        IntentUtil.gotoTopActivityWithData(this, RadioGroupActivity.class, bundle, true);

    }
    /*
    * java.lang.IllegalStateException: Could not find a method collect(View) in the activity class com.android.haobanyi.activity.shopping.store.StorePageActivity for onClick handler on view class android.widget.TextView with id 'tv_isfav'
    * 必须添加 View
    *https://my.oschina.net/u/1014520/blog/194781
    *  方法必须是public 方法签名也要一致，同时 必须添加参数View
    *  年度奇葩问题 关于butterknife 的，难道是有顺序
    *
    * */
    public void collect(View view) {
        Log.d(TAG, "触发收藏");
        if (isLogin){
            if (isNotCollected) {
                requestCount = 45;
                deletefavShop();
            } else {
                requestCount = 45;
                favShop();
            }
            isNotCollected = !isNotCollected;
        } else {
            /*
            * 应用场景：当期检测到用户没有登录时，就跳转到登录界面，待登录界面登陆成功后，返回到响应的启动界面
            * 参考文档：http://blog.csdn.net/a910626/article/details/51443233
            *          http://blog.csdn.net/Nio96/article/details/47665471
            * 回传值：http://www.jb51.net/article/75597.htm
            * */

            loginForToken();
        }
    }
    //请求失效就一直请求了
    private void deletefavShop() {
        this.appAction.deleteFavShop(shopID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("取消成功");
                Log.d(TAG, "1");
                tvIsfav.setText("收藏");
                tvIsfav.setTextColor(context.getResources().getColor(R.color.white));
                tvIsfav.setBackgroundResource(R.color.cellect_orange);
                basePreference.putBoolean("is_refresh_minePage", true);
            }
            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    deletefavShop();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);
                }

            }
        });

    }

    private void favShop() {
        this.appAction.favShop(shopID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("收藏成功");
                tvIsfav.setText("已收藏");
                Log.d(TAG, "2");
                tvIsfav.setTextColor(context.getResources().getColor(R.color.font_orange));
                tvIsfav.setBackgroundResource(R.color.tittle_view_background_white_);
                basePreference.putBoolean("is_refresh_minePage", true);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    favShop();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);

                }

            }
        });


    }
    private void loginForToken() {
/*        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(StorePageActivity.this, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面*/
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT,true);
        IntentUtil.gotoActivityForResult(StorePageActivity.this, LoginActivity.class, SELECTION);//跳转到登录界面
    }

    private void gotoSearch() {
        IntentUtil.gotoActivityWithoutData(StorePageActivity.this, SearchActivity.class, false);
    }

    private void pullList() {
        if (isNotDisplay) {
            idHomeMemusContent.setVisibility(View.GONE);
        } else {
            idHomeMemusContent.setVisibility(View.VISIBLE);
        }
        isNotDisplay = !isNotDisplay;
    }

    private void goBack() {
        finish();
        StorePageActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
    }
    /*这个反而给了我一个启示*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECTION:
                isLogin = true;
                requestCount = 45;//重新获取店铺信息
                gotoShop();
                break;
        }
    }

    private void gotoShop() {
        this.appAction.getShopwithoutId(shopID, new ActionCallbackDoubleListener<ShopBean,List<SortDataBean>>() {

            @Override
            public void onSuccess(ShopBean data, List<SortDataBean> data02) {
                isNotCollected = data.isFav();
                initCollection();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0 ){
                    requestCount--;
                    gotoShop();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                }
            }
        });
    }
}
