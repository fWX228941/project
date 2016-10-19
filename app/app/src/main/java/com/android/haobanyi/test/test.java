package com.android.haobanyi.test;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/30.
 *
 * @作者: 付敏
 * @创建日期：2016/06/30
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class test {
    /**
     * code : 101
     * message : 获取成功
     * data : {"totalPrice":20066,"totalCount":7,"discountMoney":100,"contactList":[],"redEnvelopeList":[],
     * "list":[{"ShopID":1,"ShopName":"好办易","ShopLogo":"http://192.168.0
     * .88:8089/Storage/selleradmin/1/Logo/201606230951305731001s.jpg","OrderTotalPrice":12866,"ProductCount":3,
     * "SatisfySendMoney":100,"SatisfySendPrice":1000,"VouchersList":[],"GroupSellProducts":[{"GroupSellID":17,
     * "GroupSellPrice":6966,"GroupSellName":"测试删除","IsSelected":true,"Products":[{"ProductID":54,
     * "ProductName":"sdfsdf","ProductImage":"http://192.168.0
     * .88:8089/Areas/SellerAdmin/images/2_05112700187137111_240.jpg","ProductPrice":666,"DiscountMoney":334,
     * "Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":2,"AttrName":"工本费","Price":100},
     * {"ShopAttrID":1,"AttrName":"地址费","Price":200}]},{"ProductID":117,"ProductName":"香港公司注册",
     * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg",
     * "ProductPrice":6000,"DiscountMoney":0,"Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[]}]}],
     * "SingleProducts":[{"ProductID":117,"ProductName":"香港公司注册","ProductImage":"http://192.168.0
     * .88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg","ProductPrice":6000,"DiscountMoney":0,
     * "Quantity":1,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[]}]},{"ShopID":17,"ShopName":"钱白白",
     * "ShopLogo":"http://192.168.0.88:8089/Strategies/selleradmin/17/Logo/201605231901171943216.png",
     * "OrderTotalPrice":7200,"ProductCount":4,"SatisfySendMoney":0,"SatisfySendPrice":0,"VouchersList":[],
     * "GroupSellProducts":[{"GroupSellID":28,"GroupSellPrice":3800,"GroupSellName":"创业组合","IsSelected":true,
     * "Products":[{"ProductID":115,"ProductName":"创业组合-工商注册","ProductImage":"http://192.168.0
     * .88:8089/Storage/selleradmin/17/Album/2016052615435617413417.jpg","ProductPrice":1000,"DiscountMoney":0,
     * "Quantity":1,"GroupSellID":28,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":25,"AttrName":"地址服务费",
     * "Price":500},{"ShopAttrID":26,"AttrName":"数字证书工本费","Price":300}]},{"ProductID":116,"ProductName":"创业组合-代理记帐",
     * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/17/Album/2016052615452384897117.jpg",
     * "ProductPrice":2000,"DiscountMoney":1000,"Quantity":1,"GroupSellID":28,"IsSelected":true,
     * "ShopAttrList":[]}]}],"SingleProducts":[{"ProductID":114,"ProductName":"工商变更","ProductImage":"http://192.168.0
     * .88:8089/Strategies/selleradmin/17/Album/201605231904021334112.jpg","ProductPrice":1000,"DiscountMoney":0,
     * "Quantity":1,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":25,"AttrName":"地址服务费",
     * "Price":1000},{"ShopAttrID":26,"AttrName":"数字证书工本费","Price":200}]},{"ProductID":121,"ProductName":"全面公司注册",
     * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/17/Album/2016060211185590813617.jpg",
     * "ProductPrice":500,"DiscountMoney":0,"Quantity":1,"GroupSellID":0,"IsSelected":true,
     * "ShopAttrList":[{"ShopAttrID":25,"AttrName":"地址服务费","Price":100},{"ShopAttrID":26,"AttrName":"数字证书工本费",
     * "Price":100},{"ShopAttrID":49,"AttrName":"各项工本费","Price":100},{"ShopAttrID":48,"AttrName":"比较多数字证书工本费",
     * "Price":100},{"ShopAttrID":47,"AttrName":"数字证书工本费地数字证书工本费","Price":100},{"ShopAttrID":46,"AttrName":"数字证书工本费",
     * "Price":100},{"ShopAttrID":45,"AttrName":"数字证书工本费","Price":100}]}]}]}
     */

    private int code;
    private String message;
    /**
     * totalPrice : 20066.0
     * totalCount : 7
     * discountMoney : 100.0
     * contactList : []
     * redEnvelopeList : []
     * list : [{"ShopID":1,"ShopName":"好办易","ShopLogo":"http://192.168.0
     * .88:8089/Storage/selleradmin/1/Logo/201606230951305731001s.jpg","OrderTotalPrice":12866,"ProductCount":3,
     * "SatisfySendMoney":100,"SatisfySendPrice":1000,"VouchersList":[],"GroupSellProducts":[{"GroupSellID":17,
     * "GroupSellPrice":6966,"GroupSellName":"测试删除","IsSelected":true,"Products":[{"ProductID":54,
     * "ProductName":"sdfsdf","ProductImage":"http://192.168.0
     * .88:8089/Areas/SellerAdmin/images/2_05112700187137111_240.jpg","ProductPrice":666,"DiscountMoney":334,
     * "Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":2,"AttrName":"工本费","Price":100},
     * {"ShopAttrID":1,"AttrName":"地址费","Price":200}]},{"ProductID":117,"ProductName":"香港公司注册",
     * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg",
     * "ProductPrice":6000,"DiscountMoney":0,"Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[]}]}],
     * "SingleProducts":[{"ProductID":117,"ProductName":"香港公司注册","ProductImage":"http://192.168.0
     * .88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg","ProductPrice":6000,"DiscountMoney":0,
     * "Quantity":1,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[]}]},{"ShopID":17,"ShopName":"钱白白",
     * "ShopLogo":"http://192.168.0.88:8089/Strategies/selleradmin/17/Logo/201605231901171943216.png",
     * "OrderTotalPrice":7200,"ProductCount":4,"SatisfySendMoney":0,"SatisfySendPrice":0,"VouchersList":[],
     * "GroupSellProducts":[{"GroupSellID":28,"GroupSellPrice":3800,"GroupSellName":"创业组合","IsSelected":true,
     * "Products":[{"ProductID":115,"ProductName":"创业组合-工商注册","ProductImage":"http://192.168.0
     * .88:8089/Storage/selleradmin/17/Album/2016052615435617413417.jpg","ProductPrice":1000,"DiscountMoney":0,
     * "Quantity":1,"GroupSellID":28,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":25,"AttrName":"地址服务费",
     * "Price":500},{"ShopAttrID":26,"AttrName":"数字证书工本费","Price":300}]},{"ProductID":116,"ProductName":"创业组合-代理记帐",
     * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/17/Album/2016052615452384897117.jpg",
     * "ProductPrice":2000,"DiscountMoney":1000,"Quantity":1,"GroupSellID":28,"IsSelected":true,
     * "ShopAttrList":[]}]}],"SingleProducts":[{"ProductID":114,"ProductName":"工商变更","ProductImage":"http://192.168.0
     * .88:8089/Strategies/selleradmin/17/Album/201605231904021334112.jpg","ProductPrice":1000,"DiscountMoney":0,
     * "Quantity":1,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":25,"AttrName":"地址服务费",
     * "Price":1000},{"ShopAttrID":26,"AttrName":"数字证书工本费","Price":200}]},{"ProductID":121,"ProductName":"全面公司注册",
     * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/17/Album/2016060211185590813617.jpg",
     * "ProductPrice":500,"DiscountMoney":0,"Quantity":1,"GroupSellID":0,"IsSelected":true,
     * "ShopAttrList":[{"ShopAttrID":25,"AttrName":"地址服务费","Price":100},{"ShopAttrID":26,"AttrName":"数字证书工本费",
     * "Price":100},{"ShopAttrID":49,"AttrName":"各项工本费","Price":100},{"ShopAttrID":48,"AttrName":"比较多数字证书工本费",
     * "Price":100},{"ShopAttrID":47,"AttrName":"数字证书工本费地数字证书工本费","Price":100},{"ShopAttrID":46,"AttrName":"数字证书工本费",
     * "Price":100},{"ShopAttrID":45,"AttrName":"数字证书工本费","Price":100}]}]}]
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
        private double totalPrice;
        private int totalCount;
        private double discountMoney;
        private List<?> contactList;
        private List<?> redEnvelopeList;
        /**
         * ShopID : 1
         * ShopName : 好办易
         * ShopLogo : http://192.168.0.88:8089/Storage/selleradmin/1/Logo/201606230951305731001s.jpg
         * OrderTotalPrice : 12866.0
         * ProductCount : 3
         * SatisfySendMoney : 100.0
         * SatisfySendPrice : 1000.0
         * VouchersList : []
         * GroupSellProducts : [{"GroupSellID":17,"GroupSellPrice":6966,"GroupSellName":"测试删除","IsSelected":true,
         * "Products":[{"ProductID":54,"ProductName":"sdfsdf","ProductImage":"http://192.168.0
         * .88:8089/Areas/SellerAdmin/images/2_05112700187137111_240.jpg","ProductPrice":666,"DiscountMoney":334,
         * "Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":2,"AttrName":"工本费",
         * "Price":100},{"ShopAttrID":1,"AttrName":"地址费","Price":200}]},{"ProductID":117,"ProductName":"香港公司注册",
         * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg",
         * "ProductPrice":6000,"DiscountMoney":0,"Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[]}]}]
         * SingleProducts : [{"ProductID":117,"ProductName":"香港公司注册","ProductImage":"http://192.168.0
         * .88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg","ProductPrice":6000,"DiscountMoney":0,
         * "Quantity":1,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[]}]
         */

        private List<ListBean> list;

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public double getDiscountMoney() {
            return discountMoney;
        }

        public void setDiscountMoney(double discountMoney) {
            this.discountMoney = discountMoney;
        }

        public List<?> getContactList() {
            return contactList;
        }

        public void setContactList(List<?> contactList) {
            this.contactList = contactList;
        }

        public List<?> getRedEnvelopeList() {
            return redEnvelopeList;
        }

        public void setRedEnvelopeList(List<?> redEnvelopeList) {
            this.redEnvelopeList = redEnvelopeList;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private int ShopID;
            private String ShopName;
            private String ShopLogo;
            private double OrderTotalPrice;
            private int ProductCount;
            private double SatisfySendMoney;
            private double SatisfySendPrice;
            private List<?> VouchersList;
            /**
             * GroupSellID : 17
             * GroupSellPrice : 6966.0
             * GroupSellName : 测试删除
             * IsSelected : true
             * Products : [{"ProductID":54,"ProductName":"sdfsdf","ProductImage":"http://192.168.0
             * .88:8089/Areas/SellerAdmin/images/2_05112700187137111_240.jpg","ProductPrice":666,"DiscountMoney":334,
             * "Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":2,"AttrName":"工本费",
             * "Price":100},{"ShopAttrID":1,"AttrName":"地址费","Price":200}]},{"ProductID":117,"ProductName":"香港公司注册",
             * "ProductImage":"http://192.168.0.88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg",
             * "ProductPrice":6000,"DiscountMoney":0,"Quantity":1,"GroupSellID":17,"IsSelected":true,"ShopAttrList":[]}]
             */

            private List<GroupSellProductsBean> GroupSellProducts;
            /**
             * ProductID : 117
             * ProductName : 香港公司注册
             * ProductImage : http://192.168.0.88:8089/Storage/selleradmin/1/Album/201605271700175712811.jpg
             * ProductPrice : 6000.0
             * DiscountMoney : 0.0
             * Quantity : 1
             * GroupSellID : 0
             * IsSelected : true
             * ShopAttrList : []
             */

            private List<SingleProductsBean> SingleProducts;

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

            public double getOrderTotalPrice() {
                return OrderTotalPrice;
            }

            public void setOrderTotalPrice(double OrderTotalPrice) {
                this.OrderTotalPrice = OrderTotalPrice;
            }

            public int getProductCount() {
                return ProductCount;
            }

            public void setProductCount(int ProductCount) {
                this.ProductCount = ProductCount;
            }

            public double getSatisfySendMoney() {
                return SatisfySendMoney;
            }

            public void setSatisfySendMoney(double SatisfySendMoney) {
                this.SatisfySendMoney = SatisfySendMoney;
            }

            public double getSatisfySendPrice() {
                return SatisfySendPrice;
            }

            public void setSatisfySendPrice(double SatisfySendPrice) {
                this.SatisfySendPrice = SatisfySendPrice;
            }

            public List<?> getVouchersList() {
                return VouchersList;
            }

            public void setVouchersList(List<?> VouchersList) {
                this.VouchersList = VouchersList;
            }

            public List<GroupSellProductsBean> getGroupSellProducts() {
                return GroupSellProducts;
            }

            public void setGroupSellProducts(List<GroupSellProductsBean> GroupSellProducts) {
                this.GroupSellProducts = GroupSellProducts;
            }

            public List<SingleProductsBean> getSingleProducts() {
                return SingleProducts;
            }

            public void setSingleProducts(List<SingleProductsBean> SingleProducts) {
                this.SingleProducts = SingleProducts;
            }

            public static class GroupSellProductsBean {
                private int GroupSellID;
                private double GroupSellPrice;
                private String GroupSellName;
                private boolean IsSelected;
                /**
                 * ProductID : 54
                 * ProductName : sdfsdf
                 * ProductImage : http://192.168.0.88:8089/Areas/SellerAdmin/images/2_05112700187137111_240.jpg
                 * ProductPrice : 666.0
                 * DiscountMoney : 334.0
                 * Quantity : 1
                 * GroupSellID : 17
                 * IsSelected : true
                 * ShopAttrList : [{"ShopAttrID":2,"AttrName":"工本费","Price":100},{"ShopAttrID":1,"AttrName":"地址费",
                 * "Price":200}]
                 */

                private List<ProductsBean> Products;

                public int getGroupSellID() {
                    return GroupSellID;
                }

                public void setGroupSellID(int GroupSellID) {
                    this.GroupSellID = GroupSellID;
                }

                public double getGroupSellPrice() {
                    return GroupSellPrice;
                }

                public void setGroupSellPrice(double GroupSellPrice) {
                    this.GroupSellPrice = GroupSellPrice;
                }

                public String getGroupSellName() {
                    return GroupSellName;
                }

                public void setGroupSellName(String GroupSellName) {
                    this.GroupSellName = GroupSellName;
                }

                public boolean isIsSelected() {
                    return IsSelected;
                }

                public void setIsSelected(boolean IsSelected) {
                    this.IsSelected = IsSelected;
                }

                public List<ProductsBean> getProducts() {
                    return Products;
                }

                public void setProducts(List<ProductsBean> Products) {
                    this.Products = Products;
                }

                public static class ProductsBean {
                    private int ProductID;
                    private String ProductName;
                    private String ProductImage;
                    private double ProductPrice;
                    private double DiscountMoney;
                    private int Quantity;
                    private int GroupSellID;
                    private boolean IsSelected;
                    /**
                     * ShopAttrID : 2
                     * AttrName : 工本费
                     * Price : 100.0
                     */

                    private List<ShopAttrListBean> ShopAttrList;

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

                    public String getProductImage() {
                        return ProductImage;
                    }

                    public void setProductImage(String ProductImage) {
                        this.ProductImage = ProductImage;
                    }

                    public double getProductPrice() {
                        return ProductPrice;
                    }

                    public void setProductPrice(double ProductPrice) {
                        this.ProductPrice = ProductPrice;
                    }

                    public double getDiscountMoney() {
                        return DiscountMoney;
                    }

                    public void setDiscountMoney(double DiscountMoney) {
                        this.DiscountMoney = DiscountMoney;
                    }

                    public int getQuantity() {
                        return Quantity;
                    }

                    public void setQuantity(int Quantity) {
                        this.Quantity = Quantity;
                    }

                    public int getGroupSellID() {
                        return GroupSellID;
                    }

                    public void setGroupSellID(int GroupSellID) {
                        this.GroupSellID = GroupSellID;
                    }

                    public boolean isIsSelected() {
                        return IsSelected;
                    }

                    public void setIsSelected(boolean IsSelected) {
                        this.IsSelected = IsSelected;
                    }

                    public List<ShopAttrListBean> getShopAttrList() {
                        return ShopAttrList;
                    }

                    public void setShopAttrList(List<ShopAttrListBean> ShopAttrList) {
                        this.ShopAttrList = ShopAttrList;
                    }

                    public static class ShopAttrListBean {
                        private int ShopAttrID;
                        private String AttrName;
                        private double Price;

                        public int getShopAttrID() {
                            return ShopAttrID;
                        }

                        public void setShopAttrID(int ShopAttrID) {
                            this.ShopAttrID = ShopAttrID;
                        }

                        public String getAttrName() {
                            return AttrName;
                        }

                        public void setAttrName(String AttrName) {
                            this.AttrName = AttrName;
                        }

                        public double getPrice() {
                            return Price;
                        }

                        public void setPrice(double Price) {
                            this.Price = Price;
                        }
                    }
                }
            }

            public static class SingleProductsBean {
                private int ProductID;
                private String ProductName;
                private String ProductImage;
                private double ProductPrice;
                private double DiscountMoney;
                private int Quantity;
                private int GroupSellID;
                private boolean IsSelected;
                private List<?> ShopAttrList;

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

                public String getProductImage() {
                    return ProductImage;
                }

                public void setProductImage(String ProductImage) {
                    this.ProductImage = ProductImage;
                }

                public double getProductPrice() {
                    return ProductPrice;
                }

                public void setProductPrice(double ProductPrice) {
                    this.ProductPrice = ProductPrice;
                }

                public double getDiscountMoney() {
                    return DiscountMoney;
                }

                public void setDiscountMoney(double DiscountMoney) {
                    this.DiscountMoney = DiscountMoney;
                }

                public int getQuantity() {
                    return Quantity;
                }

                public void setQuantity(int Quantity) {
                    this.Quantity = Quantity;
                }

                public int getGroupSellID() {
                    return GroupSellID;
                }

                public void setGroupSellID(int GroupSellID) {
                    this.GroupSellID = GroupSellID;
                }

                public boolean isIsSelected() {
                    return IsSelected;
                }

                public void setIsSelected(boolean IsSelected) {
                    this.IsSelected = IsSelected;
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
