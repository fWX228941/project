package com.android.haobanyi;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.AppAction;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

/**
 * Created by fWX228941 on 2016/5/17.
 *
 * @作者: 付敏
 * @创建日期：2016/05/17
 * @邮箱：466566941@qq.com
 * @当前文件描述：Fragment的基类,使用v4，兼容更低的版本
 * 参考设计：https://github.com/bingoogolapple/BGARefreshLayout-Android
 */
public abstract class BaseFragment extends Fragment {
    //1.标识
    protected String TAG;
    //2.Application
    protected BaseApplication application;
    //3.布局视图
    protected View contentView;
    //4.Activity
    protected BaseActivity activity;
    // 5.核心层的Action实例
    protected AppAction appAction;
    protected long requestCount = 5;
    //5.实现延迟懒加载，在领先于所有fragment生命周期之前调用

    //进度条
    protected SpotsDialog progressDialog;


    //全局性的缓存
    protected PreferenceUtil basePreference;

    //是否登录
    protected boolean isLogin =false;//减少重复创建对象

    //是否更新token成功
    protected boolean  hasTokenRefreshed = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 在可见与不可见之间做处理，类似Activity.Resume 和Activity.onPause 方法，fragment生命周期中虽然也是有的，但其实是在执行activity的同名方法，这个需要子类手动触发调用
        if (isVisibleToUser){
            onUserVisible();//当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
        } else {
            // 进行小量的初始化操作，比如通过AIDL调用远程服务，
            onUserInvisable();
        }

    }
    /*
    *
    * 说明：需要子类来自定义延迟加载的逻辑，当fragment 对用户可见时，会调用该方法
    * 适用场景：
    *   1.开启线程与服务器交互，获取并加载服务器和网络数据，更新界面，显示数据加载，数据统计
    *   2.数据加载，可见时在去加载数据，不可见时不加载
    *   3.ViewPager+页面加载
    *   4.配合缓存和刷新机制，只有当用户下拉刷新的时候重新从服务器获取数据然后覆盖以前保存的缓存文件，然后再去加载本地缓存数据，以实现界面显示
    * 优点：
    *   1.避免在activity刚刚创建的时候就需要大量的初始化资源，只有当我们切换到当前fragment的时候才初始化。有些需要提前预加载，有些是需要等到用时 才加载，
    *   这需要分情况考虑清楚
    * 设计参考：http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/1021/1813.html  添加标志位，防止出现空指针异常
    * */
    protected void onUserVisible(){lazyload();}
    /*谨记：系统数据是不能够初始化的*/
    protected abstract void lazyload();//抽象类的抽象方法没有方法体,而非抽象方法必须有方法体
    protected void onUserInvisable(){};

    //6.fragment与其宿主activity进行绑定
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.TAG = this.getClass().getSimpleName();
        this.application = BaseApplication.getApplication();
        // 当回调函数的入参无法修改类型的时候，只能够通过强制类型转换来达到目的
        this.activity = (BaseActivity) activity;
        this.appAction = application.getAppAction();
    }
    //7.加载fragment视图

    //8.查找View 控件
    protected <T extends View> T getViewById(int id){
        return (T) contentView.findViewById(id);
    }
    //9.视图创建完以后，进行各种初始化，逻辑处理
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null == contentView){
            int layoutResId = setLayout();
            this.contentView = LayoutInflater.from(application).inflate(layoutResId,null);
        } else {
            ViewGroup parent = (ViewGroup) contentView.getParent();
            if (parent != null) {
                parent.removeView(contentView);
            }
        }

        Log.d("CategoryListFragment", "contentView:" + contentView);
        ButterKnife.bind(this, contentView);
                /*更新时间戳*/
        NTPTime.getInstance().getCurrentTime();
        //进度条
        progressDialog =  new SpotsDialog(getActivity());
        return contentView;
    }
    private void refreshToken() {
        basePreference = PreferenceUtil.getSharedPreference(getActivity(), "preferences");
        isLogin = basePreference.getBoolean(Constants.ISLOGIN, false);
        if (isLogin){
            //超过过期时间，更新token
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 2;//实时初始化
                getToken();
            }
        }
    }

    private void getToken() {
        appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                hasTokenRefreshed = true;
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount>0) {
                    requestCount--;
                    getToken();
                }else {
                    hasTokenRefreshed = false;
                }

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshToken();
        // 10.加载完成以后出现，
        afterCreate(savedInstanceState);//当提示出现unreachable statement时，说明是有return 语句在此之前就执行了返回操作，使之不可达
    }

    protected abstract int setLayout();
    protected abstract void afterCreate(Bundle savedInstanceState);

    //13.加载提示框
    /*
    protected void showLoadingDialog() {
        activity.showLoadingDialog();
    }
    protected void dismissLoadingDialog() {
        if (isVisible()) {
            activity.dismissLoadingDialog();
        }
    }
    *
    *
    * */

}
