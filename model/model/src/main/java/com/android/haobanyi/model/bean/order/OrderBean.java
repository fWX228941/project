package com.android.haobanyi.model.bean.order;

/**
 * Created by fWX228941 on 2016/8/17.
 *
 * @作者: 付敏
 * @创建日期：2016/08/17
 * @邮箱：466566941@qq.com
 * @当前文件描述：订单详情
 * 因为字段太多，所以最少使用字段原则
 * 除了：
 * 店铺/服务/电话是可以点击的，其余的都先暂时不要
 */
public class OrderBean {
    private int Status;
    private String ContactName;
    private String ContactDistrict;
    private String ContactAddress;
    // 先手机号M 后电话号P 再然后没有了
    private String ContactPhone;
    private String ContactMobile;
    private long ShopID;
    private String ShopName;
    private String ShopLogo;
    private long OrderExtendID;
    private long ProductID;
    private String ProductName;
    private String ProductImage;
    private String ProductPrice;
    private String Price;
    private int PayMode;
    private int InvoiceType;
    private String Title;
    private String Content;
    private String CreateDate;
    private String BuyerMsg;
    private String Quantity;






    public String getBuyerMsg() {
        return BuyerMsg;
    }

    public void setBuyerMsg(String buyerMsg) {
        BuyerMsg = buyerMsg;
    }



    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }


    /*这个字段真是多*/
    public OrderBean(){

    }
    public OrderBean(int status, String contactName, String contactDistrict, String contactAddress, String
            contactPhone, String contactMobile, String buyerMsg, long shopID, String shopName, String shopLogo, long orderExtendID,
                     long productID, String productName, String productImage, String productPrice,String quantity, String price, int
                             payMode, int invoiceType, String title, String content, String createDate) {
        Status = status;
        ContactName = contactName;
        ContactDistrict = contactDistrict;
        ContactAddress = contactAddress;
        ContactPhone = contactPhone;
        ContactMobile = contactMobile;
        BuyerMsg =buyerMsg;
        ShopID = shopID;
        ShopName = shopName;
        ShopLogo = shopLogo;
        OrderExtendID = orderExtendID;
        ProductID = productID;
        ProductName = productName;
        ProductImage = productImage;
        ProductPrice = productPrice;
        Quantity = quantity;
        Price = price;
        PayMode = payMode;
        InvoiceType = invoiceType;
        Title = title;
        Content = content;
        CreateDate = createDate;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactDistrict() {
        return ContactDistrict;
    }

    public void setContactDistrict(String contactDistrict) {
        ContactDistrict = contactDistrict;
    }

    public String getContactAddress() {
        return ContactAddress;
    }

    public void setContactAddress(String contactAddress) {
        ContactAddress = contactAddress;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public String getContactMobile() {
        return ContactMobile;
    }

    public void setContactMobile(String contactMobile) {
        ContactMobile = contactMobile;
    }

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

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getPayMode() {
        return PayMode;
    }

    public void setPayMode(int payMode) {
        PayMode = payMode;
    }

    public int getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        InvoiceType = invoiceType;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }
    // 还差一个订单编号 到时再添加
    //其他两个优惠暂时先放着和商品总价

}
