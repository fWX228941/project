package com.android.haobanyi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alorma.timeline.RoundTimelineView;
import com.alorma.timeline.TimelineView;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.activity.shopping.shopping.ConformOrderActivity;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.adapter.shopping.ShoppingAttrAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.post.AttrIds;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartChildBean;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartParentBean;
import com.android.haobanyi.model.bean.shopping.order.Carts;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.NetWorkUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ActionSheet;
import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by fWX228941 on 2016/6/7.
 *
 * @作者: 付敏
 * @创建日期：2016/06/07
 * @邮箱：466566941@qq.com
 * @当前文件描述：购物车适配器 更多参考：
 * http://www.open-open.com/lib/view/open1406014566679.html
 * http://blog.sina.com.cn/s/blog_b37dcd9701017s61.html
 *
 * 这个旧版的逻辑比较复杂，写个简单的看看
 *
 */
public class ShoppingExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "fumin_test";
    //构造函数中传递的三个参数
    List<Map<String, Object>> parentMapList;
    List<List<Map<String, Object>>> childMapList_list;
    Context context;
    /*因为状态的变更而导致的字符串变化*/
    public static final String EDITING = "编辑";
    public static final String FINISH_EDITING = "完成";
    //其他服务
    private ShoppingAttrAdapter shopAttradapter  = new ShoppingAttrAdapter(context, null);
    private DialogPlus shopAttrDialog; //弹框
    private ShoppingCartChildBean.ShopAttrListBean shopattrbean = null;

    List<Carts.Cartbean > list= new ArrayList<Carts.Cartbean>();//用于删除全部服务
    List<ShoppingCartChildBean.ShopAttrListBean>  ShopAttrList = new ArrayList<ShoppingCartChildBean.ShopAttrListBean>();
    ArrayList<Long> ShopAttrListIds =  new ArrayList<Long>();
    protected long requestCount = 35;
    SweetAlertDialog pDialog;// 弹框
    //有弹框

    OnPopUpWindowsListener onPopUpWindowsListener;
    public interface OnPopUpWindowsListener{
        void onPopUp(boolean isHasPopWindws);
    }

    public void setOnPopUpWindowsListener(OnPopUpWindowsListener onPopUpWindowsListener) {
        this.onPopUpWindowsListener = onPopUpWindowsListener;
    }

    //更新刷新当前购物车
    OnRefreshServerListener onRefreshServerListener;
    public interface OnRefreshServerListener{
        void onRefreshServer();
    }
    public void setOnRefreshServerListener(OnRefreshServerListener onRefreshServerListener){
        this.onRefreshServerListener = onRefreshServerListener;
    }



    //监听器：编辑状态更新触发的操作,一个实例，一个注册set方法
    OnEditingStateChangedListener onEditingStateChangedListener;


    public interface OnEditingStateChangedListener{
        void onEditingTvChange(boolean isEditingAll);
    }
    public void setOnEditingStateChangedListener(OnEditingStateChangedListener onEditingStateChangedListener) {
        this.onEditingStateChangedListener = onEditingStateChangedListener;
    }
    //当勾选复选框时，触发价格数量的界面更新
    OnPriceCountChangeListener onPriceCountChangeListener;
    public interface OnPriceCountChangeListener {
        void onPriceCountChange(int totalCount, String totalPrice);
    }
    public void setOnPriceCountChangeListener(OnPriceCountChangeListener onPriceCountChangeListener) {
        this.onPriceCountChangeListener = onPriceCountChangeListener;
    }

    // 多选框状态的变化
    OnCheckingStateChangeListener onCheckingStateChangeListener;
    public interface OnCheckingStateChangeListener {
        void onCheckedBoxNeedChange(boolean allParentIsChecked);
    }
    public void setOnCheckingStateChangeListener(OnCheckingStateChangeListener onCheckingStateChangeListener) {
        this.onCheckingStateChangeListener = onCheckingStateChangeListener;
    }

    // 删除，检查
    OnCheckHasGoodsListener onCheckHasGoodsListener;
    public void setOnCheckHasGoodsListener(OnCheckHasGoodsListener onCheckHasGoodsListener) {
        this.onCheckHasGoodsListener = onCheckHasGoodsListener;
    }
    public interface OnCheckHasGoodsListener {
        void onCheckHasGoods(boolean isHasGoods);
    }

    // 触发登录
    OnLoginForTokenListener onLoginForTokenListener;
    public void setOnLoginForTokenListener(OnLoginForTokenListener onLoginForTokenListener){
        this.onLoginForTokenListener = onLoginForTokenListener;
    }
    public interface OnLoginForTokenListener{
        void onLoginForToken();
    }



    public ShoppingExpandableListAdapter(Context context, List<Map<String, Object>> parentMapList,
                                         List<List<Map<String, Object>>> childMapList_list) {
        this.parentMapList = parentMapList;
        this.childMapList_list = childMapList_list;
        this.context = context;
    }

    //获取当前父item的数量，分组数
    @Override
    public int getGroupCount() {
        return parentMapList.size();
    }

    //根据组索引，获取当前父item下的子item的个数，即取得指定分组的子元素个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childMapList_list.get(groupPosition).size();
    }

    //get(position):Returns the element at the specified location in this {@code List}. 获取对象
    //size:Returns the number of elements in this {@code List}. list大小
    @Override
    public Object getGroup(int groupPosition) {
        return parentMapList.get(groupPosition);
    }

    //得到子item需要关联的数据，根据组索引和item索引，取得与指定分组，指定子项目关联的数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childMapList_list.get(groupPosition).get(childPosition);
    }

    //得到父item的ID 。取得指定分组的ID，该组ID必须是全局唯一的，【在分组以及子项目中也要保持唯一性】
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //得到子item的ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 是否指定分组视图及其子视图的ID对应的后台数据改变也会保持成功
    /*
    * 如果为TRUE，意味着相同的ID永远引用相同的对象,组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
    * 更多参考：http://www.open-open.com/lib/view/open1406014566679.html
    * http://blog.sina.com.cn/s/blog_b37dcd9701017s61.html
    * */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /* // 是否选中指定位置上的子元素。指定位置的子视图是否可选择.
    * @param: groupPosition 组位置（该组内部含有这个子元素）
    * @param: childPosition  子元素位置
    * @return: 是否选中子元素
    * */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        Log.d(TAG, "调用isChildSelectable");
        return true;
    }

    /* //获取显示指定组的视图对象,返回指定组的视图对象
    *  @params:
    *  groupPosition 组位置（决定返回哪个视图）
    *  isExpanded    该组是展开状态还是伸缩状态
    *  convertView  重用已有的视图对象。注意：在使用前你应该检查一下这个视图对象是否非空并且这个对象的类型是否合适。如果该对象不能被转换并显示正确的数据，这个方法就会调用getGroupView(int,
    *  boolean, View, ViewGroup)来创建一个视图(View)对象.
    *  parent   返回的视图对象始终依附于的视图组。
    *  逻辑：
    *  1.先子控件的事件监听
    *  2.再当前item的事件监听
    * */
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d(TAG, "调用getGroupView");
        final GroupViewHolder groupViewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_parent_layout, null);
            groupViewHolder = new GroupViewHolder();
            /*View.getTag() 和View.setTag()用于把查找到的View给缓存起来，方便重用，不用在重新构建View,利用系统中缓存的VIEW，提高效率
            * 设置获取标签，用于区分相似的View，可能执行相同的逻辑
            * */
            groupViewHolder.tv_title_parent = (TextView) convertView
                    .findViewById(R.id.tv_title_parent);
            groupViewHolder.id_iv_logo_shop = (ImageView) convertView
                    .findViewById(R.id.id_iv_logo_shop);
            groupViewHolder.id_tv_edit = (TextView) convertView
                    .findViewById(R.id.id_tv_edit);
            groupViewHolder.id_cb_select_parent = (CheckBox) convertView
                    .findViewById(R.id.id_cb_select_parent);
            groupViewHolder.idTvGetcoupon = (TextView) convertView
                    .findViewById(R.id.id_tv_getcoupon);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        //01.groupItem 中商店名字的添加，以及编辑-完成 状态的切换。
        /*
        *  从对象中获取需要的属性值，并设置到控件中，最核心的还是数据源中的单个数据对象，用一个position来标识
        *  避免混乱：
        *  1.由position来唯一标识，只取一个对象实例
        *  2.从对象实例中获取的属性，利用关键字final来修饰下，避免值被无端修改
        *  
        *   java.lang.ClassCastException: com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartParentBean 
        *   cannot be cast to com.android.haobanyi.model.bean.shopping.ShoppingParentItemBean
        *
        * */
        Log.d("ShoppingExpandableListA", "groupPosition:" + groupPosition);
        final ShoppingCartParentBean shoppingParentItemBean  = (ShoppingCartParentBean) parentMapList.get(groupPosition).get("parentName");//标识每一条对象 ，需要转型下
        Glide.with(context)
                .load(shoppingParentItemBean.getShopLogo())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.shop_icon)// 也就是网络加载失败时出现的图片
                .into(groupViewHolder.id_iv_logo_shop);


        final String parentName = shoppingParentItemBean.getName(); //空引用
        groupViewHolder.tv_title_parent.setText(parentName);

        //02.根据对象的编辑状态来更改文本视图的文字显示,同时设置点击事件
        if (shoppingParentItemBean.isEditing()){
            groupViewHolder.id_tv_edit.setText(FINISH_EDITING);
        } else {
            groupViewHolder.id_tv_edit.setText(EDITING);
        }
        /*点击完成的时候最好是刷新一遍
        *
        * */
        groupViewHolder.id_tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     /*这个端逻辑有点不明觉厉*/
                    String text = "";
                    TextView textView = null;
                    if (v instanceof TextView) {
                        textView = (TextView) v;
                    }
                    Log.d("ShoppingExpandableListA", text);
                    Log.d("ShoppingExpandableListA", "textView:" + textView);
                    textView.setText(text);
                    //01.连接activity界面与适配器的桥梁就是javabean数据源对象，所以通过实时更新javaBean对象的关联状态值，最后根据状态值来实现通信
                    changeEditingState(groupPosition); // 当用户点击不同的groupView 实际上是在传递不同的position值
                /*
                * 逻辑：
                * 02.因为涉及到更新视图，需要主线程activity来操作，视图变更需要利用监听器来分流，向客户端提供一个设置监听器的方法，参数传递的就是借口对象监听器
                * 直接传递一个匿名的实例化监听器对象即可
                * 03.每一个监听，实际上本质就是一个驱动事件流，监听器中的方法体就是一个载体，客户端只管注册，一旦有触发，被adapter触发就会调用客户端，客户端执行既定的逻辑
                * 04.更新完状态之后，回调界面更新视图
                *
                * */
                    onEditingStateChangedListener.onEditingTvChange(isEditingAll());

            }
        });
        //02.点击领券，弹出优惠券对话框
        groupViewHolder.idTvGetcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showLong("弹出优惠券对话框，待开发中");
            }
        });
        groupViewHolder.tv_title_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestCount = 45;
                gotoShop(shoppingParentItemBean.getShopID());

            }
        });
        //03.对于复选框，编辑框都是先设置初始状态，当然是依旧一定的状态量来进行设置，复选框最重要的是勾选状态
        groupViewHolder.id_cb_select_parent.setChecked(shoppingParentItemBean.isChecked());
        groupViewHolder.id_cb_select_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是选中状态，则取消勾选，如果是非选中状态，则勾选
                if (shoppingParentItemBean.isChecked()){
                    cancelCartShopItem(shoppingParentItemBean, groupPosition);

                } else {

                    selectCartShopItem(shoppingParentItemBean, groupPosition);
                }

            }
        });

        return convertView;
    }

    private void selectCartShopItem(final ShoppingCartParentBean shoppingParentItemBean, final int groupPosition) {
        BaseApplication.getApplication().getAppAction().selectCartShopItem(shoppingParentItemBean.getShopID(), new ActionCallbackListener<String>() {

            @Override
            public void onSuccess(String totalPrice) {
                changeCheckingState(groupPosition,totalPrice);
                onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0){
                    requestCount--;
                    selectCartShopItem(shoppingParentItemBean, groupPosition);
                }else if ("996".equals(errorCode)){
                    onLoginForTokenListener.onLoginForToken();

                }else if ("003".equals(errorCode)){
                    changeCheckingState(false, groupPosition); //不变
                    ToastUtil.networkUnavailable();
                }else{
                    changeCheckingState(false,groupPosition); //不变
                    ToastUtil.showErrorMessage(errorMessage, errorCode);
                }
            }
        });
    }

    private void cancelCartShopItem(final ShoppingCartParentBean shoppingParentItemBean, final int groupPosition) {
        BaseApplication.getApplication().getAppAction().cancelCartShopItem(shoppingParentItemBean.getShopID(), new
                ActionCallbackListener<String>() {
                    @Override
                    public void onSuccess(String totalPrice) {
                        changeCheckingState(groupPosition, totalPrice);
                        onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode)&& requestCount > 0) {
                            requestCount--;
                            cancelCartShopItem(shoppingParentItemBean, groupPosition);
                        } else if ("996".equals(errorCode)) {
                            //触发登录
                            onLoginForTokenListener.onLoginForToken();
                        }else if ("003".equals(errorCode)){
                            changeCheckingState(true, groupPosition);//如果失败，那就不变
                            ToastUtil.networkUnavailable();
                        }else{
                            changeCheckingState(true, groupPosition);//如果失败，那就不变
                            ToastUtil.showErrorMessage(errorMessage, errorCode);
                        }

                    }
                });
    }



    private void gotoShop(final long shopId) {

        BaseApplication.getApplication().getAppAction().getShopwithoutId(shopId, new
                ActionCallbackDoubleListener<ShopBean, List<SortDataBean>>() {

            @Override
            public void onSuccess(ShopBean data, List<SortDataBean> data02) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.SHOP_ID, data.getShopID());
                bundle.putInt(Constants.STATE_FROM_SRGA, 01);
                IntentUtil.gotoActivityWithData(context, StorePageActivity.class, bundle, false);

            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if ("998".equals(errorEvent)&& requestCount > 0) {
                    requestCount--;
                    gotoShop(shopId);
                } else if ("996".equals(errorEvent)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    onLoginForTokenListener.onLoginForToken();

                }else if ("003".equals(errorEvent)){
                    ToastUtil.networkUnavailable();
                }else{
                    ToastUtil.showErrorMessage(message, errorEvent);
                }


            }
        });

    }
    /*
    * 当我勾选一个一级标题父item时，所有关联的二级子item列表项都会默认勾选，或者不勾选
    * 这里采用了两种方式：
    * 01.一种是传递当前状态和position位置
    * 02.一种是仅仅传递position
    * */
    public void changeCheckingState(int groupPosition,String totalPrice) {
        ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(groupPosition).get("parentName");
        final boolean isChecked = !shoppingParentItemBean.isChecked();
        shoppingParentItemBean.setIsChecked(isChecked);

        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
            shoppingChildItemBean.setIsChecked(isChecked);
        }
        notifyDataSetChanged();//调用一次getView，回调更新界面，使子item项处于选中和未选中两种状态
        //第二个回调是：价格计算完成以后，把价格总计价值传递给主视图进行界面更新
        countPrice(totalPrice);
    }
    public void changeCheckingState(boolean isChecked,int groupPosition) {
        ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(groupPosition).get("parentName");
        shoppingParentItemBean.setIsChecked(isChecked);
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
            shoppingChildItemBean.setIsChecked(isChecked);
        }
        notifyDataSetChanged();//调用一次getView，回调更新界面，使子item项处于选中和未选中两种状态
        //第二个回调是：价格计算完成以后，把价格总计价值传递给主视图进行界面更新
    }
    public void changeEditingState(int groupPosition) {
        /*
        * 通过一个编辑状态的变化，同时变更groupItem和childrenItem ，
        * 无论是视图的变更还是文字的更改，只需要一个状态就可以解决，不过这个状态要实时正确。
        * 点击编辑后
        *
        * */
        //01.获取当前是否编辑状态值，传递给父控件,当期对于父视图而言，当前与父视图的状态为标识量
        ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(groupPosition).get("parentName");
        final boolean isEditing = !shoppingParentItemBean.isEditing();
        Log.d(TAG, "isEditing:" + isEditing);
        shoppingParentItemBean.setIsEditing(isEditing);

       //02.同时更新下一级的关联子列表编辑状态
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        Log.d(TAG, "childMapList.size():" + childMapList.size());
        for (int j = childMapList.size()-1; j >=0; j--) {
            Log.d(TAG, "j:" + j);
            ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
            shoppingChildItemBean.setIsEditing(isEditing);
        }

        //03.只要javaBean 也就是数据源中的任何属性，也就是那些传递的参数发生变更，都要实时的调用notifyDataSetChanged ,这样会再次刷新，调用一次getView方法，监听方法是独立的部分，会被事件流忽略掉
        notifyDataSetChanged();
    }

    //单个产品列表 父item只要一个是编辑状态，那么其产品列表的视图都应该更改，如果定位呢！ 不是因该传position i 和编辑状态么？
    public boolean isEditingAll() {
        for (int i = 0; i < parentMapList.size(); i++) {
            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get("parentName");
            if (shoppingParentItemBean.isEditing()) {
                return true;//如果有一个是编辑状态  就true
            }
        }
        return false;
    }
    //数量自己计算，价格就用服务器传递过来的，[数量就是服务量，服务器没有传递这个值，得自己算]
    public void countPrice(String totalPrice) {
        int totalCount = 0;
        //遍历用户在每个商店中购买的每个产品的数量,数量自己计算，价格就用服务器传递过来的
        for (int i = 0; i < parentMapList.size(); i++) {
            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                if (shoppingChildItemBean.isChecked()) {
                    totalCount++;//单品多数量只记1，这个规则是否需要修改，需要商榷
                }
            }
        }
        //触发回调，更新界面，这样来实现 界面与适配器之间的通信，这个应该就是最传统的方式了
        onPriceCountChangeListener.onPriceCountChange(totalCount, totalPrice);
    }



    /*总全选框*/
    public boolean isCheckingAll() {
        for (int i = 0; i < parentMapList.size(); i++) {
            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get("parentName");
            if (!shoppingParentItemBean.isChecked()) {
                return false;//如果有一个没选择  就false
            }
        }
        return true;
    }



    /* //指定位置上的子元素返回的视图对象
    *  @params:
    *  groupPosition  组位置（该组内部含有子元素）
    *  childPosition   子元素位置（决定返回哪个视图）
    *  isLastChild    子元素是否处于组中的最后一个
    *  套餐说白了就是几个单独的产品拼接而成，只是套餐的一些参数要保持一直罢了
    *
    * */
    @SuppressLint("StringFormatMatches")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup
            parent) {
        Log.d(TAG, "调用getChildView");
        ChildViewHolder childViewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_child_layout, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv_items_child = (TextView) convertView
                    .findViewById(R.id.tv_items_child);
            childViewHolder.id_cb_select_child = (CheckBox) convertView
                    .findViewById(R.id.id_cb_select_child);
            childViewHolder.timeline = (RoundTimelineView) convertView
                    .findViewById(R.id.timeline);
            childViewHolder.id_ll_normal = (LinearLayout) convertView
                    .findViewById(R.id.id_ll_normal);
            childViewHolder.id_ll_edtoring = (LinearLayout) convertView
                    .findViewById(R.id.id_ll_edtoring);
            childViewHolder.other_service = (RelativeLayout) convertView
                    .findViewById(R.id.other_service);
            childViewHolder.id_iv_logo = (ImageView) convertView
                    .findViewById(R.id.id_iv_logo);
            //常规下：
            childViewHolder.tv_items_child_desc = (TextView) convertView
                    .findViewById(R.id.tv_items_child_desc);
            childViewHolder.id_tv_discount_price = (TextView) convertView
                    .findViewById(R.id.id_tv_discount_price);
            childViewHolder.id_tv_count = (TextView) convertView
                    .findViewById(R.id.id_tv_count);
            //编辑下：
            childViewHolder.id_iv_reduce = (ImageView) convertView
                    .findViewById(R.id.id_iv_reduce);
            childViewHolder.id_iv_add = (ImageView) convertView
                    .findViewById(R.id.id_iv_add);
            childViewHolder.ll_change_num = (RelativeLayout) convertView
                    .findViewById(R.id.ll_change_num);
            childViewHolder.id_tv_count_now = (TextView) convertView
                    .findViewById(R.id.id_tv_count_now);
            childViewHolder.id_tv_des_now = (TextView) convertView
                    .findViewById(R.id.id_tv_des_now);
            childViewHolder.id_iv_list = (ImageView) convertView
                    .findViewById(R.id.id_iv_list);
            childViewHolder.id_tv_goods_star = (TextView) convertView
                    .findViewById(R.id.id_tv_goods_star);
            childViewHolder.id_tv_goods_delete = (TextView) convertView
                    .findViewById(R.id.id_tv_goods_delete);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        // 获取childJavaBean对象，并且初始化当前childItem视图，分别为编辑状态和完成状态，这个不是而选一，而是同时出现，只是部分被隐藏了
        final ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList_list.get(groupPosition).get(childPosition).get("childName");
        /*id_iv_logo 产品logo childViewHolder.id_iv_logo*/
        Glide.with(context)
                .load(shoppingChildItemBean.getImageLogo())
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)// 也就是网络加载失败时出现的图片
                .into(childViewHolder.id_iv_logo);

        //01.完成状态：字符串格式化：http://blog.csdn.net/lonely_fireworks/article/details/7962171/
        childViewHolder.tv_items_child.setText(shoppingChildItemBean.getName());// 服务名
        // 价格是变化的，受到编辑状态
        childViewHolder.id_tv_discount_price.setText(String.format(context.getString(R.string.price), shoppingChildItemBean.getPrice())); //单价
        childViewHolder.id_tv_count.setText(String.format(context.getString(R.string.product_count), shoppingChildItemBean.getCount())); // 用string.xml还有一个方面就是为了格式户

        childViewHolder.id_tv_count_now.setText(String.valueOf(shoppingChildItemBean.getCount()));//当前价格变更



        if (shoppingChildItemBean.isGroupService()){
            //如果是套餐
            int type = shoppingChildItemBean.getType();
            if (type==TimelineView.TYPE_START){
                childViewHolder.timeline.setVisibility(View.VISIBLE);
                childViewHolder.id_cb_select_child.setVisibility(View.VISIBLE);
                childViewHolder.timeline.setTimelineType(TimelineView.TYPE_START);
                childViewHolder.id_tv_goods_delete.setVisibility(View.VISIBLE);
                childViewHolder.ll_change_num.setVisibility(View.VISIBLE);
            }else if (type==TimelineView.TYPE_MIDDLE){
                childViewHolder.timeline.setVisibility(View.VISIBLE);
                childViewHolder.id_cb_select_child.setVisibility(View.INVISIBLE);
                childViewHolder.timeline.setTimelineType(TimelineView.TYPE_MIDDLE);
                childViewHolder.id_tv_goods_delete.setVisibility(View.INVISIBLE);
                childViewHolder.ll_change_num.setVisibility(View.INVISIBLE);
            }else if (type== TimelineView.TYPE_END){
                childViewHolder.timeline.setVisibility(View.VISIBLE);
                childViewHolder.id_cb_select_child.setVisibility(View.INVISIBLE);
                childViewHolder.timeline.setTimelineType(TimelineView.TYPE_END);
                childViewHolder.id_tv_goods_delete.setVisibility(View.INVISIBLE);
                childViewHolder.ll_change_num.setVisibility(View.INVISIBLE);
            }

        } else {
            childViewHolder.timeline.setVisibility(View.GONE);
            childViewHolder.id_cb_select_child.setVisibility(View.VISIBLE);

        }


        //02.编辑状态：真正的价格   final List<ShoppingCartChildBean.ShopAttrListBean>
        ShopAttrList = shoppingChildItemBean.getShopAttrList();//数据源变更了
        if (null==ShopAttrList || ShopAttrList.isEmpty()){
            childViewHolder.tv_items_child_desc.setVisibility(View.INVISIBLE);
            childViewHolder.other_service.setVisibility(View.INVISIBLE);
        } else {
            childViewHolder.tv_items_child_desc.setVisibility(View.VISIBLE);
            childViewHolder.other_service.setVisibility(View.VISIBLE);
            StringBuffer sb = new StringBuffer(256);
            for (int i = 0;i<ShopAttrList.size();i++){
                if (ShopAttrList.get(i).isSelected()){
                    sb.append("["+ShopAttrList.get(i).getAttrName() + "] ");
                }
            }
            if (TextUtils.isEmpty(sb.toString())){
                childViewHolder.tv_items_child_desc.setText("请选择其他服务");
            }else {
                childViewHolder.tv_items_child_desc.setText("已选服务："+sb.toString());
            }

            //childViewHolder.id_tv_des_now.setText(sb.toString());// 初始化的时候就不应该是这个样
        }
        /*可以选择其他服务内容other_service  来这设置下*/
              /*ShoppingAttrAdapter*/
        childViewHolder.other_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //因为被执行了多次，需要重新调用才是，保证是当时的，全局变量和局部变量还是得分清楚
                ShopAttrList = shoppingChildItemBean.getShopAttrList();
                shopAttradapter = new ShoppingAttrAdapter(context,ShopAttrList);
                Log.d(TAG, "ShopAttrList.isEmpty():" + ShopAttrList.isEmpty());
                if (null!=shopAttradapter|| !shopAttradapter.isEmpty()){
                    Holder holder = new GridHolder(ShopAttrList.size());
                    //点击弹框的一刹那
                    Log.d("kkkkkk", "11" + 11);
                    onPopUpWindowsListener.onPopUp(true);
                    showShopAttrDialog(holder,shoppingChildItemBean.getProductID(),shoppingChildItemBean.getGroupSellID());



                }
            }
        });






        //03.勾选框
        childViewHolder.id_cb_select_child.setChecked(shoppingChildItemBean.isChecked());
        childViewHolder.id_cb_select_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 逻辑：
                * 1.变化一：先更新状态
                * 2.变化二：当用户点击勾选框时，底视图中的合计和结算值均实时发生变化
                * 3.变化三：关联父勾选框，计算父勾选框中，只有其所有的子勾选框处于勾选状态时，其父勾选框才会被勾选
                * */


                requestCount = 35;
                final boolean nowBeanChecked = shoppingChildItemBean.isChecked();
                if (nowBeanChecked){
                    //现在是勾选状态
                    if (shoppingChildItemBean.isGroupService()){
                        cancelCartGroupItem(shoppingChildItemBean, groupPosition);
                    }else {
                        cancelCartItem(shoppingChildItemBean, groupPosition);
                    }

                } else {
                    if (shoppingChildItemBean.isGroupService()){
                        selectCartGroupItem(shoppingChildItemBean, groupPosition);
                    }else {
                        selectCartItem(shoppingChildItemBean, groupPosition);
                    }

                }

            }
        });

        //04.更新视图，分别为编辑状态下的视图和非编辑状态下的视图
        if (shoppingChildItemBean.isEditing()) {
            childViewHolder.id_ll_normal.setVisibility(View.GONE);
            childViewHolder.id_ll_edtoring.setVisibility(View.VISIBLE);
            //这个到时里面还得做一层判断
        } else {
            childViewHolder.id_ll_normal.setVisibility(View.VISIBLE);
            childViewHolder.id_ll_edtoring.setVisibility(View.GONE);
        }
        /*为什么套餐点击不了*/

        //05.服务数量减少
        childViewHolder.id_iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "减一");
                requestCount = 35;
                dealCartReduce(shoppingChildItemBean, groupPosition);
            }
        });

        //06.服务数量增多
        childViewHolder.id_iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "加一:" );
                requestCount = 35;
                dealCartAdd(shoppingChildItemBean, groupPosition);
            }
        });
        // 07.删除
        childViewHolder.id_tv_goods_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先点击弹框，按确定了，再来删除
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("确定要删除吗？")
                        .setConfirmText("删除")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                pDialog.dismissWithAnimation();

                                requestCount = 35;
                                // 这个地方需要与服务器交互一下
                                long GroupSellID = 0;
                                long ProductID = -1;
                                if (shoppingChildItemBean.isGroupService()) {
                                    Log.d(TAG, "删除套餐服务");
                                    GroupSellID = shoppingChildItemBean.getGroupSellID();
                                } else {
                                    Log.d(TAG, "删除非套餐服务");
                                    ProductID = shoppingChildItemBean.getProductID();
                                }

                                deleteCartSelectedItem(shoppingChildItemBean.isGroupService(),GroupSellID, ProductID, groupPosition, childPosition);

                            }
                        })
                        .setCancelText("取消")
                        .show();
                pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        notifyDataSetChanged();
                        pDialog.dismissWithAnimation();
                    }
                });





            }
        });

        childViewHolder.tv_items_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                        (groupPosition).get("parentName");
                gotoProduct(shoppingParentItemBean.getShopID(),shoppingChildItemBean.getProductID());
            }
        });

        return convertView;
    }
    /*.setFooter(R.layout.item_fragment_product_footer)  把这一项给去掉*/
    private void showShopAttrDialog(Holder holder, final long productID, final long groupSellID) {
        Log.d(TAG, "执行了");
        Log.d(TAG, "shopAttradapter.isEmpty():" + shopAttradapter.isEmpty());
        /*
        * 可以通过
        *.setInAnimation(R.anim.abc_fade_in)
.setOutAnimation(R.anim.abc_fade_out)  【设置动画效果】
        * */
        shopAttrDialog = DialogPlus.newDialog(context)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setAdapter(shopAttradapter)
                .setCancelable(false)
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
                        //用这个接口来处理后腿事件,
                        //先执行的宿主activity的onBack 回调，然后才监听这个
                        Log.d(TAG, "后退键执行");
                        Log.d("kkkkkk", "33" + 33);
                        onPopUpWindowsListener.onPopUp(false);
                        shopAttrDialog.dismiss();
                    }
                })
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("ShoppingServiceFragment", "position:" + position);
                        /*
                        * 每次都要处理后腿的事件
                        *
                        * */

                        shopattrbean = (ShoppingCartChildBean.ShopAttrListBean) shopAttradapter.getItem(position);
                        Log.d(TAG, "shopattrbean.isChanged():" + shopattrbean.isChanged());
                        if (shopattrbean.isSelected()) {
                            shopattrbean.setIsSelected(false);
                            shopAttradapter.notifyDataSetChanged();// 整个也是需要刷新的，最好刷新还是调用一下服务器

                        } else {
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
                                // 只有点击确定时，应该是传递变化的服务，只要是变化了的，才会
                                AttrIds.AttrIdsbean bean = null;
                                List<AttrIds.AttrIdsbean> list = new ArrayList<AttrIds.AttrIdsbean>();
                                Log.d(TAG, "ShopAttrList:" + ShopAttrList);
                                for (int i = 0; i < ShopAttrList.size(); i++) {
                                    ShoppingCartChildBean.ShopAttrListBean shopattrbean = (ShoppingCartChildBean
                                            .ShopAttrListBean) ShopAttrList.get(i);
                                    if (shopattrbean.isSelected()) {
                                        bean = new AttrIds.AttrIdsbean(shopattrbean.getShopAttrID());//传递处于选中状态的
                                        list.add(bean);
                                    }

                                }
                                Log.d(TAG, "list:" + list);
                                requestCount = 35;
                                onPopUpWindowsListener.onPopUp(false);//不是回退导致的弹框
                                Log.d("kkkkkk", "33" + 33);
                                selectCartOtherService(list, productID, groupSellID);
                                    break;

                        }
                    }
                })
                .create();
        shopAttrDialog.show();  //设置为不可滑动

    }
    /*
    *
    * 有一个奇葩问题就是明明已经调用成功了，结果还是显示我网络失败，这种情况不是一次出现了，先折中处理这种情况
    *
    *
    * */
    private void selectCartOtherService(final List<AttrIds.AttrIdsbean> list, final long productID, final long groupSellID) {
        BaseApplication.getApplication().getAppAction().selectCartOtherService(productID,
                groupSellID, list, new ActionCallbackListener<ResponseCode>() {


                    @Override
                    public void onSuccess(ResponseCode data) {
                        //只有真正变化了的id 才保留状态的 先不考虑这种情况
                        shopAttrDialog.dismiss();//选完以后干嘛！ 状态是要修改吗？，这个地方需要斟酌
                /*
                * 01。两种方式刷新，一种是重新调用获取购物车接口  【采用第一种】 因为还有一个状态的选择，这个不是很好处理，后退也猪样处理
                * 02.一种是把获取的数据，直接赋值，恢复到初始状态【先采用第二种吧】
                *
                * */
                        onRefreshServerListener.onRefreshServer();


                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        if ("998".equals(errorCode)&& requestCount > 0) {
                            requestCount--;
                            selectCartOtherService(list, productID, groupSellID);
                        } else if ("996".equals(errorCode)) {
                            shopAttrDialog.dismiss();
                            onLoginForTokenListener.onLoginForToken();
                        }/*else if ("003".equals(errorCode)){


                        }*/else{
                            if (NetWorkUtil.isNetworkAvailable(context) && "003".equals(errorCode)){
//                                ToastUtil.showErrorMessage(errorCode, errorMessage);

                                shopAttrDialog.dismiss();
                                onRefreshServerListener.onRefreshServer();
                            }else if (NetWorkUtil.isNetworkAvailable(context) && !"003".equals(errorCode)){
                                ToastUtil.showErrorMessage(errorCode, errorMessage);
                                shopAttrDialog.dismiss();
                            }else {
                                ToastUtil.networkUnavailable();
                                shopAttrDialog.dismiss();
                            }



                        }

                    }
                });
    }


    private void selectCartGroupItem(final ShoppingCartChildBean shoppingChildItemBean, final int groupPosition) {
        BaseApplication.getApplication().getAppAction().selectCartGroupItem(shoppingChildItemBean
                .getGroupSellID(), new ActionCallbackListener<String>() {


            @Override
            public void onSuccess(String totalPrice) {
                shoppingChildItemBean.setIsChecked(true);
                setRelatedGroupItem(shoppingChildItemBean.getGroupSellID(), true, groupPosition);
                //02
                boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean)
                        parentMapList.get
                                (groupPosition).get("parentName");
                shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                //03
                notifyDataSetChanged();
                //04.控制总checkedbox 接口
                onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());
                //05计算总价格
                countPrice(totalPrice);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0) {
                    requestCount--;
                    selectCartGroupItem(shoppingChildItemBean, groupPosition);
                } else if ("996".equals(errorCode)) {
                    onLoginForTokenListener.onLoginForToken();

                }else if ("003".equals(errorCode)){
                    ToastUtil.networkUnavailable();

                    shoppingChildItemBean.setIsChecked(false);  //请求失效 为什么没有生效
                    setRelatedGroupItem(shoppingChildItemBean.getGroupSellID(), false, groupPosition);
                    boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                    ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean)
                            parentMapList.get
                                    (groupPosition).get("parentName");
                    shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                    //03
                    notifyDataSetChanged();
                }else{
                    ToastUtil.showErrorMessage(errorMessage, errorCode);
                    shoppingChildItemBean.setIsChecked(false);  //请求失效 为什么没有生效
                    setRelatedGroupItem(shoppingChildItemBean.getGroupSellID(), false, groupPosition);
                    boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                    ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean)
                            parentMapList.get
                                    (groupPosition).get("parentName");
                    shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                    //03
                    notifyDataSetChanged();
                }

            }
        });
    }

    private void cancelCartGroupItem(final ShoppingCartChildBean shoppingChildItemBean, final int groupPosition) {
        BaseApplication.getApplication().getAppAction().cancelCartGroupItem(shoppingChildItemBean.getGroupSellID(),
                new ActionCallbackListener<String>() {


            @Override
            public void onSuccess(String totalPrice) {
                shoppingChildItemBean.setIsChecked(false);
                setRelatedGroupItem(shoppingChildItemBean.getGroupSellID(), false, groupPosition);
                //套餐相关的结果也是需要更新的
                //02
                boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                        (groupPosition).get("parentName");
                shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                //03
                notifyDataSetChanged();
                //04.控制总checkedbox 接口
                onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());
                //05计算总价格
                countPrice(totalPrice);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    cancelCartGroupItem(shoppingChildItemBean, groupPosition);
                } else if ("996".equals(errorCode)) {
                    onLoginForTokenListener.onLoginForToken();
                }else if ("003".equals(errorCode)){
                    ToastUtil.networkUnavailable();

                    shoppingChildItemBean.setIsChecked(true);
                    setRelatedGroupItem(shoppingChildItemBean.getGroupSellID(),true,groupPosition);
                    boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                    ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                            (groupPosition).get("parentName");
                    shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                    //03
                    notifyDataSetChanged();
                }else{
                    ToastUtil.showErrorMessage(errorMessage, errorCode);
                    shoppingChildItemBean.setIsChecked(true);
                    setRelatedGroupItem(shoppingChildItemBean.getGroupSellID(),true,groupPosition);
                    boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                    ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                            (groupPosition).get("parentName");
                    shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                    //03
                    notifyDataSetChanged();
                }
            }
        });
    }
    /*如果单个店铺有多个套餐呢！等测试产品出来了，再来考虑吧！*/
    private void setRelatedGroupItem(long groupSellID, boolean isGroupSelected, int groupPosition) {
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
            if (shoppingChildItemBean.getGroupSellID() == groupSellID ) {
                shoppingChildItemBean.setIsChecked(isGroupSelected);
            }
        }
    }

    private void deleteCartSelectedItem(final boolean isgroupService, final long groupSellID, final long productID, final int groupPosition, final int
            childPosition) {
        BaseApplication.getApplication().getAppAction().deleteCartSelectedItem(productID, groupSellID, new
                ActionCallbackListener<String>() {
                    @Override
                    public void onSuccess(String totalPrice) {
                        removeOneGood(isgroupService, groupSellID, groupPosition, childPosition, totalPrice);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode)&& requestCount > 0) {
                            requestCount--;
                            deleteCartSelectedItem(isgroupService, groupSellID, productID, groupPosition,
                                    childPosition);
                        } else if ("996".equals(errorCode)) {
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            onLoginForTokenListener.onLoginForToken();
                        }else if ("003".equals(errorCode)){
                            ToastUtil.networkUnavailable();
                        }else{
                            ToastUtil.showErrorMessage(errorMessage, errorCode);
                        }

                    }
                });
    }

    private void selectCartItem(final ShoppingCartChildBean shoppingChildItemBean, final int groupPosition) {
        BaseApplication.getApplication().getAppAction().selectCartItem(shoppingChildItemBean.getProductID(), new
                ActionCallbackListener<String>() {


                    @Override
                    public void onSuccess(String totalPrice) {
                        shoppingChildItemBean.setIsChecked(true);
                        //02
                        boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                        ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                                (groupPosition).get("parentName");
                        shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                        //03
                        notifyDataSetChanged();
                        //04.控制总checkedbox 接口
                        onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());
                        //05计算总价格
                        countPrice(totalPrice);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode)&& requestCount > 0) {
                            requestCount--;
                            selectCartItem(shoppingChildItemBean, groupPosition);
                        } else if ("996".equals(errorCode)) {
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            onLoginForTokenListener.onLoginForToken();

                        }else if ("003".equals(errorCode)){
                            ToastUtil.networkUnavailable();
                            shoppingChildItemBean.setIsChecked(false);  //请求失效 为什么没有生效
                            boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                                    (groupPosition).get("parentName");
                            shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                            //03
                            notifyDataSetChanged();
                        }else{
                            ToastUtil.showErrorMessage(errorCode, errorMessage);
                            shoppingChildItemBean.setIsChecked(false);  //请求失效 为什么没有生效
                            boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                                    (groupPosition).get("parentName");
                            shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                            //03
                            notifyDataSetChanged();
                        }

                    }
                });
    }

    private void cancelCartItem(final ShoppingCartChildBean shoppingChildItemBean, final int groupPosition) {
        BaseApplication.getApplication().getAppAction().cancelCartItem(shoppingChildItemBean.getProductID(), new
                ActionCallbackListener<String>() {


                    @Override
                    public void onSuccess(String totalPrice) {
                        shoppingChildItemBean.setIsChecked(false);
                        //02
                        boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                        ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                                (groupPosition).get("parentName");
                        shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                        //03
                        notifyDataSetChanged();
                        //04.控制总checkedbox 接口
                        onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());
                        //05计算总价格
                        countPrice(totalPrice);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode)&& requestCount > 0) {
                            requestCount--;
                            cancelCartItem(shoppingChildItemBean, groupPosition);
                        } else if ("996".equals(errorCode)) {
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            onLoginForTokenListener.onLoginForToken();

                        }else if ("003".equals(errorCode)){
                            ToastUtil.networkUnavailable();

                            shoppingChildItemBean.setIsChecked(true);
                            boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                                    (groupPosition).get("parentName");
                            shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                            //03
                            notifyDataSetChanged();
                        }else{
                            ToastUtil.showErrorMessage(errorCode, errorMessage);
                            shoppingChildItemBean.setIsChecked(true);
                            boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                                    (groupPosition).get("parentName");
                            shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                            //03
                            notifyDataSetChanged();
                        }


                    }
                });
    }

    /*跳转到服务页*/
    private void gotoProduct(final long ShopID, final long productID){
        requestCount = 45;
        getProduct(ShopID, productID);

    }

    private void getProduct(final long ShopID, final long productID) {
        BaseApplication.getApplication().getAppAction().getProduct01(productID, new
                ActionCallbackFivefoldListener<ProductDetailsBean, List<SatisfySendBean>,
                                        List<VouchersTemplateBean>, List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>>() {
                    @Override
                    public void onSuccess(ProductDetailsBean data, List<SatisfySendBean> data02,
                                          List<VouchersTemplateBean>
                                                  data03, List<ShopAttrBean> data04, List<DetailsBean.DataBean.CLBean> data05) {
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.PRODUCT_ID, productID);
                        bundle.putLong(Constants.SHOP_ID, data.getShopID());
                        IntentUtil.gotoActivityWithData(context, ShoppingRadioGroupActivity.class, bundle, false);

                    }

                    /*这个方法有必要统一吗？*/
                    @Override
                    public void onFailure(String errorEvent, String message) {
                        if ("998".equals(errorEvent)&& requestCount > 0) {
                            requestCount--;
                            gotoProduct(ShopID, productID);
                        } else if ("996".equals(errorEvent)) {
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            onLoginForTokenListener.onLoginForToken();
                        }else if ("003".equals(errorEvent)){
                            ToastUtil.showLong("请检查网络");
                        }else{
                            ToastUtil.showErrorMessage(message, errorEvent);
                        }
                    }
                });
    }

    /*
    * UpdateCartQuantity
    *UpdateCartGroupSellQuantity
    *
    * */
    public void dealCartAdd(final ShoppingCartChildBean shoppingChildItemBean, int groupPosition) {
        final int[] count = {shoppingChildItemBean.getCount()};
        if (shoppingChildItemBean.isGroupService()){
            updateCartGroupSellQuantity(shoppingChildItemBean, count,true,groupPosition);
        }else {
            updateCartQuantity(shoppingChildItemBean, count,true,groupPosition);
        }


    }


    public void dealCartReduce(final ShoppingCartChildBean shoppingChildItemBean,int groupPosition) {
        final int[] count = {shoppingChildItemBean.getCount()};
        if (count[0] == 1) {
            ToastUtil.showHintMessage("受不了了，宝贝不能再减少了哦！");
            return; //如果到1 了是无法点击的
        }
        if (shoppingChildItemBean.isGroupService()){
            updateCartGroupSellQuantity(shoppingChildItemBean, count, false,groupPosition);
        }else {
            updateCartQuantity(shoppingChildItemBean, count,false,groupPosition);
        }

    }

    private void updateCartQuantity(final ShoppingCartChildBean shoppingChildItemBean, final int[] count, final boolean isAdd, final int groupPosition) {
        Log.d(TAG, "count[0]:" + count[0]);
        int i = count[0];
        if (isAdd){
            i=i+1;
        }else {
            i=i-1;
        }
        BaseApplication.getApplication().getAppAction().updateCartQuantity(shoppingChildItemBean.getProductID(),
                i, new ActionCallbackListener<String>() {


            @Override
            public void onSuccess(String totalPrice) {
                if (isAdd){
                    count[0]++;
                }else {
                    count[0]--;
                }
                shoppingChildItemBean.setCount(count[0]);
                shoppingChildItemBean.setIsChecked(true);
                //02
                boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                        (groupPosition).get("parentName");
                shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                //03
                notifyDataSetChanged();
                //04.控制总checkedbox 接口
                onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());


                countPrice(totalPrice);  //这个就是重复的
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0) {
                    requestCount--;
                    if (isAdd){
                        updateCartQuantity(shoppingChildItemBean, count, true, groupPosition);
                    }else {
                        updateCartQuantity(shoppingChildItemBean, count, false, groupPosition);
                    }

                } else if ("996".equals(errorCode)) {
                    onLoginForTokenListener.onLoginForToken();

                }else if ("003".equals(errorCode)){

                    ToastUtil.networkUnavailable();
                }else{

                    ToastUtil.showErrorMessage(errorMessage, errorCode);
                }
            }
        });
    }

    private void updateCartGroupSellQuantity(final ShoppingCartChildBean shoppingChildItemBean, final int[] count,
                                             final boolean isAdd, final int groupPosition) {
        int i = count[0];
        if (isAdd){
            i=i+1;
        }else {
            i=i-1;
        }
        Log.d(TAG, "count[0]:" + count[0]);
        BaseApplication.getApplication().getAppAction().updateCartGroupSellQuantity(shoppingChildItemBean
                .getGroupSellID(), i, new ActionCallbackListener<String>() {


            @Override
            public void onSuccess(String totalPrice) {
                if (isAdd) {
                    count[0]++;//一方面final 是不能复制的
                } else {
                    count[0]--;
                }
                Log.d(TAG, "count[0]:" + count[0]);
                shoppingChildItemBean.setCount(count[0]);
                shoppingChildItemBean.setIsChecked(true);
                setRelatedGroupItem(shoppingChildItemBean.getGroupSellID(), true, groupPosition);
                //02
                boolean isRelatedParentBeChecked = dealRelatedParentIsChecked(groupPosition);
                ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean)
                        parentMapList.get
                                (groupPosition).get("parentName");
                shoppingParentItemBean.setIsChecked(isRelatedParentBeChecked);
                //03
                notifyDataSetChanged();
                //04.控制总checkedbox 接口
                onCheckingStateChangeListener.onCheckedBoxNeedChange(isCheckingAll());
                countPrice(totalPrice);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0) {
                    requestCount--;
                    if (isAdd) {
                        updateCartGroupSellQuantity(shoppingChildItemBean, count, true, groupPosition);
                    } else {
                        updateCartGroupSellQuantity(shoppingChildItemBean, count, false, groupPosition);
                    }


                } else if ("996".equals(errorCode)) {
                    onLoginForTokenListener.onLoginForToken();

                }
                else if ("003".equals(errorCode)){
                    ToastUtil.networkUnavailable();
                }else{
                    ToastUtil.showErrorMessage(errorMessage, errorCode);
                }
            }
        });
    }

    /*使用这一个，上面的废弃掉*/
    public void removeOneGood(boolean isgroupService, long groupSellID, int groupPosition, int childPosition, String totalPirce) {
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        Log.d(TAG, "isgroupService:" + isgroupService);
        if (isgroupService) {
            // 这个地方有问题
            Log.d(TAG, "childMapList.size():" + childMapList.size());
            //int j = childMapList.size()-1; j >=0; j--这里有一个坑那就是你只能倒过来删除，因为每删除一个会刷新一次界面,从下往上绘制的
            for (int j = childMapList.size()-1; j >=0; j--) {
                Log.d(TAG, "j:" + j);
                ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get
                        ("childName");
                Log.d(TAG, "shoppingChildItemBean.getGroupSellID():" + shoppingChildItemBean.getGroupSellID());
                Log.d(TAG, "groupSellID:" + groupSellID);
                if (shoppingChildItemBean.getGroupSellID() == groupSellID) {
                    childMapList.remove(j);//删除一次就会有一次刷新，不能同时删除多个，
                }

            }
        }else {
            childMapList.remove(childPosition);
        }



        //子项为空了，父项也得删除
        if (childMapList!=null&&childMapList.size()>0){

        }else {
                parentMapList.remove(groupPosition);
                childMapList_list.remove(groupPosition);//！！！！因为parentMapList和childMapList_list是pos关联的  得保持一致

        }
        if (parentMapList != null && parentMapList.size() > 0) {
            onCheckHasGoodsListener.onCheckHasGoods(true);//更新删除
        } else {
            onCheckHasGoodsListener.onCheckHasGoods(false);//
        }
        notifyDataSetChanged();
        countPrice(totalPirce);
    }
    public boolean  dealRelatedParentIsChecked(int groupPosition) {
        List<Map<String, Object>> childMapList = childMapList_list.get(groupPosition);
        for (int j = 0; j < childMapList.size(); j++) {
            ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
            if (!shoppingChildItemBean.isChecked()) {
                return false;//如果有一个没选择  就false
            }
        }
        return true;
    }

    /*全部删除*/
    public void removeAllGoods() {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitleText("确定要删除吗？")
                .setConfirmText("删除")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        pDialog.dismissWithAnimation();

                        requestCount = 35;
                        Log.d(TAG, "全部删除");
                        List<Carts.Cartbean01 > list01= new ArrayList<Carts.Cartbean01>();
                        List<Carts.Cartbean02 > list02= new ArrayList<Carts.Cartbean02>();
                        for (int i = parentMapList.size()-1; i>=0; i--) {//倒过来遍历  remove
                            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get("parentName");
                            if (shoppingParentItemBean.isChecked()){
                                List<Map<String, Object>> childMapList = childMapList_list.get(i);
                                for (int j = childMapList.size()-1; j >=0; j--) {//倒过来遍历  remove
                                    ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                                    if (shoppingChildItemBean.isGroupService()){
                                        //如果是套餐服务
                                        Log.d(TAG, "删除套餐服务") ;
                                        Carts.Cartbean02  cart02= new Carts.Cartbean02(shoppingChildItemBean.getGroupSellID());
                                        list02.add(cart02);
                                    } else {
                                        Log.d(TAG, "删除非套餐服务") ;
                                        Carts.Cartbean01  cart01= new Carts.Cartbean01(shoppingChildItemBean.getProductID());
                                        list01.add(cart01);
                                    }

                                }

                            }else {
                                List<Map<String, Object>> childMapList = childMapList_list.get(i);
                                for (int j = childMapList.size()-1; j >=0; j--) {//倒过来遍历  remove
                                    ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                                    if (shoppingChildItemBean.isChecked()) {
                                        if (shoppingChildItemBean.isGroupService()){
                                            //如果是套餐服务
                                            Log.d(TAG, "删除套餐服务") ;
                                            Carts.Cartbean02  cart02= new Carts.Cartbean02(shoppingChildItemBean.getGroupSellID());
                                            list02.add(cart02);
                                        } else {
                                            Log.d(TAG, "删除非套餐服务") ;
                                            Carts.Cartbean01  cart01= new Carts.Cartbean01(shoppingChildItemBean.getProductID());
                                            list01.add(cart01);
                                        }
                                    }
                                }
                            }

                        }
                        deleteAllCartSelectedItem(list01, list02);

                    }
                })
                .setCancelText("取消")
                .show();
        pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                notifyDataSetChanged();
                pDialog.dismissWithAnimation();
            }
        });








    }

    private void deleteAllCartSelectedItem(final List<Carts.Cartbean01> list01, final List<Carts.Cartbean02> list02) {
        BaseApplication.getApplication().getAppAction().deleteAllCartSelectedItem(list01,list02, new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String totalPrice) {
                for (int i = parentMapList.size() - 1; i >= 0; i--) {//倒过来遍历  remove
                    ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get
                            ("parentName");
                    if (shoppingParentItemBean.isChecked()) {
                        parentMapList.remove(i);
                        childMapList_list.remove(i);
                    } else {
                        List<Map<String, Object>> childMapList = childMapList_list.get(i);
                        for (int j = childMapList.size() - 1; j >= 0; j--) {//倒过来遍历  remove
                            ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j)
                                    .get("childName");
                            if (shoppingChildItemBean.isChecked()) {
                                childMapList.remove(j);
                            }
                        }
                    }
                }
                if (parentMapList != null && parentMapList.size() > 0) {
                    onCheckHasGoodsListener.onCheckHasGoods(true);//
                } else {
                    onCheckHasGoodsListener.onCheckHasGoods(false);//底视图
                }
                notifyDataSetChanged();
                countPrice(totalPrice);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                //不变，什么都不变
                if ("998".equals(errorCode)&& requestCount > 0){
                    requestCount--;
                    deleteAllCartSelectedItem(list01, list02);
                }else if ("996".equals(errorCode)){
                    onLoginForTokenListener.onLoginForToken();

                }else if ("003".equals(errorCode)){
                    ToastUtil.networkUnavailable();
                }else{
                    ToastUtil.showErrorMessage(errorMessage, errorCode);
                }

            }
        });
    }

    // 移入到收藏夹
    public void addToFavGoods() {
        // 只有选中的才会去删除
        List<Carts.Cartbean03 > list= new ArrayList<Carts.Cartbean03>();

        for (int i = parentMapList.size()-1; i>=0; i--) {//倒过来遍历  remove
            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get("parentName");
            if (shoppingParentItemBean.isChecked()){
                List<Map<String, Object>> childMapList = childMapList_list.get(i);
                for (int j = childMapList.size()-1; j >=0; j--) {//倒过来遍历  remove
                    ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                    Carts.Cartbean03 cart= new Carts.Cartbean03(shoppingParentItemBean.getShopID(), shoppingChildItemBean.getProductID(), shoppingChildItemBean.getGroupSellID());
                    list.add(cart);
                }
            }else {
                List<Map<String, Object>> childMapList = childMapList_list.get(i);
                for (int j = childMapList.size()-1; j >=0; j--) {//倒过来遍历  remove
                    ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                    if (shoppingChildItemBean.isChecked()) {
                        Carts.Cartbean03 cart= new Carts.Cartbean03(shoppingParentItemBean.getShopID(), shoppingChildItemBean.getProductID(), shoppingChildItemBean.getGroupSellID());
                        list.add(cart);
                        //childMapList.remove(j);
                    }
                }
            }
        }
        requestCount = 45;
        saveCartFavoriteProduct(list);


    }

    private void saveCartFavoriteProduct(final List<Carts.Cartbean03> list) {
        BaseApplication.getApplication().getAppAction().saveCartFavoriteProduct(list, new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String totalPrice) {
                //看着我一阵蛋疼，逻辑写复杂了
                for (int i = parentMapList.size() - 1; i >= 0; i--) {//倒过来遍历  remove
                    ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get
                            ("parentName");
                    if (shoppingParentItemBean.isChecked()) {
                        parentMapList.remove(i);
                        childMapList_list.remove(i);
                    } else {
                        List<Map<String, Object>> childMapList = childMapList_list.get(i);
                        for (int j = childMapList.size() - 1; j >= 0; j--) {//倒过来遍历  remove
                            ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j)
                                    .get("childName");
                            if (shoppingChildItemBean.isChecked()) {
                                childMapList.remove(j);
                            }
                        }
                    }
                }
                if (parentMapList != null && parentMapList.size() > 0) {
                    onCheckHasGoodsListener.onCheckHasGoods(true);//
                } else {
                    onCheckHasGoodsListener.onCheckHasGoods(false);//底视图
                }
                notifyDataSetChanged();
                countPrice(totalPrice);

                // 设置刷新
                PreferenceUtil basePreference = PreferenceUtil.getSharedPreference(context, "preferences");
                basePreference.putBoolean("is_refresh_collectionPage", true);
                basePreference.putBoolean("is_refresh_minePage", true);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0){
                    requestCount--;
                    saveCartFavoriteProduct(list);
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    onLoginForTokenListener.onLoginForToken();
                }else if ("003".equals(errorCode)){
                    ToastUtil.networkUnavailable();
                }else{
                    ToastUtil.showErrorMessage(errorMessage, errorCode);
                }

            }
        });
    }


    //供全选按钮调用
    public void setupAllChecked(boolean isChecked,String totalPrice) {
        for (int i = 0; i < parentMapList.size(); i++) {
            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get("parentName");
            shoppingParentItemBean.setIsChecked(isChecked);

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                shoppingChildItemBean.setIsChecked(isChecked);
            }
        }
        notifyDataSetChanged();
        countPrice(totalPrice);
    }
    public void setupAllCheckedFailed(boolean isChecked) {
        for (int i = 0; i < parentMapList.size(); i++) {
            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get("parentName");
            shoppingParentItemBean.setIsChecked(isChecked);

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                shoppingChildItemBean.setIsChecked(isChecked);
            }
        }
        notifyDataSetChanged();
    }
    //供总编辑按钮调用
    public void setupEditingAll(boolean isEditingAll) {
        for (int i = 0; i < parentMapList.size(); i++) {
            ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get(i).get("parentName");
            shoppingParentItemBean.setIsEditing(isEditingAll);

            List<Map<String, Object>> childMapList = childMapList_list.get(i);
            for (int j = 0; j < childMapList.size(); j++) {
                ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList.get(j).get("childName");
                shoppingChildItemBean.setIsEditing(isEditingAll);
            }
        }
        notifyDataSetChanged();
    }

    static class GroupViewHolder {

        CheckBox id_cb_select_parent; //勾选框 @BindView(R.id.id_cb_select_parent)

        TextView tv_title_parent; // groupItem的商店名@BindView(R.id.tv_title_parent)
        ImageView id_iv_logo_shop;

        TextView idTvGetcoupon; // 领券@BindView(R.id.id_tv_getcoupon)

        TextView id_tv_edit; //编辑按钮@BindView(R.id.id_tv_edit)

    }

    static class ChildViewHolder {
        CheckBox id_cb_select_child; // 勾选看 @BindView(R.id.id_cb_select_child)
        RoundTimelineView timeline; // 勾选看 @BindView(R.id.id_cb_select_child)


        ImageView id_iv_logo;//@BindView(R.id.id_iv_logo)

        TextView tv_items_child; //服务产品名称@BindView(R.id.tv_items_child)

        TextView id_tv_discount_price; //真实价格@BindView(R.id.id_tv_discount_price)

        TextView tv_items_child_desc; // 产品描述@BindView(R.id.tv_items_child_desc)

        TextView id_tv_count; //服务数量@BindView(R.id.id_tv_count)

        LinearLayout id_ll_normal;//@BindView(R.id.id_ll_normal) //非编辑状态

        ImageView id_iv_reduce; // 服务数量减少@BindView(R.id.id_iv_reduce)

        TextView id_tv_count_now;//@BindView(R.id.id_tv_count_now)

        ImageView id_iv_add;// 服务数量增加@BindView(R.id.id_iv_add)

        RelativeLayout ll_change_num;//  @BindView(R.id.ll_change_num)

        TextView id_tv_des_now; // 编辑状态下的单价@BindView(R.id.id_tv_des_now)

        ImageView id_iv_list;// @BindView(R.id.id_iv_list)

        TextView id_tv_goods_star;//@BindView(R.id.id_tv_goods_star)

        TextView id_tv_goods_delete;//删除商品@BindView(R.id.id_tv_goods_delete)

        LinearLayout id_ll_edtoring;// 编辑状态@BindView(R.id.id_ll_edtoring)

        RelativeLayout other_service;// 编辑状态@BindView(R.id.id_ll_edtoring)
    }
}
