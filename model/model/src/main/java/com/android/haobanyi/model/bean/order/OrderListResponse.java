package com.android.haobanyi.model.bean.order;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/16.
 *
 * @作者: 付敏
 * @创建日期：2016/08/16
 * @邮箱：466566941@qq.com
 * @当前文件描述：订单列表
 */
public class OrderListResponse {


    private int code;
    private String message;
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
         * OrderID : 10215
         * UserID : 30
         * ShopID : 85
         * Price : 0.01
         * Status : 2
         * ShopName : zenele的店铺
         * ShopLogo : http://www.haobanyi.com/Storage/selleradmin/85/Logo/201607291924326537917.jpg
         * OrderExtends : [{"OrderExtendID":10245,"ProductID":114,"ProductName":"公司年报","ProductImage":"http://www
         * .haobanyi.com/Storage/selleradmin/85/Album/s160x160_2016072919345685549385.jpg","ProductPrice":0.01,
         * "Quantity":1,"ShopAttrList":[]}]
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
            private long OrderID;
            private long UserID;
            private long ShopID;
            private String Price;
            private int Status;
            private String ShopName;
            private String ShopLogo;
            /**
             * OrderExtendID : 10245
             * ProductID : 114
             * ProductName : 公司年报
             * ProductImage : http://www.haobanyi.com/Storage/selleradmin/85/Album/s160x160_2016072919345685549385.jpg
             * ProductPrice : 0.01
             * Quantity : 1
             * ShopAttrList : []
             */

            private List<OrderExtendsBean> OrderExtends;

            public long getOrderID() {
                return OrderID;
            }

            public void setOrderID(long OrderID) {
                this.OrderID = OrderID;
            }

            public long getUserID() {
                return UserID;
            }

            public void setUserID(long UserID) {
                this.UserID = UserID;
            }

            public long getShopID() {
                return ShopID;
            }

            public void setShopID(long ShopID) {
                this.ShopID = ShopID;
            }

            public String getPrice() {
                return Price;
            }

            public void setPrice(String Price) {
                this.Price = Price;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
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

            public List<OrderExtendsBean> getOrderExtends() {
                return OrderExtends;
            }

            public void setOrderExtends(List<OrderExtendsBean> OrderExtends) {
                this.OrderExtends = OrderExtends;
            }

            public static class OrderExtendsBean {
                private long OrderExtendID;
                private long ProductID;
                private String ProductName;
                private String ProductImage;
                private String ProductPrice;
                private String Quantity;
                private List<?> ShopAttrList;

                public long getOrderExtendID() {
                    return OrderExtendID;
                }

                public void setOrderExtendID(long OrderExtendID) {
                    this.OrderExtendID = OrderExtendID;
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

                public String getProductImage() {
                    return ProductImage;
                }

                public void setProductImage(String ProductImage) {
                    this.ProductImage = ProductImage;
                }

                public String getProductPrice() {
                    return ProductPrice;
                }

                public void setProductPrice(String ProductPrice) {
                    this.ProductPrice = ProductPrice;
                }

                public String getQuantity() {
                    return Quantity;
                }

                public void setQuantity(String Quantity) {
                    this.Quantity = Quantity;
                }

                public List<?> getShopAttrList() {
                    return ShopAttrList;
                }

                public void setShopAttrList(List<?> ShopAttrList) {
                    this.ShopAttrList = ShopAttrList;
                }
            }
        }
    }

}
