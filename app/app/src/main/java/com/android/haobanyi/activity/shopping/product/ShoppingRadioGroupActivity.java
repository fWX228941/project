package com.android.haobanyi.activity.shopping.product;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.home.SearchActivity;
import com.android.haobanyi.activity.shopping.shopping.ConformOrderActivity;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.activity.zxing.CaptureActivity;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackQuadrupleListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.android.haobanyi.util.ToastUtil.showShort;

/**
 * Created by fWX228941 on 2016/6/21.
 *
 * @作者: 付敏
 * @创建日期：2016/06/21
 * @邮箱：466566941@qq.com
 * @当前文件描述：产品服务界面
 */
public class ShoppingRadioGroupActivity extends BaseActivity {
    private static final String TAG = "fumin";
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.shop)
    LinearLayout shop;
    @BindView(R.id.collection)
    LinearLayout collection;
    @BindView(R.id.id_add_to_cart)
    TextView idAddToCart;
    @BindView(R.id.id_buy)
    TextView idBuy;
    @BindView(R.id.id_textView_back)
    ImageView idTextViewBack;
    @BindView(R.id.id_textView_add_shopping)
    ImageView idTextViewAddShopping;
    @BindView(R.id.id_textView_search_more)
    LinearLayout idTextViewSearchMore;
    @BindView(R.id.id_textView_add_shopping01)
    LinearLayout id_add_shopping01;
    @BindView(R.id.textview_home)
    TextView textviewHome;
    @BindView(R.id.textview_scan_code)
    TextView textviewScanCode;
    @BindView(R.id.textview_search)
    TextView textviewSearch;
    @BindView(R.id.id_home_memus_content)
    LinearLayout idHomeMemusContent; //下拉显示和隐藏
    @BindView(R.id.index_home_memus_content)
    LinearLayout indexHomeMemusContent;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.id_img_collect)
    ImageView idImgCollect;
    @BindView(R.id.id_text_collect)
    TextView idTextCollect;
    private String[] categoryArray;
    private boolean isNotDisplay = false;
    private boolean isNotCollected = false;//是否收藏
    private PathMeasure mPathMeasure;
    private long productID;
    private long shopID;
    private BadgeView badge;
    Bundle bundle = new Bundle();
    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];
    private int count;
    private static final int SELECTION = 1;//收藏


    /*
    * 用户相关，点击收藏，加入购物车，立即购买，客服，都要先判断是否已经登录，所以需要设置一个全局变量用于判断是否已经登录
    *
    *
    * */
    @Override
    protected int setLayout() {
        return R.layout.activity_shopping_radio_group;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        //productId得获取方便获取shopId
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
        productID = this.getIntent().getLongExtra(Constants.PRODUCT_ID,-1);
        shopID = this.getIntent().getLongExtra(Constants.SHOP_ID,-1);
        if (shopID ==-1){
            PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(this, "product");
            ProductDetailsBean productBean = complexPreferences01.getObject("productid" + productID, ProductDetailsBean.class);
            shopID = productBean.getShopID();
        }
        Log.d("ShoppingRadioGroupActiv", "productID:" + productID);
        bundle.putLong(Constants.PRODUCT_ID, productID);
        bundle.putLong(Constants.SHOP_ID, shopID);
        categoryArray = getResources().getStringArray(R.array.category_shopping_list);

        badge = new BadgeView(this, id_add_shopping01);
        badge.setBadgePosition(BadgeView.POSITION_CENTER);
        badge.setTextColor(Color.WHITE);
        badge.setBadgeBackgroundColor(Color.RED);
        badge.setText("0");

        //检查是否登录成功
        Log.d(TAG, "isLogin:" + isLogin);

    }



    @Override
    protected void initViews() {
        initViewPager();
        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(this, "product");
        ProductDetailsBean productBean = complexPreferences01.getObject("productid" + productID, ProductDetailsBean.class);
        isNotCollected = productBean.isFav();
        initCollectionState();



    }


    private void initCollectionState() {
        if (isLogin){
            if (isNotCollected) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.collection_success);
                idImgCollect.setImageBitmap(bitmap);
                idTextCollect.setText("已收藏");

            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.collection);
                idImgCollect.setImageBitmap(bitmap);
                idTextCollect.setText("收藏");
            }
        }else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.collection);
            idImgCollect.setImageBitmap(bitmap);
            idTextCollect.setText("收藏");
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
    /*
    * 参考设计：https://github.com/ogaclejapan/SmartTabLayout
    * 携带不同的数据activity 传递给fragment，实现两者之间的通信
    *
    * */
    private void initViewPager() {
        //FragmentPagerItems pages = new FragmentPagerItems(this);
        //添加不同的fragment
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(categoryArray[0], ShoppingServiceFragment.class,bundle)
                .add(categoryArray[1], ShoppingDetailsFragment.class,bundle)
                .add(categoryArray[2], ShoppingEvaluatationFragment.class)
                .create());

        viewpager.setAdapter(adapter);// 左右滑动页面关联fragment适配器
        //02.设置ViewPager缓存个数，意味这个多次调用onViewCreated方法
