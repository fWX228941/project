package com.android.haobanyi.util.citypickerview.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.databinding.tool.util.L;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.city.AreaList;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.citypickerview.model.CityModel;
import com.android.haobanyi.util.citypickerview.model.DistrictModel;
import com.android.haobanyi.util.citypickerview.model.ProvinceModel;
import com.android.haobanyi.util.citypickerview.widget.wheel.OnWheelChangedListener;
import com.android.haobanyi.util.citypickerview.widget.wheel.WheelView;
import com.android.haobanyi.util.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;
import com.google.gson.Gson;


/**
 * 省市区三级选择
 * 修改部分：暂时先把邮编去掉
 * 解析文件也进行了修改  xml文件要改为json格式
 *
 *
 *
 */
public class CityPickerView implements com.android.haobanyi.util.citypickerview.widget.CanShow, OnWheelChangedListener {

    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private TextView mTvOK;

    private TextView mTvCancel;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    protected List<String> mProvinceDatas01 = new ArrayList<String>();
    /**
     * key - 省 values - 邮编
     */
    protected Map<String, String>  mProvinceIDsMap = new HashMap<String, String>();
    /*
    * 所有省ID
    * */
    protected long[] mProvinceIDDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    protected Map<String, String> mCitisIDsMap = new HashMap<String, String>();


    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap01 = new HashMap<String, String[]>();  //取一个唯一标识  最终区域还是这个生效了

    /**
     * key - 区 values - 邮编  这真的是一个奇葩的问题，mDistrictIDsMap 存都没有问题，结果取得时候第一个地区存在问题
     */
    protected Map<String, String>  mDistrictIDsMap = new HashMap<String, String>();
    protected Map<String, String>  mDistrictIDsMap01 = new HashMap<String, String>();



    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /*
    * 新：当前的省ID
    * */
    protected String mCurrentProviceID;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    /*
    * 新：当前的市ID
    * */
    protected String mCurrentCityID;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";


    /*
    * 新：当前的区ID
    * */
    protected String  mCurrentDistrictID;
    String[] distrinctID02;
    private boolean isFirtTine = true;


    /**
     * 当前区的邮政编码
     */
    //protected String mCurrentZipCode = "";

    private OnCityItemClickListener listener;

    public interface OnCityItemClickListener {
        void onSelected(String... citySelected);//原来是一个数组，这个东西自己定义 ,这样实际上完美了
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }


    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;


    /**
     * 滚轮是否循环滚动
     */
    boolean isCyclic = true;
    /* 持有私有静态实例，防止被引用，此处赋值为null，目的是实现延迟加载 */
/*    private static CityPickerView instance = null;

    *//* 私有构造方法，防止被实例化 *//*
    private CityPickerView() {
    }

    *//* 1:懒汉式，静态工程方法，创建实例 *//*
    public static CityPickerView getInstance(final Context context) {
        if (instance == null) {
            instance = new CityPickerView(context);
        }
        return instance;
    }*/

    public CityPickerView(final Context context) {
        super();
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);

