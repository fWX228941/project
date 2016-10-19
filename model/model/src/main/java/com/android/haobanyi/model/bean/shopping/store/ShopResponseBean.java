package com.android.haobanyi.model.bean.shopping.store;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/7.
 *
 * @作者: 付敏
 * @创建日期：2016/07/07
 * @邮箱：466566941@qq.com
 * @当前文件描述：getShop() 店铺信息
 */
public class ShopResponseBean {


    /**
     * code : 101
     * message : 获取成功！
     * data : {"ShopID":1,"ShopName":"好办易事务所","ShopLogo":"http://www.haobanyi
     * .com/Storage/selleradmin/1/Logo/201606281523061706575s.png","OrdersNum":0,
     * "MainService":"提供从公司注册、商标注册、代理记账、知识产权、法律服务、财会税务等一站式的新型在线企业服务。","District":"广东省深圳市南山区","IsFav":false,
     * "HotProductList":[]}
     */

    private int code;
    private String message;
    /**
     * ShopID : 1
     * ShopName : 好办易事务所
     * ShopLogo : http://www.haobanyi.com/Storage/selleradmin/1/Logo/201606281523061706575s.png
     * OrdersNum : 0
     * MainService : 提供从公司注册、商标注册、代理记账、知识产权、法律服务、财会税务等一站式的新型在线企业服务。
     * District : 广东省深圳市南山区
     * IsFav : false    收藏这个字段待定
     * HotProductList : []
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int ShopID;
        private String ShopName;
        private String ShopLogo;
        private int OrdersNum;
        private String MainService;
        private String District;
        private boolean IsFav;
        private List<HotProductBean> HotProductList;  //热门服务  有什么用？





        public int getShopID() {
            return ShopID;
        }

        public void setShopID(int ShopID) {
            this.ShopID = ShopID;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String ShopName) {
            this.ShopName = ShopName;
        }

        public String getShopLogo() {
            return ShopLogo;
        }

        public void setShopLogo(String ShopLogo) {
            this.ShopLogo = ShopLogo;
        }

        public int getOrdersNum() {
            return OrdersNum;
        }

        public void setOrdersNum(int OrdersNum) {
            this.OrdersNum = OrdersNum;
        }

        public String getMainService() {
            return MainService;
        }

        public void setMainService(String MainService) {
            this.MainService = MainService;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String District) {
            this.District = District;
        }

        public boolean isIsFav() {
            return IsFav;
        }

        public void setIsFav(boolean IsFav) {
            this.IsFav = IsFav;
        }

        public List<HotProductBean> getHotProductList() {
            return HotProductList;
        }

        public void setHotProductList(List<HotProductBean> HotProductList) {
            this.HotProductList = HotProductList;
        }

        public static class HotProductBean {
            private long ProductID;
            private String ProductName;
            private double MinPrice;
            private String ImagePath;
            private int OrderNum;

            public long getProductID() {
                return ProductID;
            }

            public void setProductID(long productID) {
                ProductID = productID;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String productName) {
                ProductName = productName;
            }

            public double getMinPrice() {
                return MinPrice;
            }

            public void setMinPrice(double minPrice) {
                MinPrice = minPrice;
            }

            public String getImagePath() {
                return ImagePath;
            }

            public void setImagePath(String imagePath) {
                ImagePath = imagePath;
            }

            public int getOrderNum() {
                return OrderNum;
            }

            public void setOrderNum(int orderNum) {
                OrderNum = orderNum;
            }
        }
    }
}
