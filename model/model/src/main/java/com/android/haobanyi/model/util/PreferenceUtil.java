package com.android.haobanyi.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fWX228941 on 2016/4/27.
 *
 * @作者: 付敏
 * @创建日期：2016/04/26
 * @邮箱：466566941@qq.com
 * @当前文件描述：preference文件存储类工具类，
 * 参考设计：https://github.com/fWX228941/android-complex-preferences
 * https://github.com/fWX228941/TinyDB--Android-Shared-Preferences-Turbo
 * 扩展设计：
 * 01.由原来支持基本类型，到支持原型对象
 * eg.
        User user = new User();
        user.setName("Felipe");
        user.setAge(22);
        user.setActive(true);
 *
 * 用法说明：
 *  存放单个数据
    PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
    complexPreferences.putString(Constants.ACCESS_TOKEN,bean.getAccessToken());
    系统从内部存储中删除你app的所有文件 参考文档：http://www.jb51.net/article/56300.htm
    卸载以后，文件都清空

        暂时先分为四个文件，以后细分，貌似存在数量限制，分别是：
        01.com.android.haobanyi_preferences：用于存放除店铺和服务外的其他信息,也就是非这三个id标识的
            key:district_location : 定位城市  RadioGroupActivity
            一系列城市定位相关信息：CitySelecterActivity
            key:registtedpn：登录成功的账号   LoginActivity
            key:first_open：是否第一次开启应用的标识量   SplashActivity
            key:ip_address: ip地址  RegisterActivity02
            key:newest_normal_sort_product_list: 最新一列的综合排序的服务列表


        02.com.android.haobanyi_product：服务相关   key:productId    product_details-> product
        03.com.android.haobanyi_shop: 店铺相关    key:shopId
        04.com.android.haobanyi_user:用户相关    key:userID 来标识，可以记录多个用户的信息

PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
complexPreferences.putString(Constants.ACCESS_TOKEN, bean.getAccessToken());
complexPreferences.putLong(Constants.USERID, bean.getUserID());
complexPreferences.putInt(Constants.EXPIRES_IN, bean.getExpiresIn());
complexPreferences.putBoolean(Constants.ISLOGIN,isLogin);


每次只有一个用户登录

原始json 数据存入

SharedPreferences preference = context.getSharedPreferences("preferences_city", Context
.MODE_PRIVATE);
SharedPreferences.Editor editor = preference.edit();
Gson GSON = new Gson();
editor.putString("see", GSON.toJson(response.body()));
editor.apply();


        待确认：WelcomeGuideActivity.getDataFromServer

 */
public class PreferenceUtil {
    //final的常量必须先初始化，这个是全局性的文件
    //private static final String preferenceFileName.get(filename) = "com.android.haobanyi_preferences";
    //利用一个HashMap  这个地方需要优化一下，会不会存在冲突问题
    private static HashMap<String ,String> preferenceFileName = new HashMap<String,String>();
    private static String fileName = null;
    private static PreferenceUtil preferenceUtil;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static Gson GSON = new Gson();
    private PreferenceUtil (Context context ,String fileName){
        this.context = context;
        this.preferences = context.getSharedPreferences(getFileName(fileName),Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    //支持对象
    public static PreferenceUtil getSharedPreference(Context context ,String fileName){
        //采用单例模式,被这个方式给坑了，应该是

        return new PreferenceUtil(context,fileName);

    }
    /**
     * Register SharedPreferences change listener
     * @param listener listener object of OnSharedPreferenceChangeListener
     */
    public void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregister SharedPreferences change listener
     * @param listener listener object of OnSharedPreferenceChangeListener to be unregistered
     */
    public void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
    /*
    * 1.先参数判断
    * 2.后添加键值
    * 3.使用方法 【可能整合为一个方法】存放对象
    User user = new User();
    user.setName("Felipe");
    user.setAge(22);
    user.setActive(true);
    PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(this, "mypref");;
    complexPreferences.putObject("user", user);
     4.取对象
    PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(this, "mypref");
    User user = complexPreferences.getObject("user", User.class);
     5.存取列表对象
    https://github.com/fWX228941/android-complex-preferences

    * */
    public void putObject(String key,Object object){
        if (null == object){
            /*参数不正确，为空，抛出异常*/
            throw  new IllegalArgumentException("object is null");
        }
        if (TextUtils.isEmpty(key)){
            throw new IllegalArgumentException("key is empty");
        }
        editor.putString(key, GSON.toJson(object));
        editor.apply();
    }
    public <T> T getObject (String key,Class<T> cls){
        String gson = preferences.getString(key,null);
        if (gson == null) {
            return null;
        } else {
            try{
                return GSON.fromJson(gson, cls);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }
    }
    /*
    * ArrayList<Person> usersWhoWon = new ArrayList<Person>();
    * PreferenceUtil complexPreferences = PreferenceUtil.getComplexPreferences(this, "mypref");;
      complexPreferences.putListObject("allWinners", usersWhoWon);
      为什么不能使用  这两个方法都是存在问题的
      这就是一个键值对，
      如果是同一个key 那么就覆盖了，
      如果是不同则添加
      存放也就是更新
    *
    * */
    public <T> void putListObject(String key, List<T> objArray){
        if (TextUtils.isEmpty(key)){
            throw new IllegalArgumentException("key is empty");
        }
        List<String> objStrings = new ArrayList<String>();
        for(T obj : objArray){
            objStrings.add(GSON.toJson(obj));
        }
        //字符串数组化 让指定的符号组加入到分隔符字符序列中并组成一个字符串
        String[] myStringList = objStrings.toArray(new String[objStrings.size()]);
//        Log.d("PreferenceUtil", "myStringList:" + myStringList);
        editor.putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public <T> ArrayList<T> getListObject(String key, Class<?> mClass){
            Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key,
                ""), "‚‗‚")));
//        Log.d("PreferenceUtil", "objStrings:" + objStrings);
//        Log.d("PreferenceUtil", "objStrings.toArray():" + objStrings.toArray());
        ArrayList<T> objects =  new ArrayList<T>();
        for(String jObjString : objStrings){
            T value  = (T) gson.fromJson(jObjString,  mClass);
            objects.add(value);
        }
        return objects;
    }
    /**
     * 布尔类型的设置值     需要分门别类的自定义文件
     * @param context
     * @param key  String
     * @param defaultValue  boolean
     * @param filename  文件名
     * @return boolean
     */
    public static boolean getSharedPreference(Context context, String key, boolean defaultValue,String filename) {
        SharedPreferences prefs = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }
    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public static void setSharedPreference(Context context, String key, boolean value,String filename) {
        SharedPreferences prefs = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }
    public void putInt(String key, int value) {
        editor.putInt(key, value).apply();
    }
    /*整形*/
    public static int getSharedPreference(Context context, String key, int defaultValue,String filename) {
        SharedPreferences prefs = context.getSharedPreferences(getFileName(filename),Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultValue);
    }