        mViewProvince = (WheelView) popview.findViewById(R.id.id_province);
        mViewCity = (WheelView) popview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popview.findViewById(R.id.id_district);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);

        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(0x80000000));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.setFocusable(true);

        //初始化城市数据
        //initProvinceDatas(context);
        // 初始化城市数据版本二

            initProvinceDatas01(context);



        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.onSelected(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentZipCode);,mCurrentProviceID,mCurrentCityID,mCurrentDistrictID
                listener.onSelected(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName,mCurrentProviceID,mCurrentCityID,mCurrentDistrictID);
                hide();
            }
        });
    }


    /**
     * 设置颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        this.textColor = color;
    }

    /**
     * 设置颜色
     *
     * @param colorString
     */
    public void setTextColor(String colorString) {
        this.textColor = Color.parseColor(colorString);
    }

    private int getTextColor() {
        return textColor;
    }

    /**
     * 设置文字颜色
     *
     * @param textSp
     */
    public void setTextSize(int textSp) {
        this.textSize = textSp;
    }

    private int getTextSize() {
        return textSize;
    }

    /**
     * 设置滚轮显示个数
     *
     * @param count
     */
    public void setVisibleItems(int count) {
        this.visibleItems = count;
    }

    private int getVisibleItems() {
        return this.visibleItems;
    }

    /**
     * 设置滚轮是否循环滚动
     *
     * @param isCyclic
     */
    public void setIsCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
    }

    private boolean getIsCyclic() {
        return this.isCyclic;
    }

    private void setUpData() {
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mProvinceDatas);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(getVisibleItems());
        mViewCity.setVisibleItems(getVisibleItems());
        mViewDistrict.setVisibleItems(getVisibleItems());
        mViewProvince.setCyclic(getIsCyclic());
        mViewCity.setCyclic(getIsCyclic());
        mViewDistrict.setCyclic(getIsCyclic());

        arrayWheelAdapter.setTextColor(getTextColor());
        arrayWheelAdapter.setTextSize(getTextSize());

        updateCities();
        updateAreas();
    }
    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas01(Context context) {
        List<ProvinceModel> provinceList = null;
       // AssetManager asset = context.getAssets();
        try {
             //InputStream input = asset.open("preferences_city.json");
            // 创建一个解析xml的工厂对象
            // 获取解析出来的数据
            provinceList = getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                 mCurrentProviceName = provinceList.get(0).getName();
                Log.d("CityPickerView10", mCurrentProviceName);
                mCurrentProviceID = provinceList.get(0).getProvinceID();
                Log.d("CityPickerView11", mCurrentProviceID);
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    Log.d("CityPickerView09", mCurrentCityName);
                    mCurrentCityID = cityList.get(0).getCityID();
                    Log.d("CityPickerView12", mCurrentCityID);
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    Log.d("CityPickerView08", mCurrentDistrictName);
                    mCurrentDistrictID = districtList.get(0).getDistrictID();
//                    Log.d("CityPickerView07", mCurrentDistrictID);
                }
            }

            mProvinceDatas = new String[provinceList.size()];
            //PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                mProvinceIDsMap.put(mProvinceDatas[i],provinceList.get(i).getProvinceID());
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];  // 所有市
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    mCitisIDsMap.put(cityNames[j], cityList.get(j).getCityID());
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctName = new String[districtList.size()];//所有区
                    String[] distrincID = new String[districtList.size()];//所有区

                    distrinctID02 = new String[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        distrinctName[k] = districtList.get(k).getName();
                        distrincID[k] = districtList.get(k).getDistrictID();
                        mDistrictIDsMap.put(distrinctName[k], districtList.get(k).getDistrictID());
                        mDistrictIDsMap01.put(districtList.get(k).getDistrictID(),distrinctName[k]);
                        //complexPreferences.putString(distrinctName[k],districtList.get(k).getDistrictID());
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityList.get(j).getName(), distrinctName);
                    mDistrictDatasMap01.put(cityList.get(j).getName(), distrincID);

                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
        }
    }

    private List<ProvinceModel> getDataList() {
        //List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("preferences_city.json");
            /*
            *
            * http://www.tuicool.com/articles/6vUfEj
            * http://blog.csdn.net/randyjiawenjie/article/details/6589509
            * http://blog.sina.com.cn/s/blog_a000da9d010121bl.html
            *把 InputStream 转换成 String
            * */
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
            List<ProvinceModel> provinceList01 = new ArrayList<ProvinceModel>();
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



    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        Log.d("CityPickerView14", "pCurrent:" + pCurrent);
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        Log.d("CityPickerView01", mCurrentCityName);
        mCurrentCityID = mCitisIDsMap.get(mCurrentCityName);
        Log.d("CityPickerView02", mCurrentCityID);
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<String>(context, areas);
        // 设置可见条目数量
        districtWheel.setTextColor(getTextColor());
        districtWheel.setTextSize(getTextSize());
        mViewDistrict.setViewAdapter(districtWheel);
        mViewDistrict.setCurrentItem(0);//我怀疑是这个地方有问题

        //获取第一个区名称  只能是根据地区找到下标
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[pCurrent];//java.lang.ArrayIndexOutOfBoundsException: length=3; index=5 这个出现下标数组越位
        mCurrentDistrictID = mDistrictDatasMap01.get(mCurrentCityName)[pCurrent];
        Log.d("CityPickerView03", mCurrentDistrictName);
        Log.d("CityPickerView77", mCurrentDistrictID);

    }
/*    public String getDistrictId(String mCurrentDistrictName){
        String districtId ="";
        if (mDistrictIDsMap!=null){
            districtId = mDistrictIDsMap.get(mCurrentDistrictName);

        }
        return districtId;
    }*/
    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();//这个就是坐标
        Log.d("CityPickerView13", "pCurrent:" + pCurrent);
        mCurrentProviceName = mProvinceDatas[pCurrent];// 也要一起关联
        Log.d("CityPickerView05", mCurrentProviceName);
        mCurrentProviceID = mProvinceIDsMap.get(mCurrentProviceName);
        Log.d("CityPickerView06", mCurrentProviceID);
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }

        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<String>(context, cities);
        // 设置可见条目数量
        cityWheel.setTextColor(getTextColor());
        cityWheel.setTextSize(getTextSize());
        mViewCity.setViewAdapter(cityWheel);
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            Log.d("CityPickerView31", "newValue:" + newValue);
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            Log.d("CityPickerView32", mCurrentDistrictName);
            mCurrentDistrictID = mDistrictDatasMap01.get(mCurrentCityName)[newValue];

        }
    }
}
