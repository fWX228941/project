package com.android.haobanyi.activity.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.adapter.CityListSortAdapter01;
import com.android.haobanyi.adapter.HotCityGridViewAdapter;
import com.android.haobanyi.api.utils.PinyinComparator;
import com.android.haobanyi.model.bean.home.location.CityBean;
import com.android.haobanyi.model.bean.home.location.CitySortBean;
import com.android.haobanyi.model.db.CityDAO;
import com.android.haobanyi.model.util.CharacterParserUtil;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.CustomGridView;
import com.android.haobanyi.view.SideBar;
import com.android.haobanyi.view.TitleBar;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/5/11.
 *
 * @作者: 付敏
 * @创建日期：2016/05/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：城市选择界面 参考设计：https://github.com/chsmy/CitySelector
 *
 * 计算距离：
        LatLng lastPoint = new LatLng(location.getLatitude(),
        location.getLongitude());
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        double distance = DistanceUtil.getDistance(lastPoint, curPoint); 距离是米，而且是百度经纬度坐标
 *
 */
public class CitySelecterActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar; // 标题栏
    @BindView(R.id.edit_city_search)
    EditText editCitySearch; // 搜索框
    @BindView(R.id.lv_city_search)
    ListView lvCitySearch; // 城市列表 - 对应一个adapter【citySortAdapter】，一个数据源citySortDateList 数据源来源于数据库，数据库中再次过滤
    private CityListSortAdapter01 citySortAdapter;
    private List<CitySortBean> citySortDateList;
    private List<CityBean> cityDatabaseList;
    private List<String> cityNames;
    @BindView(R.id.dialog)
    TextView dialog;// 滑动提示框
    @BindView(R.id.sidrbar)
    SideBar sidrbar;// 右侧边滑动栏 - 对应一个adapter

    private CustomGridView idGridviewHotcity; // 热门城市列表 - 对应一个adapter[HotCityGridViewAdapter]，一个数据源
    private HotCityGridViewAdapter hotCityAdapter;
    private List<CityBean> hotCityDatas;//热门城市数据源

    //汉字拼音相互转化的工具类
    private CharacterParserUtil characterParserUtil;
    //按照拼音排序的比较规则
    private PinyinComparator pinyinComparator;
    //城市定位
    private LocationService locationService;
    private TextView LocationResult;
    private ImageView LocationResultImg;

    private TextView LocationLoading;
    private boolean isLocationSuccessful =false ;
    private String resultCity;
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected int setLayout() {
        return R.layout.activity_city_selecter;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        /*04初始化汉字拼音相互转化的工具类*/
        characterParserUtil = CharacterParserUtil.getInstance();
        Log.d("CitySelecterActivity", "cityNames05" + characterParserUtil.toString());
        /*05.初始化拼音比较器*/
        pinyinComparator = new PinyinComparator();
        Log.d("CitySelecterActivity", "cityNames06" + pinyinComparator.toString());

    }

    @Override
    protected void initViews() {
        /*1.设置标题栏*/
        initTittleBar();

        /*动态申请权限，after andrioid m,must request Permiision on runtime*/
        getPersimmions();


    }

    /*只有23以上才会执行*/
    @TargetApi(23)
    private void getPersimmions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);//关键就是这句话了
            }
        }


    }
    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void loadData() {
        /*01.热门城市数据源,动态添加，其实可以使用一个数组文件array.xml文件动态存储*/
        hotCityDatas = new ArrayList<CityBean>();
        hotCityDatas.add(new CityBean(2, 1, "北京"));
        hotCityDatas.add(new CityBean(25, 1, "上海"));
        hotCityDatas.add(new CityBean(77, 6, "深圳"));
        hotCityDatas.add(new CityBean(76, 6, "广州"));
        hotCityDatas.add(new CityBean(197, 14, "长沙"));
        hotCityDatas.add(new CityBean(343, 1, "天津"));

        /*02.获取list的省份来源,从数据库中获取CityBean*/
        cityDatabaseList = CityDAO.getProvencesOrCity(1);
        Log.d("CitySelecterActivity", "cityDatabaseList01" + cityDatabaseList.toString());
        cityDatabaseList.addAll(CityDAO.getProvencesOrCity(2));
        Log.d("CitySelecterActivity", "cityDatabaseList02" + cityDatabaseList.toString());
        citySortDateList = filledDataWithPinyin(cityDatabaseList);
        Log.d("CitySelecterActivity", "cityDatabaseList03" + citySortDateList.toString());
        /*03.数据库中获取的城市实体，只需要其名字构成的数据集*/
        cityNames = new ArrayList<String>();
        for (CityBean info : cityDatabaseList) {
            cityNames.add(info.getName().trim());
            Log.d("CitySelecterActivity", "cityNames04" + cityNames.toString());
        }


    }


    @Override
    protected void registerEventListener() {//关联数据的在
        //2.添加热门城市视图
        initHotCityView();
        //4.添加排序城市列表
        initSortCityList();  //顺序也是不能变更的，因为先定好位置再来监听
        //3.并设置滑动右边栏
        initSideBar();
        //5.为搜索栏监听搜索过滤变化
        initSearchText();

    }


    @Override
    protected void registerBroadCastReceiver() {

    }

    @Override
    protected void saveState(Bundle outState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((BaseApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        LocationResult.setVisibility(View.GONE);
        LocationResultImg.setVisibility(View.GONE);
        LocationLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
              //  StringBuffer sb = new StringBuffer(256);
                String result = "定位失败,检查GPS，重新点击定位" ;
//                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
//                sb.append(location.getTime()); //这个其实可以作为时间戳
/*                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());*/
//                sb.append("\nPoi: ");
 /*               if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }*/
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    Log.d("CitySelecterActivity", "gps定位成功");
                    isLocationSuccessful = true;
                    result = location.getDistrict();
                    saveFile(location);
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    Log.d("CitySelecterActivity", "网络定位成功");
                    result = location.getDistrict();
                    isLocationSuccessful = true;
                    saveFile(location);
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    Log.d("CitySelecterActivity", "离线定位成功，离线定位结果也是有效的");
                    result = location.getDistrict();
                    isLocationSuccessful = true;
                    saveFile(location);
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Log.d("CitySelecterActivity", "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    result = "定位失败,检查GPS，重新点击定位";
                    isLocationSuccessful = false;
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Log.d("CitySelecterActivity", "网络不同导致定位失败，请检查网络是否通畅");
                    result = "定位失败,检查GPS，重新点击定位";
                    isLocationSuccessful = false;
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Log.d("CitySelecterActivity", "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    result = "定位失败,检查GPS，重新点击定位";
                    isLocationSuccessful = false;
                }
                changResult(result,isLocationSuccessful);
            }
        }

    };
    /*如果定位成功就保存记录*/
    private void saveFile(BDLocation location) {
        /*位置相关的数据就保存在location中*/
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        complexPreferences.putString(Constants.latitude,Double.toString(location.getLatitude()));
        complexPreferences.putString(Constants.lontitude, Double.toString(location.getLongitude()));
        complexPreferences.putString(Constants.CountryCode,location.getCountryCode());
        complexPreferences.putString(Constants.country,location.getCountry());
        complexPreferences.putString(Constants.citycode,location.getCityCode());
        complexPreferences.putString(Constants.city,location.getCity());
        complexPreferences.putString(Constants.district,location.getDistrict());
        complexPreferences.putString(Constants.street,location.getStreet());
        complexPreferences.putString(Constants.addr,location.getAddrStr());
        complexPreferences.putString(Constants.Describe,location.getLocationDescribe());
        StringBuffer sb = new StringBuffer(256);
        if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
            for (int i = 0; i < location.getPoiList().size(); i++) {
                Poi poi = (Poi) location.getPoiList().get(i);
                sb.append(poi.getName() + ";");
            }
        }
        complexPreferences.putString(Constants.Poi,sb.toString());
    }

    /*当定位成功后，把定位结果显示出来*/
    private void changResult(String s, boolean isLocationSuccessful) {
        try {
            if (LocationResult != null){
                if (isLocationSuccessful){
                    LocationLoading.setVisibility(View.GONE);
                    LocationResult.setVisibility(View.VISIBLE);
                    LocationResultImg.setVisibility(View.VISIBLE);
                    Drawable leftDrawable = getResources().getDrawable(R.drawable.nearby_icon_01);
                    LocationResultImg.setImageDrawable(leftDrawable);
                    LocationResult.setText(s);
                    resultCity=s;
                    locationService.stop();
                } else {
                    LocationResult.setVisibility(View.GONE);
                    LocationResultImg.setVisibility(View.GONE);
                    LocationLoading.setVisibility(View.VISIBLE);
                    LocationLoading.setText(s);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*04.动态生成带拼音首字母的城市列表数据*/
    private List<CitySortBean> filledDataWithPinyin(List<CityBean> data) {
        List<CitySortBean> mSortList = new ArrayList<CitySortBean>();
        Log.d("CitySelecterActivity", "01");
        for (int i = 0; i < data.size(); i++) {
            CitySortBean citySortBean = new CitySortBean();

            String cityName = data.get(i).getName();
            citySortBean.setName(cityName);
            //汉字转换成拼音
            String pinyin = characterParserUtil.getSelling(cityName);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                citySortBean.setSortLetters(sortString.toUpperCase());
            }
            /* else {
                citySortBean.setSortLetters("#");
            }*/

            mSortList.add(citySortBean);
        }
        return mSortList;

    }

    /*02.设置标题栏*/
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.black);
        // 设置返回箭头和文字以及颜色
        titleBar.setLeftImageResource(R.drawable.back_icon);
        // 设置监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CitySelecterActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitleBackground(R.drawable.top_navagation_logo);
        titleBar.setDividerColor(Color.GRAY);

    }

    /*03.初始化热门城市视图*/
    private void initHotCityView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_hot_city_list, null);
        LocationResult = (TextView) view.findViewById(R.id.id_item_city_result);
        LocationLoading = (TextView) view.findViewById(R.id.id_item_city_loading);
        LocationResultImg = (ImageView) view.findViewById(R.id.imageView3);
        /*当定位失败可以重新触发监听*/
        LocationLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationService.start();
                LocationResult.setVisibility(View.GONE);
                LocationResultImg.setVisibility(View.GONE);
                LocationLoading.setVisibility(View.VISIBLE);
            }
        });
        LocationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.district,resultCity);
                IntentUtil.gotoTopActivityWithData(CitySelecterActivity.this, RadioGroupActivity.class, bundle, true);
            }
        });
        idGridviewHotcity = (CustomGridView) view.findViewById(R.id.id_gridview_hotcity);//类型要转化过来，不然会出现空指针异常  自定视图
        //添加到视图到ListView的头部
        lvCitySearch.addHeaderView(view);
        //item_tag  这个为了复用
        hotCityAdapter = new HotCityGridViewAdapter(this, hotCityDatas, R.layout.item_tag);
        idGridviewHotcity.setAdapter(hotCityAdapter);
        idGridviewHotcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityBean bean = hotCityAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.district, bean.getName());
                IntentUtil.gotoTopActivityWithData(CitySelecterActivity.this, RadioGroupActivity.class, bundle, true);

            }
        });
        //击GridView时出现背景色，可以给gridview设置setSelector背景色为透明色：
        idGridviewHotcity.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    /*04.设置滑动右边栏*/
    private void initSideBar() {
        //1.通过设置滑动或者手点击时的，屏幕中间会显示的
        sidrbar.setTextView(dialog);
        //2.设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = citySortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                   /*listview该方法用于显示，哪个item项会被显示在最上面，调用setSelection(9),则第10个数据项会显示在最上面
                    * 快速查阅对应分组的侧边栏
                    * */
                    lvCitySearch.setSelection(position + 1);
                }


            }
        });

    }

    /*05.添加排序城市列表*/
    private void initSortCityList() {
        /*06.对cityDatabaseList数据源进行拼音排序*/
        Collections.sort(citySortDateList, pinyinComparator);
        citySortAdapter = new CityListSortAdapter01(this, citySortDateList);
        lvCitySearch.setAdapter(citySortAdapter);
        lvCitySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CitySortBean bean = (CitySortBean) citySortAdapter.getItem(position-1);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.district, bean.getName());
                IntentUtil.gotoTopActivityWithData(CitySelecterActivity.this, RadioGroupActivity.class, bundle, true); // 最后一个参数貌似没有用，还是会
            }
        });
    }

    /*06.为搜索栏监听搜索过滤变化*/
    private void initSearchText() {
        editCitySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //根据输入框中的值来过滤数据并更新ListView
    private void filterData(String filterStr) {
        List<CitySortBean> filterDateList = new ArrayList<CitySortBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = citySortDateList;
        } else {
            if (!cityNames.contains(filterStr)) {
                filterDateList.clear();
                for (CitySortBean citySortBean : citySortDateList) {
                    String name = citySortBean.getName();
                    Log.d("CitySelecterActivity", "cityNames09" + name);
                    String cell = characterParserUtil.getSelling(name);
                    Log.d("CitySelecterActivity", "cityNames10" + cell);
                    if (name.indexOf(filterStr.toString()) != -1 || cell.startsWith
                            (filterStr.toString())) {
                        filterDateList.add(citySortBean);
                    }
                }
            } else {
                filterDateList.clear();
                for (int i = 0; i < cityDatabaseList.size(); i++) {
                    String name = cityDatabaseList.get(i).getName();
                    Log.d("CitySelecterActivity", "cityNames11" + name);
                    if (name.equals(filterStr)) {
                        filterDateList.addAll(filledDataWithPinyin(CityDAO.getProvencesOrCityOnParent(cityDatabaseList
                                .get(i)
                                .getId())));
                    }
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        citySortAdapter.updateListView(filterDateList);//这个方法需要在子类中实现吗？

    }

    /*选择城市，更新首页*/

}
