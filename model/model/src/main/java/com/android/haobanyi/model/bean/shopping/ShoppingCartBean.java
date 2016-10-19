package com.android.haobanyi.model.bean.shopping;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/6.
 *
 * @作者: 付敏
 * @创建日期：2016/06/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：从 ‘获取购物车列表’ GetVerifyShoppingCart 比 getShoppingCart 多了一些字段，包括联系人信息，代金券等
 * 缺少的字段都是为？ 后续数据完善的时候添加数据源
 *  这个就以GetVerifyShoppingCart为标准
 *  过滤出来才是，没有数据的就用假数据集来填充并且说明下
 *
 *  套餐的场景先放一边
 *  确认订单也只显示有的
 *  增删改查的字段也是可以添加并且分类的
 *
 */
public class ShoppingCartBean {


    /**  场景一：
     * code : 101
     * message : 获取成功
     * data : {"totalPrice":0,"totalCount":0,"discountMoney":0,"contactList":[],"redEnvelopeList":[],"list":[]}
     private List<?> contactList;
     private List<?> redEnvelopeList;
     private List<?> list;
     *  一个是联系人列表
     * 一个是平台红包
     * 场景二：
     * 增加了一个两个服务
     *
     *
     *
     * 在有订单的情况下
     */
    /*
    *  果然是同步更新的
    *  vertify
全选：{"code":101,"message":"获取成功","data":{"totalPrice":20300.00,"totalCount":2,"discountMoney":100.00,"contactList":[],"redEnvelopeList":[],"list":[{"ShopID":19,"ShopName":"好办易","ShopLogo":"http://192.168.0.10:8090/Strategies/selleradmin/19/Logo/201605181655437295955.png","OrderTotalPrice":14400.00,"ProductCount":1,"SatisfySendMoney":0.0,"SatisfySendPrice":0.0,"VouchersList":[],"GroupSellProducts":[],"SingleProducts":[{"ProductID":112,"ProductName":"为您提供最专业的财税服务","ProductImage":"http://192.168.0.10:8090/Strategies/selleradmin/19/Album/201605231154274717408.jpg","ProductPrice":7200.00,"DiscountMoney":0.00,"Quantity":2,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":40,"AttrName":"小规模纳税人","Price":7200.00},{"ShopAttrID":39,"AttrName":"一般纳税人","Price":12000.00}]}]},{"ShopID":1,"ShopName":"好办易","ShopLogo":"http://192.168.0.10:8090/Storage/selleradmin/1/Logo/201606230951305731001s.jpg","OrderTotalPrice":5900.00,"ProductCount":1,"SatisfySendMoney":100.00,"SatisfySendPrice":1000.00,"VouchersList":[],"GroupSellProducts":[],"SingleProducts":[{"ProductID":117,"ProductName":"香港公司注册","ProductImage":"http://192.168.0.10:8090/Storage/selleradmin/1/Album/201605271700175712811.jpg","ProductPrice":6000.00,"DiscountMoney":0.00,"Quantity":1,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[]}]}]}}
部分勾选：{"code":101,"message":"获取成功","data":{"totalPrice":14400.00,"totalCount":1,"discountMoney":0.0,"contactList":[],"redEnvelopeList":[],"list":[{"ShopID":19,"ShopName":"好办易","ShopLogo":"http://192.168.0.10:8090/Strategies/selleradmin/19/Logo/201605181655437295955.png","OrderTotalPrice":14400.00,"ProductCount":1,"SatisfySendMoney":0.0,"SatisfySendPrice":0.0,"VouchersList":[],"GroupSellProducts":[],"SingleProducts":[{"ProductID":112,"ProductName":"为您提供最专业的财税服务","ProductImage":"http://192.168.0.10:8090/Strategies/selleradmin/19/Album/201605231154274717408.jpg","ProductPrice":7200.00,"DiscountMoney":0.00,"Quantity":2,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":40,"AttrName":"小规模纳税人","Price":7200.00},{"ShopAttrID":39,"AttrName":"一般纳税人","Price":12000.00}]}]}]}}

全选：{"code":101,"message":"获取成功","data":{"totalPrice":20300.00,"totalCount":2,"list":[{"ShopID":19,"ShopName":"好办易","ShopLogo":"http://192.168.0.10:8090/Strategies/selleradmin/19/Logo/201605181655437295955.png","ProductCount":1,"OrderTotalPrice":14400.00,"GroupSellProducts":[],"SingleProducts":[{"ProductID":112,"ProductName":"为您提供最专业的财税服务","ProductImage":"http://192.168.0.10:8090/Strategies/selleradmin/19/Album/201605231154274717408.jpg","ProductPrice":7200.00,"DiscountMoney":0.00,"Quantity":2,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":40,"AttrName":"小规模纳税人","Price":7200.00},{"ShopAttrID":39,"AttrName":"一般纳税人","Price":12000.00}]}]},{"ShopID":1,"ShopName":"好办易","ShopLogo":"http://192.168.0.10:8090/Storage/selleradmin/1/Logo/201606230951305731001s.jpg","ProductCount":1,"OrderTotalPrice":5900.00,"GroupSellProducts":[],"SingleProducts":[{"ProductID":117,"ProductName":"香港公司注册","ProductImage":"http://192.168.0.10:8090/Storage/selleradmin/1/Album/201605271700175712811.jpg","ProductPrice":6000.00,"DiscountMoney":0.00,"Quantity":1,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[]}]}]}}
部分勾选：{"code":101,"message":"获取成功","data":{"totalPrice":14400.00,"totalCount":1,"list":[{"ShopID":19,"ShopName":"好办易","ShopLogo":"http://192.168.0.10:8090/Strategies/selleradmin/19/Logo/201605181655437295955.png","ProductCount":1,"OrderTotalPrice":14400.00,"GroupSellProducts":[],"SingleProducts":[{"ProductID":112,"ProductName":"为您提供最专业的财税服务","ProductImage":"http://192.168.0.10:8090/Strategies/selleradmin/19/Album/201605231154274717408.jpg","ProductPrice":7200.00,"DiscountMoney":0.00,"Quantity":2,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":40,"AttrName":"小规模纳税人","Price":7200.00},{"ShopAttrID":39,"AttrName":"一般纳税人","Price":12000.00}]}]},{"ShopID":1,"ShopName":"好办易","ShopLogo":"http://192.168.0.10:8090/Storage/selleradmin/1/Logo/201606230951305731001s.jpg","ProductCount":0,"OrderTotalPrice":0.0,"GroupSellProducts":[],"SingleProducts":[{"ProductID":117,"ProductName":"香港公司注册","ProductImage":"http://192.168.0.10:8090/Storage/selleradmin/1/Album/201605271700175712811.jpg","ProductPrice":6000.00,"DiscountMoney":0.00,"Quantity":1,"GroupSellID":0,"IsSelected":false,"ShopAttrList":[]}]}]}}

     场景三:对于无论是套餐还是非套餐，对于加1还是减1 都是
    *
    *
    *
    * */

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
        /*=================getShoppingCart==========================*/
        private String totalPrice; //
        private String totalCount;
        private List<ListBean> list;
        /*==================getShoppingCart========================*/

