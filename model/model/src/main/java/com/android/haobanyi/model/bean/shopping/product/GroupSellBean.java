package com.android.haobanyi.model.bean.shopping.product;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/15.
 *
 * @作者: 付敏
 * @创建日期：2016/07/15
 * @邮箱：466566941@qq.com
 * @当前文件描述：优惠套餐
 *
 * {"GroupSellID":1,"Name":"套餐1","VouchersDes":“测试”,"Products":[{"ProductName":"套餐1","ImagePath":"套餐1","MinPrice":"1","GroupPrice":"1"}]}
 *
 */
public class GroupSellBean {
    /**
     * GroupSellID : 1
     * Name : 套餐1
     * VouchersDes : “测试”
     * Products : [{"ProductName":"套餐1","ImagePath":"套餐1","MinPrice":"1","GroupPrice":"1"}]
     */

    private long GroupSellID;
    private String Name;
    private String VouchersDes;
    /**
     * ProductName : 套餐1
     * ImagePath : 套餐1
     * MinPrice : 1
     * GroupPrice : 1
     */

    private List<ProductsBean> Products;

    public long getGroupSellID() {
        return GroupSellID;
    }

    public void setGroupSellID(long GroupSellID) {
        this.GroupSellID = GroupSellID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getVouchersDes() {
        return VouchersDes;
    }

    public void setVouchersDes(String VouchersDes) {
        this.VouchersDes = VouchersDes;
    }

    public List<ProductsBean> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductsBean> Products) {
        this.Products = Products;
    }

    public static class ProductsBean {
        private String ProductName;
        private String ImagePath;
        private String MinPrice;
        private String GroupPrice;

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String ProductName) {
            this.ProductName = ProductName;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }

        public String getMinPrice() {
            return MinPrice;
        }

        public void setMinPrice(String MinPrice) {
            this.MinPrice = MinPrice;
        }

        public String getGroupPrice() {
            return GroupPrice;
        }

        public void setGroupPrice(String GroupPrice) {
            this.GroupPrice = GroupPrice;
        }
    }
}
