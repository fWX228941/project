package com.android.haobanyi.model.bean.order;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/16.
 *
 * @作者: 付敏
 * @创建日期：2016/08/16
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class OrderChildBean {
    private long OrderID;// 订单ID
    private long OrderExtendID;// 订单ID
        private long ProductID;// 用户ID
        private String ProductName;//
        private String ProductImage;//
        private String ProductPrice;
        private String Quantity;
        //private List<ShopAttrListBean> ShopAttrList; 暂时不考虑其他服务


    /*=============添加一个是否是最后一个子item的标识量==================*/
        private boolean IsLastChild;
        private int Count;
        private String Price;
        private int Status;
        /*=============添加一个是否是最后一个子item的标识量==================*/

        public OrderChildBean(long orderExtendID, long productID, String productName, String productImage,
                                    String productPrice, String quantity,boolean isLastChild,int count,String price,int status,long orderID) {
            OrderExtendID = orderExtendID;
            ProductID = productID;
            ProductName = productName;
            ProductImage = productImage;
            ProductPrice = productPrice;
            Quantity = quantity;
            IsLastChild = isLastChild;
            Count=count;
            Price = price;
            Status = status;
            OrderID = orderID;
        }
    public long getOrderID() {
        return OrderID;
    }

    public void setOrderID(long orderID) {
        OrderID = orderID;
    }


    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

        public boolean isLastChild() {
            return IsLastChild;
        }

        public void setIsLastChild(boolean isLastChild) {
            IsLastChild = isLastChild;
        }

        public long getOrderExtendID() {
            return OrderExtendID;
        }

        public void setOrderExtendID(long orderExtendID) {
            OrderExtendID = orderExtendID;
        }

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

        public String getProductImage() {
            return ProductImage;
        }

        public void setProductImage(String productImage) {
            ProductImage = productImage;
        }

        public String getProductPrice() {
            return ProductPrice;
        }

        public void setProductPrice(String productPrice) {
            ProductPrice = productPrice;
        }

        public String getQuantity() {
            return Quantity;
        }

        public void setQuantity(String quantity) {
            Quantity = quantity;
        }
/*        public List<ShopAttrListBean> getShopAttrList() {
            return ShopAttrList;
        }

        public void setShopAttrList(List<ShopAttrListBean> shopAttrList) {
            ShopAttrList = shopAttrList;
        }*/


        public static class ShopAttrListBean {
            private long ShopAttrID;
            private String AttrName;
            private String Price;

            public ShopAttrListBean(long shopAttrID, String attrName, String price) {
                ShopAttrID = shopAttrID;
                AttrName = attrName;
                Price = price;
            }

            public long getShopAttrID() {
                return ShopAttrID;
            }

            public void setShopAttrID(long shopAttrID) {
                ShopAttrID = shopAttrID;
            }

            public String getAttrName() {
                return AttrName;
            }

            public void setAttrName(String attrName) {
                AttrName = attrName;
            }

            public String getPrice() {
                return Price;
            }

            public void setPrice(String price) {
                Price = price;
            }
        }

}
