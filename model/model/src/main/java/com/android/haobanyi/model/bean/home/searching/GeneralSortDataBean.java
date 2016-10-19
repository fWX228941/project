package com.android.haobanyi.model.bean.home.searching;

import java.util.List;

/**
 * Created by fWX228941 on 2016/5/12.
 *
 * @作者: 付敏
 * @创建日期：2016/05/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：综合排序的服务列表json映射bean对象orm
 */
public class GeneralSortDataBean {

    /**
     * code : 101
     * message : 获取成功！
     * data : {"totalCount":45,"pageIndex":1,"pageSize":10,"list":[{"ProductID":55,"ProductName":"测试测试",
     * "MinPrice":333,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,
     * "ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":54,"ProductName":"sdfsdf",
     * "MinPrice":1000,"ImagePath":"/Areas/SellerAdmin/images/2_05112700187137111_240.jpg","OrderNum":0,"IsHot":0,
     * "ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":53,"ProductName":"我是服务产品名称不少于3小于50有空
     * 格          还有特殊字符！@#￥%\u2026\u2026&","MinPrice":666.88,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":52,"ProductName":"我是ie","MinPrice":5555,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":51,"ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格
     * 长度一旦超出就会提示 ","MinPrice":666,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,
     * "IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":50,
     * "ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格  长度一旦超出就会提示 ","MinPrice":666,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":49,"ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格
     * 长度一旦超出就会提示 ","MinPrice":666,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,
     * "IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":48,
     * "ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格  长度一旦超出就会提示 ","MinPrice":666,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":47,"ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格
     * 长度一旦超出就会提示 ","MinPrice":666,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,
     * "IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":46,
     * "ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格  长度一旦超出就会提示 ","MinPrice":666,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""}]}
     */

    private int code;
    private String message;
    /**
     * totalCount : 45  这个就是总数
     * pageIndex : 1
     * pageSize : 10
     * list : [{"ProductID":55,"ProductName":"测试测试","MinPrice":333,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":54,"ProductName":"sdfsdf","MinPrice":1000,
     * "ImagePath":"/Areas/SellerAdmin/images/2_05112700187137111_240.jpg","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":53,"ProductName":"我是服务产品名称不少于3小于50有空       格
     * 还有特殊字符！@#￥%\u2026\u2026&","MinPrice":666.88,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240
     * .gif","OrderNum":0,"IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":52,
     * "ProductName":"我是ie","MinPrice":5555,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif",
     * "OrderNum":0,"IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":51,
     * "ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格  长度一旦超出就会提示 ","MinPrice":666,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":50,"ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格
     * 长度一旦超出就会提示 ","MinPrice":666,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,
     * "IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":49,
     * "ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格  长度一旦超出就会提示 ","MinPrice":666,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":48,"ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格
     * 长度一旦超出就会提示 ","MinPrice":666,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,
     * "IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":47,
     * "ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格  长度一旦超出就会提示 ","MinPrice":666,
     * "ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":46,"ProductName":"我是商品名称，不少于≥3，≤50，可以是特殊字符@#￥%&也可以带空   格
     * 长度一旦超出就会提示 ","MinPrice":666,"ImagePath":"/Areas/SellerAdmin/images/default_goods_image_240.gif","OrderNum":0,"IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""}]
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
        private String totalCount;
        private String pageIndex;
        private int pageSize;
        /**
         * ProductID : 55
         * ProductName : 测试测试
         * MinPrice : 333.0
         * ImagePath : /Areas/SellerAdmin/images/default_goods_image_240.gif
         * OrderNum : 0
         * IsHot : 0
         * ShopName : 好办易
         * Province :
         * City :
         * District :
         */

        private List<ListBean> list;

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(String pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private long ProductID;
            private String ProductName;
            private double MinPrice;
            private String ImagePath;
            private int OrderNum;
            private int IsHot;
            private String ShopName;
            private String Province;
            private String City;
            private String District;



            /* ==============GetFavoriteProductList =================*/
            private long FpID;
            private int IsRecommend;
            /* ==============GetFavoriteProductList =================*/



            /*================getProductListByNormalSort===========================*/
            private float ComprehensiveScore;
            /*================getProductListByNormalSort===========================*/



            public float getComprehensiveScore() {
                return ComprehensiveScore;
            }

            public void setComprehensiveScore(float comprehensiveScore) {
                ComprehensiveScore = comprehensiveScore;
            }
            public long getFpID() {
                return FpID;
            }

            public void setFpID(long fpID) {
                FpID = fpID;
            }

            public int getIsRecommend() {
                return IsRecommend;
            }

            public void setIsRecommend(int isRecommend) {
                IsRecommend = isRecommend;
            }

            public long getProductID() {
                return ProductID;
            }

            public void setProductID(long ProductID) {
                this.ProductID = ProductID;
            }

            public String getProductName() {
                return ProductName;
            }

            public void setProductName(String ProductName) {
                this.ProductName = ProductName;
            }

            public double getMinPrice() {
                return MinPrice;
            }

            public void setMinPrice(double MinPrice) {
                this.MinPrice = MinPrice;
            }

            public String getImagePath() {
                return ImagePath;
            }

            public void setImagePath(String ImagePath) {
                this.ImagePath = ImagePath;
            }

            public int getOrderNum() {
                return OrderNum;
            }

            public void setOrderNum(int OrderNum) {
                this.OrderNum = OrderNum;
            }

            public int getIsHot() {
                return IsHot;
            }

            public void setIsHot(int IsHot) {
                this.IsHot = IsHot;
            }

            public String getShopName() {
                return ShopName;
            }

            public void setShopName(String ShopName) {
                this.ShopName = ShopName;
            }

            public String getProvince() {
                return Province;
            }

            public void setProvince(String Province) {
                this.Province = Province;
            }

            public String getCity() {
                return City;
            }

            public void setCity(String City) {
                this.City = City;
            }

            public String getDistrict() {
                return District;
            }

            public void setDistrict(String District) {
                this.District = District;
            }
        }
    }

}
