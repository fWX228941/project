package com.android.haobanyi.model.test;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/23.
 *
 * @作者: 付敏
 * @创建日期：2016/06/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class testData01 {

    private int code;
    private String message;
    /**
     * totalCount : 45
     * pageIndex : 1
     * pageSize : 10
     * list : [{"ProductID":2,"ProductName":"1元注册公司","MinPrice":598,"ImagePath":"/Areas/Web/images/服务页.jpg",
     * "OrderNum":0,"IsHot":0,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":3,
     * "ProductName":"2元注册公司","MinPrice":698,"ImagePath":"/Areas/Web/images/服务页2.jpg","OrderNum":0,"IsHot":0,
     * "ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":4,"ProductName":"100元注册公司",
     * "MinPrice":1098,"ImagePath":"/Areas/Web/images/服务页3.jpg","OrderNum":0,"IsHot":0,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":5,"ProductName":"外资代表处变更","MinPrice":6888,
     * "ImagePath":"/Areas/Web/images/服务页4.jpg","OrderNum":0,"IsHot":1,"ShopName":"好办易","Province":"","City":"",
     * "District":""},{"ProductID":6,"ProductName":"集团代表处变更","MinPrice":5999,"ImagePath":"/Areas/Web/images/服务页5
     * .jpg","OrderNum":0,"IsHot":1,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":7,
     * "ProductName":"私人代表处变更","MinPrice":4500,"ImagePath":"/Areas/Web/images/服务页6.jpg","OrderNum":0,"IsHot":1,
     * "ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":8,"ProductName":"外包代表处变更",
     * "MinPrice":6800,"ImagePath":"/Areas/Web/images/服务页7.jpg","OrderNum":0,"IsHot":1,"ShopName":"好办易",
     * "Province":"","City":"","District":""},{"ProductID":9,"ProductName":"国际代表处变更","MinPrice":9800,
     * "ImagePath":"/Areas/Web/images/服务页8.jpg","OrderNum":0,"IsHot":1,"ShopName":"好办易","Province":"","City":"",
     * "District":""},{"ProductID":10,"ProductName":"特使代表处变更","MinPrice":7200,"ImagePath":"/Areas/Web/images/gang-01
     * .jpg","OrderNum":0,"IsHot":1,"ShopName":"好办易","Province":"","City":"","District":""},{"ProductID":11,
     * "ProductName":"民警代表处变更","MinPrice":9600,"ImagePath":"/Areas/Web/images/gang-01.jpg","OrderNum":0,"IsHot":1,
     * "ShopName":"好办易","Province":"","City":"","District":""}]
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
        private int totalCount;
        private int pageIndex;
        private int pageSize;
        /**
         * ProductID : 2
         * ProductName : 1元注册公司
         * MinPrice : 598.0
         * ImagePath : /Areas/Web/images/服务页.jpg
         * OrderNum : 0
         * IsHot : 0
         * ShopName : 好办易
         * Province :
         * City :
         * District :
         */

        private List<ListBean> list;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
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
            private int ProductID;
            private String ProductName;
            private double MinPrice;
            private String ImagePath;
            private int OrderNum;
            private int IsHot;
            private String ShopName;
            private String Province;
            private String City;
            private String District;

            public int getProductID() {
                return ProductID;
            }

            public void setProductID(int ProductID) {
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
