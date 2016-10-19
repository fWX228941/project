package com.android.haobanyi.activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.collection.CollectionPageView;
import com.android.haobanyi.activity.home.CitySelecterActivity;
import com.android.haobanyi.activity.home.FrontPageView;
import com.android.haobanyi.activity.home.SearchActivity;
import com.android.haobanyi.activity.mine.MinePageView;
import com.android.haobanyi.activity.mine.order.MyOrderListActivity;
import com.android.haobanyi.activity.searching.ShopListActivity;
import com.android.haobanyi.activity.shopping.ShoppingPageView;
import com.android.haobanyi.activity.zxing.CaptureActivity;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/3/30.
 *
 * @作者: 付敏
 * @创建日期：2016/03/30
 * @邮箱：466566941@qq.com
 * @当前文件描述：容器主页，分别动态加载3大模块，分别是首页模块；购物车模块；我的模块
 */
public class RadioGroupActivity extends BaseActivity {
    @BindView(R.id.radio_home_page)
    RadioButton radio_homepage; /*首页按钮*/
    @BindView(R.id.radio_shopping)
    RadioButton radio_Shopping;//购物车
    @BindView(R.id.radio_mine)
    RadioButton radioMine;
    @BindView(R.id.group_tab)
    RadioGroup group_tab;/*底部导航包裹容器*/
    @BindView(R.id.linear_content)
    LinearLayout linear_content;/*中间包裹容器*/
    @BindView(R.id.radio_collection)
    RadioButton radioCollection;// 收藏
    @BindView(R.id.home_city_from)
    TextView city;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.imageView_pull_list)
    ImageView imageViewPullList;