//        viewpager.setOffscreenPageLimit(adapter.getCount());
        viewpager.setOffscreenPageLimit(1);  //不缓存看看是什么样子
        //03.关联smartTabLayout和ViewPager
        smartTabLayout.setViewPager(viewpager);
        if(progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }


    @OnClick({R.id.shop, R.id.collection, R.id.id_add_to_cart, R.id.id_buy, R.id.id_textView_back, R.id
            .id_textView_add_shopping, R.id.id_textView_search_more, R.id.textview_home, R.id.textview_scan_code, R
            .id.textview_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shop:
                requestCount = 45;
                gotoShop();//跳转到店铺详情
                break;
            case R.id.collection:
                collect();//收藏
                break;
            case R.id.id_add_to_cart:
                addToCart();//加入购物车
                break;
            case R.id.id_buy:
                buy();//立即购买
                break;
            case R.id.id_textView_back:
                goBack();//后退
                break;
            case R.id.id_textView_add_shopping:
                gotoShopping();//跳转到购物车
                break;
            case R.id.id_textView_search_more:
                pullList();//下拉菜单
                break;
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

        IntentUtil.gotoActivityWithoutData(ShoppingRadioGroupActivity.this, SearchActivity.class, false);

    }

    private void gotoScanCode() {
        IntentUtil.gotoActivityWithoutData(ShoppingRadioGroupActivity.this, CaptureActivity.class, false);

    }

    private void gotoHomePage() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.STATE_FROM_SHOPPING, 02);
        IntentUtil.gotoTopActivityWithData(ShoppingRadioGroupActivity.this, RadioGroupActivity.class, bundle, true);

    }

    private void pullList() {
        if (isNotDisplay) {
            indexHomeMemusContent.setVisibility(View.GONE);
        } else {
            indexHomeMemusContent.setVisibility(View.VISIBLE);
        }
        isNotDisplay = !isNotDisplay;

    }

    private void gotoShopping() {

        if (isLogin){
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.STATE_FROM_SHOPPING, 01);
            /*
            * 标签页全部刷新：
            *            IntentUtil.gotoTopActivityWithData(ShoppingRadioGroupActivity.this, RadioGroupActivity.class, bundle, true);//这个是全部刷新，如果只是想实现部分刷新呢
            *            因为会把activity 给销毁掉
            * 标签页部分刷新：
            * */
            IntentUtil.gotoTopActivityWithData(ShoppingRadioGroupActivity.this, RadioGroupActivity.class, bundle, true);//这个是全部刷新，如果只是想实现部分刷新呢
        } else {
            loginForToken();
        }



    }

    private void goBack() {
        removeResouce();
        finish();
        ShoppingRadioGroupActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);

    }


    private void buy() {

        long districtid  = Long.parseLong(basePreference.getString("DistrictID_ADD","-1"));
        if (districtid==-1){
            ToastUtil.showHintMessage("请先选择服务地址");
            return;
        }
        List<Long> AttributeIdList = new ArrayList<Long>();
        if (basePreference.contains("shopAttrIDlist")) {
            List<ShopAttrBean> shopAttrIDlist = basePreference.getListObject("shopAttrIDlist",ShopAttrBean.class);
            for (int i = 0;i<shopAttrIDlist.size();i++){
                AttributeIdList.add(shopAttrIDlist.get(i).getShopAttrID());
            }
        }


        if (isLogin){
        /*
        * 01.调用接口
        * 02.跳转到购确认订单界面
        *
        * */

            if (shopID!=-1 && productID!=-1){
                requestCount = 45;
                addCartItemOfBuyNow(districtid, AttributeIdList);

            }

        } else {
            loginForToken();
        }

    }

    private void addCartItemOfBuyNow(final long districtid, final List<Long> attributeIdList) {
        this.appAction.addCartItemOfBuyNow(shopID, productID, 0, count + 1, districtid, attributeIdList, new
                ActionCallbackListener<ResponseCode>() {
                    @Override
                    public void onSuccess(ResponseCode data) {
                        ToastUtil.showSuccessfulMessage(data.getMessage());
                        IntentUtil.gotoActivityWithoutData(ShoppingRadioGroupActivity.this, ConformOrderActivity
                                .class, false);
                        removeResouce();
/*                        complexPreferences.removeItem("DistrictID_ADD");
                        complexPreferences.removeItem("shopAttrIDlist");*/
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode) && requestCount > 0 ){
                            requestCount--;
                            addCartItemOfBuyNow(districtid, attributeIdList);
                        }else if ("996".equals(errorCode)){
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            loginForToken();
                        }else if ("003".equals(errorCode)) {
                            ToastUtil.networkUnavailable();

                        }else {
                            ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);

                        }

                    }
                });
    }

    /*
    * 1.添加动画效果
    * 2.与服务器交互
    * 3.加入购物车之后这些
    *  类型只要不是差太多都是可以得
    *
    *  4.还有一个是已加入购物车的场景
    *
    * */
    private void addToCart() {
        isLogin = basePreference.getBoolean(Constants.ISLOGIN, false);
        Log.d("AA", "isLogin:" + isLogin);

        long districtid  = Long.parseLong(basePreference.getString("DistrictID_ADD","-1"));
        if (districtid==-1){
            ToastUtil.showLong("请先选择服务地址");
            return;
        }
        List<Long> AttributeIdList = new ArrayList<Long>();//一定要初始化
        if (basePreference.contains("shopAttrIDlist")) {
            List<ShopAttrBean> shopAttrIDlist = basePreference.getListObject("shopAttrIDlist",ShopAttrBean.class);
            for (int i = 0;i<shopAttrIDlist.size();i++){
                AttributeIdList.add(shopAttrIDlist.get(i).getShopAttrID());
            }
        }





        if (isLogin){
            if (shopID!=-1 && productID!=-1){

                /*
                * 暂时先不考虑套餐服务还有其他服务,即 DistrictID
                * */
                requestCount = 45;
                addCartItem(districtid, AttributeIdList);

            }

        } else {
            loginForToken();
        }


    }

    private void addCartItem(final long districtid, final List<Long> attributeIdList) {
        this.appAction.addCartItem(shopID, productID, 0, count + 1, districtid, attributeIdList, new
                ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());

                //使界面恢复到初始状态
                onRefreshListener.onRefresh();//安插一个监听代理的对象
                removeResouce();
                basePreference.putBoolean("is_refresh_shoppingPage", true);
                addAmitation();

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0 ){
                    requestCount--;
                    addCartItem( districtid,  attributeIdList);
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);
                }
            }});
    }

    private void addAmitation() {
        //01。添加动画效果
        //一。创建出执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里
        final ImageView goods = new ImageView(ShoppingRadioGroupActivity.this);
        ViewGroup.LayoutParams para = goods.getLayoutParams();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shop_car);
        goods.setImageBitmap(bitmap);
