package com.android.haobanyi.adapter.comformorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.adapter.ShoppingExpandableListAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartChildBean;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartParentBean;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.haobanyi.R.id.id_ll_parent;

/**
 * Created by fWX228941 on 2016/6/30.
 *
 * @作者: 付敏
 * @创建日期：2016/06/30
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class ConformOrderExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "fumin_test";
    List<Map<String, Object>> parentMapList  = new ArrayList<Map<String, Object>>();
    List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();
    Context context;


    List<Map<Long, Object>>  mappedMeaagList  = new ArrayList<>();
    final Map<Long, Object> mappedMeaageBean = new HashMap<Long, Object>(); //监听留言
    private int requestCount=45;


    public ConformOrderExpandableListAdapter(Context context, List<Map<String, Object>> parentMapList,
                                             List<List<Map<String, Object>>> childMapList_lis) {
        this.parentMapList = parentMapList;
        this.childMapList_list = childMapList_lis;//这种低智商的错误  ，置灰没有
        this.context = context;
    }

    // 触发登录
    OnLoginForTokenListener onLoginForTokenListener;
    public void setOnLoginForTokenListener(OnLoginForTokenListener onLoginForTokenListener){
        this.onLoginForTokenListener = onLoginForTokenListener;
    }
    public interface OnLoginForTokenListener{
        void onLoginForToken();
    }


    @Override
    public int getGroupCount() {
;
        return parentMapList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        /*
        * 01.这个为空，不理解 问题还是处在数量上。果然问题还是出现在这里
        * 02.问题出现在套餐这里，至少数据不正确
        *  不同对象的问题解决完以后：java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0
        *  数据下标越界，意思为：你某个地方调用了数组的下标为0但是这个数组的实际大小是0。所以有这种错误。你检查一下你的代码看哪里调用某个数组。
        * */

        return childMapList_list.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {

        return parentMapList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return childMapList_list.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d(TAG, "调用getGroupView");
        GroupViewHolder groupViewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_conform_order_parent, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tv_title_parent = (TextView) convertView
                    .findViewById(R.id.tv_title_parent);
            groupViewHolder.list_item_icon = (ImageView) convertView
                    .findViewById(R.id.list_item_icon);
            groupViewHolder.id_ll_parent = (LinearLayout)convertView.findViewById(id_ll_parent);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final ShoppingCartParentBean shoppingParentItemBean  = (ShoppingCartParentBean)parentMapList.get(groupPosition).get("parentBeanName"); //标识的是parentBean
        String ShopLogo = shoppingParentItemBean.getShopLogo();
        String ShopName = shoppingParentItemBean.getShopName();
        Glide.with(context)
                .load(ShopLogo)
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.shop_icon)// 也就是网络加载失败时出现的图片
                .into(groupViewHolder.list_item_icon);
        groupViewHolder.tv_title_parent.setText(ShopName);

        //跳转到店铺首页
        groupViewHolder.id_ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoShop(shoppingParentItemBean.getShopID());
            }
        });

        return convertView;
    }
    private void gotoShop(final long shopId) {

        BaseApplication.getApplication().getAppAction().getShopwithoutId(shopId, new
                ActionCallbackDoubleListener<ShopBean, List<SortDataBean>>() {

                    @Override
                    public void onSuccess(ShopBean data, List<SortDataBean> data02) {
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.SHOP_ID, data.getShopID());
                        bundle.putInt(Constants.STATE_FROM_SRGA, 01);
                        //    android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
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

    @SuppressLint("StringFormatMatches")
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup
            parent) {
        Log.d(TAG, "调用getChildView");
        ChildViewHolder childViewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_conform_order_child, null);
            childViewHolder = new ChildViewHolder();
            childViewHolder.id_iv_logo = (ImageView) convertView
                    .findViewById(R.id.id_iv_logo);
            childViewHolder.tv_items_child = (TextView) convertView
                    .findViewById(R.id.tv_items_child);
            childViewHolder.id_tv_discount_price = (TextView) convertView
                    .findViewById(R.id.id_tv_discount_price);
            childViewHolder.edit_phone = (TextView) convertView
                    .findViewById(R.id.edit_phone);
            childViewHolder.id_tv_count = (TextView) convertView
                    .findViewById(R.id.id_tv_count);
            childViewHolder.id_last_visiable = (LinearLayout) convertView
                    .findViewById(R.id.id_last_visiable);
            childViewHolder.id_tv_totalPrice = (TextView) convertView
                    .findViewById(R.id.id_tv_totalPrice);
            childViewHolder.id_tv_totalCount = (TextView) convertView
                    .findViewById(R.id.id_tv_totalCount);
            childViewHolder.id_hoabanyi_coupon_01 = (View) convertView
                    .findViewById(R.id.id_hoabanyi_coupon_01);
            childViewHolder.id_hoabanyi_coupon = (RelativeLayout) convertView
                    .findViewById(R.id.id_hoabanyi_coupon);


            childViewHolder.id_sales_promotion_01 = (View) convertView
                    .findViewById(R.id.id_sales_promotion_01);
            childViewHolder.id_sales_promotion = (RelativeLayout) convertView
                    .findViewById(R.id.id_sales_promotion);
            childViewHolder.id_tv_sales_promotion_01 = (TextView) convertView
                    .findViewById(R.id.id_tv_sales_promotion_01);
            childViewHolder.id_tv_sales_promotion_02 = (TextView) convertView
                    .findViewById(R.id.id_tv_sales_promotion_02);
            childViewHolder.id_rl_child = (RelativeLayout)convertView.findViewById(R.id.id_rl_child);


            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ShoppingCartChildBean shoppingChildItemBean = (ShoppingCartChildBean) childMapList_list.get(groupPosition).get(childPosition).get("childBeanName");
        String productImage = shoppingChildItemBean.getProductImage();
        Glide.with(context)
                .load(productImage)
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)// 也就是网络加载失败时出现的图片
                .into(childViewHolder.id_iv_logo);
        childViewHolder.tv_items_child.setText(shoppingChildItemBean.getProductName());
        childViewHolder.id_tv_count.setText(String.format(context.getString(R.string.product_count),
                shoppingChildItemBean.getQuantity())); // 用string.xml还有一个方面就是为了格式户,而且可以hi任意类型的
        childViewHolder.id_tv_discount_price.setText(String.format(context.getString(R.string.price), (shoppingChildItemBean.getProductPrice())*(shoppingChildItemBean.getQuantity()))); //价格
        //如果是最后一个孩子，则显示出来
        if (isLastChild){
            childViewHolder.id_last_visiable.setVisibility(View.VISIBLE);
            childViewHolder.id_tv_totalCount.setText("共" + shoppingChildItemBean.getProductCount() + "个服务");
            childViewHolder.id_tv_totalPrice.setText("￥"+shoppingChildItemBean.getOrderTotalPrice());

            if (shoppingChildItemBean.getVouchersList().isEmpty()){
                childViewHolder.id_hoabanyi_coupon.setVisibility(View.GONE);
                childViewHolder.id_hoabanyi_coupon_01.setVisibility(View.GONE);
            } else {
                childViewHolder.id_hoabanyi_coupon.setVisibility(View.VISIBLE);
                childViewHolder.id_hoabanyi_coupon_01.setVisibility(View.VISIBLE);
            }
            //优惠存在才显示，不存在是不显示的
            if (Double.parseDouble(shoppingChildItemBean.getSatisfySendMoney())>0.0 &&
                    Double.parseDouble(shoppingChildItemBean.getSatisfySendPrice())>0.0){

                childViewHolder.id_sales_promotion_01.setVisibility(View.VISIBLE);
                childViewHolder.id_sales_promotion.setVisibility(View.VISIBLE);
                childViewHolder.id_tv_sales_promotion_01.setText(String.format(context.getString(R.string
                        .voucher_content_formate), shoppingChildItemBean.getSatisfySendMoney(), shoppingChildItemBean
                        .getSatisfySendPrice()));
                childViewHolder.id_tv_sales_promotion_02.setText("省" + shoppingChildItemBean.getSatisfySendPrice() + "元");

            } else {
                childViewHolder.id_sales_promotion_01.setVisibility(View.GONE);
                childViewHolder.id_sales_promotion.setVisibility(View.GONE);
            }
        } else {
            childViewHolder.id_last_visiable.setVisibility(View.GONE);
        }
        /*
        * http://blog.csdn.net/harryweasley/article/details/50395209
        *  android edittext监听输入完成，输入完成后，获取到值
        *
        * */
        final ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                (groupPosition).get("parentBeanName");


        childViewHolder.edit_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String meaasage = s.toString();//键值对存储
                long shopID = shoppingParentItemBean.getShopID();
                //一个键只能对应一个值
                ShopBean shopBean = new ShopBean(shopID, meaasage);
                mappedMeaageBean.put(shopID,shopBean);
                mappedMeaagList.add(mappedMeaageBean);// 到时还是得看下到底是怎么回事
            }
        });

        childViewHolder.id_rl_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.Long com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartParentBean.getShopID()' on a null object reference
/*                ShoppingCartParentBean shoppingParentItemBean = (ShoppingCartParentBean) parentMapList.get
                        (groupPosition).get("parentBeanName");*/
                gotoProduct(shoppingParentItemBean.getShopID(),shoppingChildItemBean.getProductID());
            }
        });
        return convertView;
    }

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

    public  List<Map<Long, Object>>  getMappedMessageList(){
        return mappedMeaagList;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {

        public TextView tv_title_parent;
        public ImageView list_item_icon;
        public LinearLayout id_ll_parent;
    }

    static class ChildViewHolder {
        public ImageView id_iv_logo;
        public TextView tv_items_child;
        public TextView id_tv_discount_price;
        public TextView edit_phone;
        public TextView id_tv_count;
        public LinearLayout id_last_visiable;
        public TextView id_tv_totalCount;
        public TextView id_tv_totalPrice;
        //代金券
        public View id_hoabanyi_coupon_01;
        public RelativeLayout id_hoabanyi_coupon;

        //优惠活动
        public View id_sales_promotion_01;
        public RelativeLayout id_sales_promotion;
        public TextView id_tv_sales_promotion_01;
        public TextView id_tv_sales_promotion_02;
        public RelativeLayout id_rl_child;
    }
}
