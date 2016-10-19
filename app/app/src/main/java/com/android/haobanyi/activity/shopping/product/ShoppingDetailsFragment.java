package com.android.haobanyi.activity.shopping.product;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by fWX228941 on 2016/7/5.
 *
 * @作者: 付敏
 * @创建日期：2016/07/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务详情页
 */
public class ShoppingDetailsFragment extends BaseFragment {
/*    @BindView(R.id.product_img)
    ImageView productImg;*/
    @BindView(R.id.webview)
    AdvancedWebView mWebView;
    private long productID;
    ProductDetailsBean productBean;
    @Override
    protected void lazyload() {

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_shopping_details;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        productID = getActivity().getIntent().getLongExtra(Constants.PRODUCT_ID, -1);
        NTPTime.getInstance().getCurrentTime();
        if (productID !=-1){
            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(activity, "product");
            productBean = complexPreferences.getObject("productid" + productID, ProductDetailsBean.class);
            loadData(productBean);
        }
    }
    private void loadData(ProductDetailsBean productBean) {
        //加载网页源代码  Load HTML source text and display as page

/*        Glide.with(activity)
                .load(productBean.getImagePath())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.big_image)// 也就是网络加载失败时出现的图片
                .into(productImg);
        productName.setText(productBean.getProductName());*/
        try {
            if (productBean.getWebDes()!=null) {
                Log.d("ShoppingDetailsFragment", productBean.getWebDes());
                mWebView.loadHtml(productBean.getWebDes());
            }
        }catch (Exception e){

        }

    }
}