/*        goods.setAdjustViewBounds(true);
        goods.setMaxHeight(5);
        goods.setMaxWidth(5);*/
        // goods.setImageResource(R.drawable.shop_car);//先20dp 设置图片大小

        //在购物车控件上绑定计数器
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        rl.addView(goods, params);
        //二。计算动漫开始点和结算点的坐标，分为三步：得到父布局的起始点坐标，商品图片的坐标，购物车的坐标
        int[] parentLocation = new int[2];
        rl.getLocationInWindow(parentLocation);

        int startLoc[] = new int[2];
        idAddToCart.getLocationInWindow(startLoc);

        int endLoc[] = new int[2];
        idTextViewAddShopping.getLocationInWindow(endLoc);

        //三。计算动画开始/结算的坐标 【这个地方是可以自控制的】起点：商品起始点-父布局起始点+该商品图片的一半  / 终点：购物车起始点-父布局起始点+购物车图片的1/5
        float startX = startLoc[0] - parentLocation[0] + idAddToCart.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + idAddToCart.getHeight() / 2;
        float toX = endLoc[0] - parentLocation[0] + idTextViewAddShopping.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        //四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
        //五、 开始执行动画
        valueAnimator.start();

        // 六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
                /*   i++;
                count.setText(String.valueOf(i));*/
                // 把移动的图片imageview从父布局里移除
                rl.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        /*  02.购物车数量+1，为其添加红色计数，本质就是一个textView，包括背景和动漫都是可以定制的
        *   用法：
        *   View target = findViewById(R.id.target_view);
            BadgeView badge = new BadgeView(this, target);
            badge.setText("1");
            badge.show();
        *   参考文档：https://github.com/jgilfelt/android-viewbadger
        * */
        count++;
        if (count ==0){
            badge.hide();
        } else {
            badge.increment(1);
            badge.toggle(true);
        }
    }

    /*
    *
    * 这里涉及到一个场景就是假设我是收藏状态，但是我是在没有登录的情况下，回来后重新刷新界面，重新设置值。这个也是一个问题，放在后面处理先
    * 这里的收藏是只服务收藏
    * */
    private void collect() {
        if (isLogin){
            if (isNotCollected) {
                requestCount = 45;
                deleteFavProduct();
            } else {
                requestCount = 45;
                favProduct();
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

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT,true);
        IntentUtil.gotoActivityForResult(this, LoginActivity.class, SELECTION);//跳转到登录界面
    }

    private void deleteFavProduct() {
        this.appAction.deleteFavProduct(productID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("取消成功");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.collection);
/*            idImgCollect.setAdjustViewBounds(true);
    idImgCollect.setMaxHeight(20);
    idImgCollect.setMaxWidth(20);*/
                idImgCollect.setImageBitmap(bitmap);
                idTextCollect.setText("收藏");
                basePreference.putBoolean("is_refresh_collectionPage", true);
                basePreference.putBoolean("is_refresh_minePage", true);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0 ){
                    requestCount--;
                    deleteFavProduct();
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);
                }
            }
        });
    }

    /*收藏服务*/
    private void favProduct() {
        this.appAction.favProduct(productID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("收藏成功");
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.collection_success);
                idImgCollect.setImageBitmap(bitmap);
                idTextCollect.setText("已收藏");
                basePreference.putBoolean("is_refresh_collectionPage", true);
                basePreference.putBoolean("is_refresh_minePage", true);

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0 ){
                    requestCount--;
                    favProduct();
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);
                }

            }
        });
    }

    private void gotoShop() {
        removeResouce();
        if (shopID!= -1){
            this.appAction.getShopwithoutId(shopID, new ActionCallbackDoubleListener<ShopBean,List<SortDataBean>>() {

                @Override
                public void onSuccess(ShopBean data, List<SortDataBean> data02) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.SHOP_ID, data.getShopID());
                    bundle.putInt(Constants.STATE_FROM_SRGA, 01);
                    IntentUtil.gotoActivityWithData(ShoppingRadioGroupActivity.this, StorePageActivity.class, bundle, false);
                }

                @Override
                public void onFailure(String errorCode, String errorMessage) {
                    if ("998".equals(errorCode) && requestCount > 0 ){
                        requestCount--;
                        gotoShop();
                    }else if ("003".equals(errorCode)) {
                        ToastUtil.networkUnavailable();
                    }else {
                        ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);
                    }
                }
            });

        } else {
            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(this, "product");
            ProductDetailsBean productBean = complexPreferences.getObject("productid" + productID, ProductDetailsBean.class);
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.SHOP_ID,  productBean.getShopID());
            bundle.putInt(Constants.STATE_FROM_SRGA, 01);
            IntentUtil.gotoActivityWithData(ShoppingRadioGroupActivity.this, StorePageActivity.class, bundle,false);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECTION:
                isLogin = true;
                requestCount = 45;
                getProdut();
            break;
        }

    }

    private void getProdut() {
        this.appAction.getProduct01(productID, new ActionCallbackFivefoldListener<ProductDetailsBean,List<SatisfySendBean>,List<VouchersTemplateBean>,List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>>() {
            @Override
            public void onSuccess(ProductDetailsBean data, List<SatisfySendBean> data01, List<VouchersTemplateBean> data02, List<ShopAttrBean> data03, List<DetailsBean.DataBean.CLBean> data05) {
                isNotCollected = data.isFav();
                initCollectionState();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0 ){
                    requestCount--;
                    getProdut();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showHintMessage("加载失败，请重新点击加载",errorCode,errorMessage);
                }

            }
        });
    }
