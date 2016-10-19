package com.android.haobanyi.activity.searching;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.home.SearchActivity;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.activity.zxing.CaptureActivity;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/5/23.
 *
 * @作者: 付敏
 * @创建日期：2016/05/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：搜索的结果界面 根据关键字搜索
 * activity_keyword_search
 * fragment_keyword_product_search
 * KeywordProductFragment
 * fragment_keyword_shop_search
 * KeywordShopFragment
 */
public class KeywordSearchActivity extends BaseActivity {

    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.id_textView_back)
    ImageView idTextViewBack;
    @BindView(R.id.id_editText_search)
    TextView idEditTextSearch;//  Caused by: java.lang.ClassCastException: android.widget.TextView cannot be cast to android.widget.EditText 修改类型的时候，切记
    @BindView(R.id.id_textView_search_more)
    LinearLayout idTextViewSearchMore;
    @BindView(R.id.index_home_memus_content)
    LinearLayout indexHomeMemusContent;
    @BindView(R.id.textview_home)
    TextView textviewHome;
    @BindView(R.id.textview_scan_code)
    TextView textviewScanCode;
    private String[] categoryArray;
    String user_input; //用户输入,这个就是关键字
    Bundle bundle = new Bundle();
    private boolean isNotDisplay = false;
    int positon = 0;
    @Override
    protected int setLayout() {
        return R.layout.activity_keyword_search;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }

        //01.获取分类字符串
        categoryArray = getResources().getStringArray(R.array.keyword_list);

        //02.获取从activity中传递过来的数据
        user_input = this.getIntent().getStringExtra(Constants.USER_SEARCH);
        bundle.putString(Constants.USER_SEARCH, user_input);

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        List<String> user_input_list = new ArrayList<String>();
        user_input_list = complexPreferences.getListObject(Constants.USER_SEARCH_LIST, String.class);
        if (!user_input_list.contains(user_input)) {
            user_input_list.add(user_input);
            complexPreferences.putListObject(Constants.USER_SEARCH_LIST, user_input_list);
        }

        positon = this.getIntent().getIntExtra(Constants.POSITION, 0);

        Log.d("KeywordSearchActivity", "执行几次");
        complexPreferences.putBoolean("onlyForOnce",true);//用于缓存只2执行一次



    }

    /*fragment,一定得注意版本问题*/
    @Override
    protected void initViews() {
        idEditTextSearch.setText(user_input);
        //这里光标也是需要设置下
        initViewPager();


    }

    /*        FragmentPagerItems pages = new FragmentPagerItems(this);
            for (String name : categoryArray) {
                pages.add(FragmentPagerItem.of(name, CategoryListFragment.class));
            }
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), pages);*/
    private void initViewPager() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(categoryArray[0], KeywordProductFragment.class, bundle)
                .add(categoryArray[1], KeywordShopFragment.class, bundle)
                .add(categoryArray[2], KeywordPriceUpFragment.class, bundle)
                .add(categoryArray[3], KeywordPriceDownFragment.class, bundle)
                .create());
        adapter.getPage(positon);
        viewpager.setAdapter(adapter);// 左右滑动页面关联fragment适配器
        viewpager.setCurrentItem(positon);//这个失效了，真是蛋疼
        //02.设置ViewPager缓存个数，意味这个多次调用onViewCreated方法
        /*
        * http://blog.csdn.net/aohanyao/article/details/50833373
        * android小技巧之不缓存的ViewPager
        *
        * */
        viewpager.setOffscreenPageLimit(0);//adapter.getCount()
        //03.关联smartTabLayout和ViewPager
        smartTabLayout.setViewPager(viewpager);


        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("KeywordSearchActivity", "01:"+position);//顺滑，实时调用

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("KeywordSearchActivity", "02:"+position);//这个position 是正确的1，2  就这个有用
                onPageChangedListener.onPassPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        /*基本上没有用*/
/*        smartTabLayout.setOnScrollChangeListener(new SmartTabLayout.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(int scrollX, int oldScrollX) {
                Log.d("KeywordSearchActivity", "03:");
            }
        });*/
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


    @OnClick({R.id.id_textView_back, R.id.id_textView_search_more,R.id.id_editText_search, R.id.textview_home, R.id.textview_scan_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_textView_back:
                finish();
                KeywordSearchActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
                break;
            case R.id.id_textView_search_more://下拉
                pullList();//下拉菜单
                break;
            case R.id.id_editText_search:
                gotoSearch();
                break;
            case R.id.textview_home:
                gotoHomePage();
                break;
            case R.id.textview_scan_code:
                gotoScanCode();
                break;
        }
    }
    /*跳转到搜索界面*/
    private void gotoSearch() {
        IntentUtil.gotoActivityWithoutData(KeywordSearchActivity.this, SearchActivity.class, false);

    }
    private void pullList() {
        if (isNotDisplay) {
            indexHomeMemusContent.setVisibility(View.GONE);
        } else {
            indexHomeMemusContent.setVisibility(View.VISIBLE);
        }
        isNotDisplay = !isNotDisplay;

    }
    private void gotoHomePage() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.STATE_FROM_SHOPPING, 02);
        IntentUtil.gotoTopActivityWithData(KeywordSearchActivity.this, RadioGroupActivity.class, bundle, true);

    }
    private void gotoScanCode() {
        IntentUtil.gotoActivityWithoutData(KeywordSearchActivity.this, CaptureActivity.class, false);
    }

    OnPageChangedListener onPageChangedListener;
    public interface OnPageChangedListener{
        void onPassPosition(int position);
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
    }
}
