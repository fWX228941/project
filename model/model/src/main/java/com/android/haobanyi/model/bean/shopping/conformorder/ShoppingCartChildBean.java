package com.android.haobanyi.model.bean.shopping.conformorder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/28.
 *
 * @作者: 付敏
 * @创建日期：2016/06/28
 * @邮箱：466566941@qq.com
 * @当前文件描述：ShoppingCartBean 分离出来  关联界面 “确认订单” 关联类：ConformOrderActivity
 *  购物车也用这个接口
 *  当存在不确定因素时，以网站为标准
 *  其他字段再补充
 *  针对不同的业务拼装出不同的bean
 */
public class ShoppingCartChildBean {
    /*
    * "SatisfySendMoney":100.00,"SatisfySendPrice":1000.00,  满1000减100  优惠活动这部分放在店铺详情中再来深究
    *
    * */
    /*=================getShoppingCart==========================*/
    private long ProductID;// 服务的ID
    private String ProductName; // 服务产品名
    private String ProductImage; //服务图片
    private double ProductPrice; // 单个服务的单价
    private double DiscountMoney;
    private int Quantity; //购买单个服务的数量
    private boolean IsSelected;  //服务是否被勾选
    private long GroupSellID; // 标识量用于判断是否是套餐/*单个服务的总价= 单个服务的数量*单个服务的单价*/
    private List<ShopAttrListBean> ShopAttrList;

    /*=================getShoppingCart==========================*/

    /*这个用在确认订单中，而不是用在购物车中*/
    private String OrderTotalPrice; //单个店所有服务的合计价格，这个是使用了店铺代金券的总价 ， 这个商家的价格是包含套餐和非套餐合计的价格
    private String ProductCount;//单个店所有服务的数量/*bug:就是这个值与网站上的不一致*/
    private List<VouchersBean> VouchersList;
    public static final  int STATUS_INVALID=0;
    public static final  int STATUS_VALID=1;
    private String name;
    /** 商品宣传图片 */

    private String imageLogo;
//* 商品规格，商品描述

    private String desc;
//* 原价，市场价

    private double price;
//* 现价，折扣价

    private double discountPrice;
//购买数量

    private int count;
//* 状态 ，两种状态之间进行切换，对应的就是视图的切换

    private int status;
//* 是否被选中

    private boolean isChecked;
//* 是否是编辑状态

    private boolean isEditing;
//因为涉及到套餐，所以还需要添加一个tag状态标识量

    private String SatisfySendMoney;

    private String SatisfySendPrice;
    public List<VouchersBean> getVouchersList() {
        return VouchersList;
    }

    public void setVouchersList(List<VouchersBean> vouchersList) {
        VouchersList = vouchersList;
    }

    public String getSatisfySendMoney() {
        return SatisfySendMoney;
    }

    public void setSatisfySendMoney(String satisfySendMoney) {
        SatisfySendMoney = satisfySendMoney;
    }

    public String getSatisfySendPrice() {
        return SatisfySendPrice;
    }

    public void setSatisfySendPrice(String satisfySendPrice) {
        SatisfySendPrice = satisfySendPrice;
    }



    /*确认订单就用这个接口*/
    public ShoppingCartChildBean(long productID,String productName, String productImage, double productPrice, double
            discountMoney, int quantity, boolean isSelected, long groupSellID, String orderTotalPrice, String
            productCount, String satisfySendMoney, String satisfySendPrice, List<VouchersBean> vouchersList) {
        ProductID = productID;
        ProductName = productName;
        ProductImage = productImage;
        ProductPrice = productPrice;
        DiscountMoney = discountMoney;
        Quantity = quantity;
        IsSelected = isSelected;
        GroupSellID = groupSellID;
        OrderTotalPrice = orderTotalPrice;
        ProductCount = productCount;
        SatisfySendMoney =satisfySendMoney;
        SatisfySendPrice =satisfySendPrice;
        VouchersList = vouchersList;
    }

    //非套餐
    public ShoppingCartChildBean(String productName, String productImage, double productPrice, double
            discountMoney, int quantity, boolean isSelected, long groupSellID) {
        ProductName = productName;
        ProductImage = productImage;
        ProductPrice = productPrice;
        DiscountMoney = discountMoney;
        Quantity = quantity;
        IsSelected = isSelected;
        GroupSellID = groupSellID;

    }
    //套餐购物车

