package com.android.haobanyi.model.bean.shopping.store;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/7.
 *
 * @作者: 付敏
 * @创建日期：2016/07/07
 * @邮箱：466566941@qq.com
 * @当前文件描述：作用于单个店铺下全部服务和店铺首页的单个服务getShopProductListByHotSort [先保存后回传，速度要快]
 * 指定规则：直接脱胎于bean的新bean 命名规则： _bean
 *
 */
public class ShopRelatedServiceBean {
    /*
    * 测试时间：7/7日
    * 经过测试发现：getShopProductListByHotSort(1,1,23)
    * 最多一页10条数据   "totalCount":23,"pageIndex":1,"pageSize":10  【log】   原来设想这个totalCount 是上限数量，  这个由服务器确定
    *
    *   getShopProductListByHotSort(1,1,10)
    *   "totalCount":22,"pageIndex":1,"pageSize":10
    *
    *   (1,2,10)
    *   第二页也是显示10条数据  返回的是第二页的十条数据
    *   totalCount":23,"pageIndex":2,"pageSize":10,
    *  (1,3,10)
    *  第三页只有三条数据
    *totalCount":23,"pageIndex":3,"pageSize":10    后连个值是自己设置进去的，暂时没有发现什么意义
    *
    * (1,1,5)
    * 异常 请求失效了 但是第二次又请求成功了。
    * 第一页返回了5条数据  "totalCount":23,"pageIndex":1,"pageSize":5
    *
    * (1,4,5)
    * "totalCount":23,"pageIndex":4,"pageSize":5
    * 用的是pageIndex * pageSize  所以是有数据的
    *
    *(1,5,5)
    * 23-20 = 最后三条数据  果然
    * */



    /**
     * code : 101
     * message : 获取成功！
     * data : {"totalCount":22,"pageIndex":1,"pageSize":10,"list":[{"ProductID":1,"ProductName":"深圳公司注册",
     * "MinPrice":600,"ImagePath":"http://www.haobanyi.com/Storage/selleradmin/1/Album/201606241528128177791.jpg",
     * "OrderNum":0,"IsHot":0,"Province":"广东省","City":"深圳市","District":"南山区"},{"ProductID":2,"ProductName":"公司变更",
     * "MinPrice":400,"ImagePath":"http://www.haobanyi.com/Storage/selleradmin/1/Album/201606241442180799511.jpg",
     * "OrderNum":0,"IsHot":0,"Province":"广东省","City":"深圳市","District":"南山区"},{"ProductID":3,"ProductName":"深圳公司注销",
     * "MinPrice":3000,"ImagePath":"http://www.haobanyi.com/Storage/selleradmin/1/Album/201606271841286763911.jpg",
     * "OrderNum":0,"IsHot":0,"Province":"广东省","City":"深圳市","District":"南山区"},{"ProductID":4,
     * "ProductName":"一般纳税人一年记账","MinPrice":4800,"ImagePath":"http://www.haobanyi
     * .com/Storage/selleradmin/1/Album/201606291658077949871.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":5,"ProductName":"香港公司注册","MinPrice":3800,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291115391561511.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":6,"ProductName":"计算机软件著作权","MinPrice":1800,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606271832059252901.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":8,"ProductName":"前海公司注册","MinPrice":1380,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291449007134491.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":9,"ProductName":"外资公司注册","MinPrice":6800,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291450386175991.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":10,"ProductName":"股份公司注册","MinPrice":2200,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291923412151801.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":11,"ProductName":"小规模一年记账","MinPrice":2000,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291712336915171.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"}]}
     */

    private int code;
    private String message;
    /**
     * totalCount : 22  这个因该是中条数
     * pageIndex : 1
     * pageSize : 10
     * list : [{"ProductID":1,"ProductName":"深圳公司注册","MinPrice":600,"ImagePath":"http://www.haobanyi
     * .com/Storage/selleradmin/1/Album/201606241528128177791.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":2,"ProductName":"公司变更","MinPrice":400,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606241442180799511.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":3,"ProductName":"深圳公司注销","MinPrice":3000,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606271841286763911.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":4,"ProductName":"一般纳税人一年记账","MinPrice":4800,
     * "ImagePath":"http://www.haobanyi.com/Storage/selleradmin/1/Album/201606291658077949871.jpg","OrderNum":0,
     * "IsHot":0,"Province":"广东省","City":"深圳市","District":"南山区"},{"ProductID":5,"ProductName":"香港公司注册",
     * "MinPrice":3800,"ImagePath":"http://www.haobanyi.com/Storage/selleradmin/1/Album/201606291115391561511.jpg",
     * "OrderNum":0,"IsHot":0,"Province":"广东省","City":"深圳市","District":"南山区"},{"ProductID":6,
     * "ProductName":"计算机软件著作权","MinPrice":1800,"ImagePath":"http://www.haobanyi
     * .com/Storage/selleradmin/1/Album/201606271832059252901.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":8,"ProductName":"前海公司注册","MinPrice":1380,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291449007134491.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":9,"ProductName":"外资公司注册","MinPrice":6800,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291450386175991.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":10,"ProductName":"股份公司注册","MinPrice":2200,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291923412151801.jpg","OrderNum":0,"IsHot":0,"Province":"广东省",
     * "City":"深圳市","District":"南山区"},{"ProductID":11,"ProductName":"小规模一年记账","MinPrice":2000,"ImagePath":"http://www
     * .haobanyi.com/Storage/selleradmin/1/Album/201606291712336915171.jpg","OrderNum":0,"IsHot":0,"Province":"广东省","City":"深圳市","District":"南山区"}]
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
        private String pageIndex;// 这里的int 换成了String 类型，因为是数据流，所以类型自由转化
        private int pageSize;
        /**
         * ProductID : 1
         * ProductName : 深圳公司注册
         * MinPrice : 600.0
         * ImagePath : http://www.haobanyi.com/Storage/selleradmin/1/Album/201606241528128177791.jpg
         * OrderNum : 0
         * IsHot : 0
         * Province : 广东省
         * City : 深圳市
         * District : 南山区
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
            private String Province;
            private String City;
            private String District;

            /* ==============SearchShopByKeyword 新添加字段如下=================*/
            private long ShopID;
            private String ShopName;
            private String ShopLogo;
            //private int OrderNum;
            private String MainService;
            //private String District;
            private double ComprehensiveScore;
            /* ==============SearchShopByKeyword 新添加字段如上=================*/

            /* ==============GetFavoriteShopList =================*/
            private long FsID;
           /* ==============GetFavoriteShopList =================*/


            public long getShopID() {
                return ShopID;
            }

            public void setShopID(long shopID) {
                ShopID = shopID;
            }

            public String getShopName() {
                return ShopName;
            }

            public void setShopName(String shopName) {
                ShopName = shopName;
            }

            public String getShopLogo() {
                return ShopLogo;
            }

            public void setShopLogo(String shopLogo) {
                ShopLogo = shopLogo;
            }

            public String getMainService() {
                return MainService;
            }

            public void setMainService(String mainService) {
                MainService = mainService;
            }

            public double getComprehensiveScore() {
                return ComprehensiveScore;
            }

            public void setComprehensiveScore(double comprehensiveScore) {
                ComprehensiveScore = comprehensiveScore;
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

            public long getFsID() {
                return FsID;
            }

            public void setFsID(long fsID) {
                FsID = fsID;
            }
        }
    }
}