        //以下三个字段是确认后的购物车列表
        /*==================getVerifyShoppingCart========================*/
        private String discountMoney;
        private List<ContactBean> contactList;
        private List<RedEnvelopeBean> redEnvelopeList;
        /*==================getVerifyShoppingCart========================*/


        private List<VouchersBean> VouchersList;//  这个暂时可以删除

        public List<VouchersBean> getVouchersList() {
            return VouchersList;
        }

        public void setVouchersList(List<VouchersBean> vouchersList) {
            VouchersList = vouchersList;
        }


        /**
         * ShopID : 19
         * ShopName : 好办易
         * ShopLogo : http://192.168.0.10:8090/Strategies/selleradmin/19/Logo/201605181655437295955.png
         * OrderTotalPrice : 14400.0
         * ProductCount : 1
         * SatisfySendMoney : 0.0
         * SatisfySendPrice : 0.0
         * VouchersList : []
         * GroupSellProducts : []
         * SingleProducts : [{"ProductID":112,"ProductName":"为您提供最专业的财税服务","ProductImage":"http://192.168.0
         * .10:8090/Strategies/selleradmin/19/Album/201605231154274717408.jpg","ProductPrice":7200,"DiscountMoney":0,
         * "Quantity":2,"GroupSellID":0,"IsSelected":true,"ShopAttrList":[{"ShopAttrID":40,"AttrName":"小规模纳税人",
         * "Price":7200},{"ShopAttrID":39,"AttrName":"一般纳税人","Price":12000}]}]
         * ShopAttrList 这个是单个产品内的其他服务
         *
         *
         */



        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getDiscountMoney() {
            return discountMoney;
        }

        public void setDiscountMoney(String discountMoney) {
            this.discountMoney = discountMoney;
        }

        public List<ContactBean> getContactList() {
            return contactList;
        }

        public void setContactList(List<ContactBean> contactList) {
            this.contactList = contactList;
        }

        public List<RedEnvelopeBean> getRedEnvelopeList() {
            return redEnvelopeList;
        }