    public ShoppingCartChildBean(long productID, String productName, String productImage, double productPrice, double
            discountMoney, int quantity, boolean isSelected, long groupSellID) {
        ProductID = productID;
        ProductName = productName;
        ProductImage = productImage;
        ProductPrice = productPrice;
        DiscountMoney = discountMoney;
        Quantity = quantity;
        IsSelected = isSelected;
        GroupSellID = groupSellID;

    }

    //先用这个来代替  还有一个desc  这个是其他服务字段，暂时不考虑
    public ShoppingCartChildBean(long productID, String productName, String productImage, double productPrice, double
            discountMoney, int quantity, boolean isSelected, long groupSellID,int status,boolean isEditing) {
        ProductID = productID;
        this.name = productName;
        this.imageLogo = productImage;
        this.price = productPrice;
        this.discountPrice = discountMoney;
        this.count = quantity;
        this.status = status;
        this.isChecked = isSelected;
        GroupSellID = groupSellID;
        this.isEditing = isEditing;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type; //套餐类型
    public ShoppingCartChildBean(long productID, String productName, String productImage, double productPrice, double
            discountMoney, int quantity, boolean isSelected, long groupSellID,int status,boolean isEditing,int Type) {
        ProductID = productID;
        this.name = productName;
        this.imageLogo = productImage;
        this.price = productPrice;
        this.discountPrice = discountMoney;
        this.count = quantity;
        this.status = status;
        this.isChecked = isSelected;
        GroupSellID = groupSellID;
        this.isEditing = isEditing;
        type = Type;
    }

    /*自选服务也添加上*/
    /*======================getShoppingCart===============================*/
    public ShoppingCartChildBean(long productID, String productName, String productImage, double productPrice, double
            discountMoney, int quantity, boolean isSelected, long groupSellID,int status,boolean isEditing,int Type,List<ShopAttrListBean> ShopAttrList) {
        ProductID = productID;
        this.name = productName;
        this.imageLogo = productImage;
        this.price = productPrice;
        this.discountPrice = discountMoney;
        this.count = quantity;
        this.status = status;
        this.isChecked = isSelected;
        GroupSellID = groupSellID;
        this.isEditing = isEditing;
        type = Type;
        this.ShopAttrList = ShopAttrList;
    }
    /*======================getShoppingCart===============================*/

    public String getProductCount() {
        return ProductCount;
    }

    public void setProductCount(String productCount) {
        ProductCount = productCount;
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

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public double getDiscountMoney() {
        return DiscountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        DiscountMoney = discountMoney;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setIsSelected(boolean isSelected) {
        IsSelected = isSelected;
    }

    public long getGroupSellID() {
        return GroupSellID;
    }
//    判断是不是套餐
    public boolean isGroupService() {
        return GroupSellID==0?false:true;
    }
    public void setGroupSellID(long groupSellID) {
        GroupSellID = groupSellID;
    }

    public String getOrderTotalPrice() {
        return OrderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        OrderTotalPrice = orderTotalPrice;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

/*    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }*/

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

/*    @Override
    public String toString() {
        return "ProductID = "+ProductID+"; ProductName = "+ProductName+"; ProductImage = "+ProductImage+"; ProductPrice = "+ProductPrice+
                    "; DiscountMoney = "+DiscountMoney+"; Quantity = "+Quantity+"; IsSelected = "+IsSelected+"; GroupSellID = "+GroupSellID;
    }*/

    public List<ShopAttrListBean> getShopAttrList() {
        return ShopAttrList;
    }

    public void setShopAttrList(List<ShopAttrListBean> ShopAttrList) {
        this.ShopAttrList = ShopAttrList;
    }

    public static class ShopAttrListBean {  //选择其他服务，这个情况也需要考虑

        /*=================getShoppingCart==========================*/
        private long ShopAttrID;
        private String AttrName;
        private double Price;
        private boolean IsSelected;
        private boolean IsChanged = true;//默认都是可变化的

        public boolean isChanged() {
            return IsChanged;
        }

        public void setIsChanged(boolean isChanged) {
            IsChanged = isChanged;
        }


        public ShopAttrListBean(long shopAttrID, String attrName, double price, boolean isSelected) {
            ShopAttrID = shopAttrID;
            AttrName = attrName;
            Price = price;
            IsSelected = isSelected;
        }
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
    /*代金券*/
    public static class VouchersBean {
        private long VouchersID;
        private String Price;
        private boolean IsSelected;

        public VouchersBean(long vouchersID, String price, boolean isSelected) {
            VouchersID = vouchersID;
            Price = price;
            IsSelected = isSelected;
        }

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