    public static void setSharedPreference(Context context, String key, int value,String filename) {
        SharedPreferences prefs = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static void setSharedPreference(Context context, String key, long value,String filename) {
        SharedPreferences prefs = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }
    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }
    public void putString(String key, String value) {
        editor.putString(key, value).apply();
    }
    public Long getLong(String key, Long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }
    public void putLong(String key, Long value) {
        editor.putLong(key, value).apply();
    }
    /*字符串类型*/
    public static String getSharedPreference(Context context, String key, String defaultValue,String filename) {
        SharedPreferences prefs = context.getSharedPreferences(getFileName(filename),Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }

    public static void setSharedPreference(Context context, String key, String value,String filename) {

        SharedPreferences prefs = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }
    /*
        依据文件名键值获取自定义文件名
    *   命名规范：key_filename : location
    *            fileName:com.android.haobanyi_location
    *   通用的存放在preferences中
    *
    * */
    private static String getFileName(String key_filename) {
        fileName = preferenceFileName.get(key_filename);
//        Log.d("fumin","fileName:"+fileName);
//        Log.d("fumin"," preferenceFileName.get(key_filename):"+ preferenceFileName.get(key_filename));
        if (null == preferenceFileName.get(key_filename)){
            preferenceFileName.put(key_filename,"com.android.haobanyi_"+key_filename);
            fileName = preferenceFileName.get(key_filename);
//            Log.d("fumin","fileName+1:"+fileName);
        }
        return fileName;
    }
    /**
     * SharedPreferences存储本地上的的文件名字
     */
//    private static String getSpName(Context context) {
//        return context.getPackageName() + "_preferences";
//    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void putAndApply(Context context, String key, Object object,String filename) {
        SharedPreferences sp = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * 泛型机制
     */
    public static Object get(Context context, String key, Object defaultObject,String filename) {
        SharedPreferences sp = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return null;
        }
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key,String filename) {
        SharedPreferences sp = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /*
    *
    * 删除一行数据。
    * */
    public void removeItem(String key) {
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }
    public  void clear() {
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }
    public boolean contains(String key) {

        return preferences.contains(key);
    }
    /**
     * 清除所有数据
     */
    public static void clear(Context context,String filename) {
        SharedPreferences sp = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(Context context, String key,String filename) {
        SharedPreferences sp = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context,String filename) {
        SharedPreferences sp = context.getSharedPreferences(getFileName(filename), Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     * 兼容类  很多兼容性问题，其实这也是个问题
     * @author zhy
     */
    private static class SharedPreferencesCompat {

        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException expected) {
            } catch (IllegalAccessException expected) {
            } catch (InvocationTargetException expected) {
            }
            editor.commit();
        }
    }

}