/*监听器应该在哪里工厂中定义，并在工厂中触发*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (onCitySelectedBackListener.onCitySelectedBack()){
                return true;
            }
        }
        //清理文件
        removeResouce();

        return super.onKeyDown(keyCode, event);
    }

    private void removeResouce() {

        if (basePreference.contains("shopAttrIDlist")) {
            basePreference.removeItem("shopAttrIDlist");

        }
        if (basePreference.contains("DistrictID_ADD")) {
            basePreference.removeItem("DistrictID_ADD");

        }
    }

    OnCitySelectedBackListener onCitySelectedBackListener;
    public interface OnCitySelectedBackListener{
        //可以把activity中的任意数据传递
        boolean onCitySelectedBack();
    }
    /*对fragment 提供一个注册方法,而这个activity和fragment之间又可以相互拿到实例，持有对象*/
    public void setOnCitySelectedBackListener(OnCitySelectedBackListener onCitySelectedBackListener){
        this.onCitySelectedBackListener = onCitySelectedBackListener;
    }


    OnRefreshListener onRefreshListener;
    public interface OnRefreshListener{
        //可以把activity中的任意数据传递
        void onRefresh();
    }
    /*对fragment 提供一个注册方法,而这个activity和fragment之间又可以相互拿到实例，持有对象*/
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }


}