/*    @BindView(R.id.index_home_tips_arrow)
    ImageView tips_arrow;//下拉箭头图标*/
    @BindView(R.id.head)
    LinearLayout head;
    @BindView(R.id.textview_scan_code)
    TextView textviewScanCode;
    @BindView(R.id.textview_center)
    TextView textviewCenter;
    @BindView(R.id.index_home_memus_content)
    LinearLayout tips_content;
    @BindView(R.id.textview_store)
    TextView textviewStore;
    @BindView(R.id.id_front_page_head)
    LinearLayout FrontPageHead;
    /*4.是否点击下拉选项卡,分别是下拉内容列表和下拉箭头小图标和大图标*/
    private static final String TAG = "minge";
    private boolean isNotDisplay = false;
    /*1.首页视图*/
    private FrontPageView frontpage;
    /*2.购物车*/
    private ShoppingPageView shoppingpage;
    /*3.我的*/
    private MinePageView minepageview;
    /*4.收藏*/
    private CollectionPageView collectionpageview;
    private boolean isFirstLoad = true;
    private int STATE_FROM_RGA = 00;

    private static final int SELECT_PHOTO = 0;// 选择一张照片时用到
    private static final int SELECT_CAMERO = 1;// 拍照片时用到
    private static final int SELECT_CITY = 2;//选择城市
    private static final int LOGIN_TO_SPV = 3;//登录成功后，跳转到购物车页面的标识量
    private String filePath;
    private Bitmap tmpBitmap;
    private String url = "";
    private static boolean flag =true;//退出标志位

    private boolean isNeedToRefresh = false;//是否有必要刷新，文件内容的读和写
    private boolean isHasPopWindws =false; //购物车是否有弹框
    private int testCount =1;
    private boolean isOnlyProOnce = true;

    @Override
    protected int setLayout() {
        return R.layout.activity_home_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        STATE_FROM_RGA = this.getIntent().getIntExtra(Constants.STATE_FROM_SHOPPING, 00);
        //1）预启动地图定位服务
        //IntentUtil.gotoServiceWtthAction(Constants.ACTION_ENABLE_LOACTION, context, LocationService.class);
        // 2）设置当前手机所在的城市

        String chooseCity = this.getIntent().getStringExtra(Constants.district);
        if (!TextUtils.isEmpty(chooseCity)){
            city.setText(chooseCity);
        } else {
            city.setText(PreferenceUtil.getSharedPreference(context, Constants.district, "南山区", "preferences"));
        }

        /*假定都是在有网络的情况下进行的*/
        //  getDataFromServer();
    }


    @Override
    protected void initViews() {
        NTPTime.getInstance().getCurrentTime();
        //1）如果是从其他地方跳转到购物车
        Log.d(TAG, "STATE_FROM_RGA:" + STATE_FROM_RGA);
        switch (STATE_FROM_RGA) {
            //部分更新
            case 01:
                radio_Shopping.setChecked(true);
                enterToShoppingPage(true);
                break;
            case 02:
                //2)动态设置为选中，动态方法和静态属性谁的优先级更高
                radio_homepage.setChecked(true);
                //3)随着系统进入一起设置加载
                enterToFrontPage(true);
                break;
            case 03:
                //2)动态设置为选中，动态方法和静态属性谁的优先级更高
                radioMine.setChecked(true);
                //3)随着系统进入一起设置加载
                enterToMinePage(true);
                break;
            case 04:
                //2)动态设置为选中，动态方法和静态属性谁的优先级更高
                radioCollection.setChecked(true);
                //3)随着系统进入一起设置加载
                enterToCollectionPage(true);
                break;
            default:
                radio_homepage.setChecked(true);
                //3)随着系统进入一起设置加载
                enterToFrontPage(true);
                break;

        }



    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void registerEventListener() {
        /*1）注册-监听*/
        group_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {//两个参数分别代表容器和选中选ID
                /*
                * 2）是否添加一个选中逻辑判断，其实是不需要添加，我每次点击一次都会重新绘制一次界面，而当我仿佛点击是不会返回进入
                * 绘制接口，因为这个监听方法已经封装了一层是否选中的判断。这里是否需要考虑添加复用性，或者记录用户的操作，实在
                * 上是存在记录的操作，一个视图判空的逻辑。
                * 3)radioGroup.getCheckedRadioButtonId(),
                * */
                switch (checkedId) {
                    //首页
                    case R.id.radio_home_page:
                        Log.d("fumin", 01 + "");
                        flag =true;
                        enterToFrontPage(false);
                        break;
                    //购物车
                    case R.id.radio_shopping:
                        flag =true;
                        enterToShoppingPage(false);
                        break;

                    //收藏
                    case R.id.radio_collection:
                        Log.d("fumin", 03 + "");
                        flag =true;
                        enterToCollectionPage(false);
                        break;
                    //我的
                    case R.id.radio_mine:
                        Log.d("fumin", 04 + "");
                        flag =true;
                        enterToMinePage(false);
                        break;
                }

            }
        });

    }

    @Override
    protected void registerBroadCastReceiver() {
    }

    @Override
    protected void saveState(Bundle outState) {

    }


    /*
    * 1.跳转到我的页面
    * 2.步骤：
    *     1）调用ViewGroup.removeAllViews() ：容器类移除子视图to remove all child views from the ViewGroup.清除视图
    *     2）在容器视图中添加复杂视图ViewGroup.addView(View child)
    * */
    private void enterToMinePage(boolean refresh) {
        FrontPageHead.setVisibility(View.GONE);
        linear_content.removeAllViews();
        if (null == minepageview) {
            minepageview = new MinePageView(this);
        } else {
            if (refresh){
                minepageview = new MinePageView(this);
            }else {
                isNeedToRefresh = basePreference.getBoolean("is_refresh_minePage", false);
                Log.d(TAG, "isNeedToRefresh01:" + isNeedToRefresh);
                if (isNeedToRefresh){
                    basePreference.putBoolean("is_refresh_minePage", false);
                    Log.d(TAG, "isNeedToRefresh02:" + basePreference.getBoolean("is_refresh_minePage", false));//减少实例的创建
                    minepageview = new MinePageView(this);
                }
            }
        }

        linear_content.addView(minepageview);
    }

    //2.跳转到首页
    private void enterToFrontPage(boolean refresh) {
        FrontPageHead.setVisibility(View.VISIBLE);
        linear_content.removeAllViews();
        Log.d(TAG, "frontpage:" + frontpage);
        if (null == frontpage) {
            Log.d(TAG, "刷新了" );
            frontpage = new FrontPageView(this);
            /*一定要在实例化后才能监听*///head.setBackgroundResource(R.color.background_black_pay);
/*            frontpage.setOnHeaderColorChangedListener(new FrontPageView.OnHeaderColorChangedListener() {
                @Override
                public void onHeaderColorChanged(boolean isTop) {
                    if (isTop){
                        head.setBackgroundResource(R.color.transparent);
                    } else {
                        head.setBackgroundResource(R.color.background_black_pay);
                    }

                }
            });*/
        }else {
            if (refresh){
                frontpage = new FrontPageView(this);// 这个地方是不会被执行到的，就统一刷新吧！
            }
        }


        frontpage.setOnHeaderColorChangedListener(new FrontPageView.OnHeaderColorChangedListener() {
            @Override
            public void onHeaderColorChanged(boolean isTop) {
                if (isTop){
                    head.setBackgroundResource(R.color.transparent);
                } else {
                    head.setBackgroundResource(R.color.background_black_pay);
                }

            }
        });
        linear_content.addView(frontpage);
    }

    private void enterToShoppingPage(boolean refresh) {
        FrontPageHead.setVisibility(View.GONE);
        linear_content.removeAllViews();

        if (null == shoppingpage) {
            shoppingpage = new ShoppingPageView(this);//关键还是在这里，我就知道的，实现部分刷新
        }else {
            if (refresh){
                shoppingpage = new ShoppingPageView(this);
            }else {
                isNeedToRefresh = basePreference.getBoolean("is_refresh_shoppingPage", false);
                Log.d(TAG, "isNeedToRefresh01:" + isNeedToRefresh);
                if (isNeedToRefresh){
                    basePreference.putBoolean("is_refresh_shoppingPage", false);
                    Log.d(TAG, "isNeedToRefresh02:" + basePreference.getBoolean("is_refresh_shoppingPage", false));//减少实例的创建
                    shoppingpage = new ShoppingPageView(this);
                }
            }

        }

        linear_content.addView(shoppingpage);
    }

    private void enterToCollectionPage(boolean refresh) {
        FrontPageHead.setVisibility(View.GONE);
        linear_content.removeAllViews();
        if (null == collectionpageview) {
            collectionpageview = new CollectionPageView(this);
        }else {
            if (refresh){
                collectionpageview = new CollectionPageView(this);
            }else {
                isNeedToRefresh = basePreference.getBoolean("is_refresh_collectionPage", false);
                Log.d(TAG, "isNeedToRefresh01:" + isNeedToRefresh);
                if (isNeedToRefresh){
                    basePreference.putBoolean("is_refresh_collectionPage", false);
                    Log.d(TAG, "isNeedToRefresh02:" + basePreference.getBoolean("is_refresh_collectionPage", false));//减少实例的创建
                    collectionpageview = new CollectionPageView(this);
                }
            }
        }
        //只是更新部分


        linear_content.addView(collectionpageview);
    }



    @OnClick({R.id.home_city_from, R.id.edit_search, R.id.imageView_pull_list, R.id.textview_scan_code, R.id
            .textview_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_city_from:
                chooseCity();
                break;
            case R.id.edit_search:
                search();
                break;
            case R.id.imageView_pull_list:
                displayList();
                break;
            case R.id.textview_scan_code:
                capture();
                break;
            case R.id.textview_center:
                IntentUtil.gotoActivityWithoutData(RadioGroupActivity.this, MyOrderListActivity.class, false);

                break;
        }
    }

    @OnClick(R.id.textview_store)
    public void onClick() {
        Log.d("FrontPageView", "跳转到店铺列表");
        /*
        * 第一版：IntentUtil.gotoActivityWithoutData(RadioGroupActivity.this, ShopSearchActivity.class, false);
        * 第二版：第二版再来查看为什么销量排序有问题
        *
        * */
        IntentUtil.gotoActivityWithoutData(RadioGroupActivity.this, ShopListActivity.class, false);


    }

    //14.城市选择
    void chooseCity() {
        Log.d("FrontPageView", "chooseCity");

        IntentUtil.gotoActivityForResult(RadioGroupActivity.this, CitySelecterActivity.class, SELECT_CITY);
    }

    //15.点击搜索
    void search() {
        Log.d("FrontPageView", "search");
        IntentUtil.gotoActivityWithoutData(RadioGroupActivity.this, SearchActivity.class, false);
    }

    //13.点击下拉菜单
    void displayList() {// 参数一般建议传递当前ID对应的控件或者无参传递，会自动映射
        Log.d("FrontPageView", "display");
        if (isNotDisplay) {
            //tips_arrow.setVisibility(View.GONE);
            tips_content.setVisibility(View.GONE);
        } else {
            Log.d("FrontPageView", "01");
            //tips_arrow.setVisibility(View.VISIBLE);
            tips_content.setVisibility(View.VISIBLE);
        }
        isNotDisplay = !isNotDisplay;
    }
    //16.扫二维码

    void capture() {
        Log.d("FrontPageView", "capture");
        IntentUtil.gotoActivityWithoutData(RadioGroupActivity.this, CaptureActivity.class, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "被执行了");
        Log.d(TAG, "requestCode:" + requestCode);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage == null) {
                        return;
                    }
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null && filePathColumn != null) {
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor == null) {
                            return;
                        }
                        if (cursor.moveToFirst()) {
                            int columnIndex = cursor
                                    .getColumnIndex(filePathColumn[0]);
                            filePath = cursor.getString(columnIndex);
/*                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.PHONE_PATH, filePath);*/
                            Log.d(TAG, filePath);
                            onPictureStateChangedListener.onPictureChanged(filePath);

//                            enterToMinePage(bundle);
                        }
                        cursor.close();
                    }
                }
                break;
            case SELECT_CAMERO:
                if (filePath != null) {
                    onPictureStateChangedListener.onPictureChanged(filePath);
                } else {
                    ToastUtil.showHintMessage("未找到图片，请重新选取照片");
                }
                break;
            case LOGIN_TO_SPV:
                //这些了吗？
                Log.d(TAG, "执行了吗");
                onRreshListener.onRresh();  // 因为有两个模块，其实只是触发其中一个，除非设置不同的标志量
                //enterToShoppingPage(); //登录成功后，跳转到购物车模块
                break;
        }

    }

    //监听器：编辑状态更新触发的操作,一个实例，一个注册set方法
    OnPictureStateChangedListener onPictureStateChangedListener;

    public void setIsHasPopWindows(boolean isHasPopWindws) {
        Log.d("kkkkkk", "isHasPopWindws:" + isHasPopWindws);
        this.isHasPopWindws = isHasPopWindws;

    }

    public interface OnPictureStateChangedListener{
        void onPictureChanged(String  picpath);
    }
    public void setOnPictureStateChangedListener(OnPictureStateChangedListener onPictureStateChangedListener) {
        this.onPictureStateChangedListener = onPictureStateChangedListener;
    }


    //再次更新界面
    OnRreshListener onRreshListener;
    public interface OnRreshListener{
        void onRresh();
    }
    public void setOnRreshChangedListener(OnRreshListener onRreshListener) {
        this.onRreshListener = onRreshListener;
    }

    /*选择城市，更新首页*/