        public void setRedEnvelopeList(List<RedEnvelopeBean> redEnvelopeList) {
            this.redEnvelopeList = redEnvelopeList;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
        /*分为套餐和非套餐*/
        public static class ListBean {
            /*=================getShoppingCart==========================*/
            private long ShopID;
            private String ShopName;
            private String ShopLogo;
            private String OrderTotalPrice;
            private String ProductCount;   // 这两个属性和TotalPrice  TotalCount 重复了，先不管是干嘛用的，其实是变化的难道。初始状态的值是一致的
            private List<GroupSellProductsBean> GroupSellProducts;
            private List<SingleProductsBean> SingleProducts;
            /*=================getShoppingCart==========================*/


            /*==================getVerifyShoppingCart========================*/
            private String SatisfySendMoney;
            private String SatisfySendPrice;
            private List<VouchersBean> VouchersList;//代金券
            /*==================getVerifyShoppingCart========================*/

            public long getShopID() {
                return ShopID;
            }

            public void setShopID(long ShopID) {
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

            public String getOrderTotalPrice() {
                return OrderTotalPrice;
            }

            public void setOrderTotalPrice(String OrderTotalPrice) {
                this.OrderTotalPrice = OrderTotalPrice;
            }

            public String getProductCount() {
                return ProductCount;
            }

            public void setProductCount(String ProductCount) {
                this.ProductCount = ProductCount;
            }

            public String getSatisfySendMoney() {
                return SatisfySendMoney;
            }

            public void setSatisfySendMoney(String SatisfySendMoney) {
                this.SatisfySendMoney = SatisfySendMoney;
            }

            public String getSatisfySendPrice() {
                return SatisfySendPrice;
            }

            public void setSatisfySendPrice(String SatisfySendPrice) {
                this.SatisfySendPrice = SatisfySendPrice;
            }

            public List<VouchersBean> getVouchersList() {
                return VouchersList;
            }

            public void setVouchersList(List<VouchersBean> VouchersList) {
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
            /*套餐*/
            public static class GroupSellProductsBean {
                /*=================getShoppingCart==========================*/
                private long GroupSellID;
                private double GroupSellPrice;
                private String GroupSellName;
                private boolean IsSelected;
                private List<ProductsBean> Products;
                /*=================getShoppingCart==========================*/

                public long getGroupSellID() {
                    return GroupSellID;
                }

                public void setGroupSellID(long GroupSellID) {
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
                    /*=================getShoppingCart==========================*/
                    private long ProductID;
                    private String ProductName;
                    private String ProductImage;
                    private double ProductPrice;
                    private double DiscountMoney;
                    private int Quantity;
                    private long GroupSellID;
                    private boolean IsSelected;
                    private List<ShopAttrListBean> ShopAttrList;
                    /*=================getShoppingCart==========================*/


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

                    public long getGroupSellID() {
                        return GroupSellID;
                    }

                    public void setGroupSellID(long GroupSellID) {
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
                        /*=================getShoppingCart==========================*/
                        private long ShopAttrID;
                        private String AttrName;
                        private double Price;
                        private boolean IsSelected;
                        /*=================getShoppingCart==========================*/



                        public boolean isSelected() {
                            return IsSelected;
                        }

                        public void setIsSelected(boolean isSelected) {
                            IsSelected = isSelected;
                        }
                        public long getShopAttrID() {
                            return ShopAttrID;
                        }

                        public void setShopAttrID(long ShopAttrID) {
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
            /*非套餐*/
            public static class SingleProductsBean {
                /*=================getShoppingCart==========================*/
                private long ProductID;
                private String ProductName;
                private String ProductImage;
                private double ProductPrice;
                private double DiscountMoney;
                private int Quantity;
                private long GroupSellID;
                private boolean IsSelected;
                /*==================getVerifyShoppingCart========================*/
                private List<ShopAttrListBean> ShopAttrList;
                /*==================getVerifyShoppingCart========================*/

                private List<VouchersBean> VouchersList;// 暂时非套餐才存在优惠券
                /*=================getShoppingCart==========================*/

                public List<VouchersBean> getVouchersList() {
                    return VouchersList;
                }

                public void setVouchersList(List<VouchersBean> vouchersList) {
                    VouchersList = vouchersList;
                }
                private String OrderTotalPrice;
                private String ProductCount;
                public String getProductCount() {
                    return ProductCount;
                }

                public void setProductCount(String productCount) {
                    ProductCount = productCount;
                }

                public boolean isSelected() {
                    return IsSelected;
                }

                public String getOrderTotalPrice() {
                    return OrderTotalPrice;
                }

                public void setOrderTotalPrice(String orderTotalPrice) {
                    OrderTotalPrice = orderTotalPrice;
                }


                /**
                 * ShopAttrID : 40
                 * AttrName : 小规模纳税人
                 * Price : 7200.0
                 */



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

                public long getGroupSellID() {
                    return GroupSellID;
                }

                public void setGroupSellID(long GroupSellID) {
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
                /*其他服务列表暂时不作考虑，这个真的好久了，我都快忘记了，我去*/
                public static class ShopAttrListBean {
                    /*=================getShoppingCart==========================*/
                    private long ShopAttrID;
                    private String AttrName;
                    private double Price;
                    private boolean IsSelected;
                    /*=================getShoppingCart==========================*/


                    public boolean isSelected() {
                        return IsSelected;
                    }

                    public void setIsSelected(boolean isSelected) {
                        IsSelected = isSelected;
                    }
                    public long getShopAttrID() {
                        return ShopAttrID;
                    }

                    public void setShopAttrID(long ShopAttrID) {
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

                public static class VouchersBean {
                    private long VouchersID;
                    private String Price;
                    private boolean IsSelected;

                    public long getVouchersID() {
                        return VouchersID;
                    }

                    public void setVouchersID(long vouchersID) {
                        VouchersID = vouchersID;
                    }

                    public String getPrice() {
                        return Price;
                    }

                    public void setPrice(String price) {
                        Price = price;
                    }

                    public boolean isSelected() {
                        return IsSelected;
                    }

                    public void setIsSelected(boolean isSelected) {
                        IsSelected = isSelected;
                    }
                }

            }
            public static class VouchersBean {
                private long VouchersID;
                private String Price;
                private boolean IsSelected;

                public long getVouchersID() {
                    return VouchersID;
                }

                public void setVouchersID(long vouchersID) {
                    VouchersID = vouchersID;
                }

                public String getPrice() {
                    return Price;
                }

                public void setPrice(String price) {
                    Price = price;
                }

                public boolean isSelected() {
                    return IsSelected;
                }

                public void setIsSelected(boolean isSelected) {
                    IsSelected = isSelected;
                }
            }

        }

        public static class ContactBean {
            private long ContactManageID;
            private String ContactName;
            private String ContactAddress;
            private String ContactMobile;
            private String ContactPhone;
            private int IsDefault; //是否默认1 是


            public long getContactManageID() {
                return ContactManageID;
            }

            public void setContactManageID(long contactManageID) {
                ContactManageID = contactManageID;
            }

            public String getContactName() {
                return ContactName;
            }

            public void setContactName(String contactName) {
                ContactName = contactName;
            }

            public String getContactAddress() {
                return ContactAddress;
            }

            public void setContactAddress(String contactAddress) {
                ContactAddress = contactAddress;
            }

            public String getContactMobile() {
                return ContactMobile;
            }

            public void setContactMobile(String contactMobile) {
                ContactMobile = contactMobile;
            }

            public String getContactPhone() {
                return ContactPhone;
            }

            public void setContactPhone(String contactPhone) {
                ContactPhone = contactPhone;
            }

            public int getIsDefault() {
                return IsDefault;
            }

            public void setIsDefault(int isDefault) {
                IsDefault = isDefault;
            }
        }
        /*平台红包*/
        public static class RedEnvelopeBean {
            private long RedEnvelopeID;
            private String Price;
            private boolean IsSelected;


            public long getRedEnvelopeID() {
                return RedEnvelopeID;
            }

            public void setRedEnvelopeID(long redEnvelopeID) {
                RedEnvelopeID = redEnvelopeID;
            }

            public String getPrice() {
                return Price;
            }

            public void setPrice(String price) {
                Price = price;
            }

            public boolean isSelected() {
                return IsSelected;
            }

            public void setIsSelected(boolean isSelected) {
                IsSelected = isSelected;
            }
        }

        private class VouchersBean {
            private long VouchersTemplateID;
            private String Price;
            private String StartTime;
            private String EndTime;
            private String Limit; //限额
            private boolean IsExist;
            private int EachLimit;

            public long getVouchersTemplateID() {
                return VouchersTemplateID;
            }

            public void setVouchersTemplateID(long vouchersTemplateID) {
                VouchersTemplateID = vouchersTemplateID;
            }

            public String getPrice() {
                return Price;
            }

            public void setPrice(String price) {
                Price = price;
            }

            public String getStartTime() {
                return StartTime;
            }

            public void setStartTime(String startTime) {
                StartTime = startTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public void setEndTime(String endTime) {
                EndTime = endTime;
            }

            public String getLimit() {
                return Limit;
            }

            public void setLimit(String limit) {
                Limit = limit;
            }

            public boolean isExist() {
                return IsExist;
            }

            public void setIsExist(boolean isExist) {
                IsExist = isExist;
            }

            public int getEachLimit() {
                return EachLimit;
            }

            public void setEachLimit(int eachLimit) {
                EachLimit = eachLimit;
            }
        }
    }
}