/*    OnCitySelectListener onCitySelectListener;
    public interface OnCitySelectListener{
        void onCitySelected(String  city);
    }
    public void setOnPictureStateChangedListener(OnCitySelectListener onCitySelectListener) {
        this.onCitySelectListener = onCitySelectListener;
    }*/


    /*
    *
    *     @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(pvOptions.isShowing()||pvTime.isShowing()){
                pvOptions.dismiss();
                pvTime.dismiss();
                return true;
            }
            if(pvTime.isShowing()){
                pvTime.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    *
    *
    * */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

/*            * 处理逻辑：1.首先判断是不是位于首页，如果是，则点击两下退出程序，如果不是则跳转到首页。
            *   原理上是可行的
            * */
            Log.d("kkkkkk", "22" + 22);
            Log.d("kkkkkk", "isHasPopWindws:" + isHasPopWindws);
            if (isHasPopWindws) {
                Log.d("kkkkkk", "1");
                isHasPopWindws = false;//只执行一次，这个bug 真是恶心死我了，先放着吧！，以后改为popWindow
                Log.d("kkkkkk", "first:" + isHasPopWindws);
                return true;//直接拦截，并且只拦截一次就好了

            } else {
                Log.d("kkkkkk", "2");
                Log.d("kkkkkk", "isOnlyProOnce:" + isOnlyProOnce);
                exit();
                return false;//算了这个先放一边了，只能以后替换一个控件了

            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        /*需要点击三下*/
        if (!radio_homepage.isChecked()){
            Log.d("kkkkkk", "3");
            radio_homepage.setChecked(true);
            enterToFrontPage(false);
            //这个地方直接销毁了，是为什么呢！
            return ;
        }

        if(flag){
            Log.d("kkkkkk", "4");
            ToastUtil.showHintMessage("再点击一次退出程序");
            //毫秒
            flag =false;
            /*多个消息可以使用同一个handler,通过what不同区分不同的消息来源， 从而获取消息内容*/
            handler.sendEmptyMessageDelayed(0,3000);// 这里是有bug的，如果已经点击过一次退出程序了，下次就没有任何提醒了,what 区分不同的消息，处理不同的内容 【3秒的时间】
        }else {
            Log.d("kkkkkk", "5");
            finish();
            System.exit(0);
        }
    }
    /*有一个时间的延迟，如果3秒内不退出，会再次弹框*/
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            flag =true;
        }
    };
}
