package com.android.haobanyi.model.bean.order;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/17.
 *
 * @作者: 付敏
 * @创建日期：2016/08/17
 * @邮箱：466566941@qq.com
 * @当前文件描述：订单详情
 */
public class OrderResponse {
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
        private long OrderID;
        private long UserID;
        private long ShopID;
        private String ContactName;
        private String ContactDistrict;
        private String ContactAddress;
        private String ContactPhone;
        private String ContactMobile; //逻辑严谨点还是好些
        private String Price;
        private String BuyerMsg; //买家留言
        private int Status;

        public int getPayMode() {
            return PayMode;
        }

        public void setPayMode(int payMode) {
            PayMode = payMode;
        }

        private int PayMode;
        private String ShopName;
        private String ShopLogo;
        private String SSendRulePrice;
        private String SSendRuleMoney;
        private String VouchersPrice;
        private String CreateDate;
        private int InvoiceType;
        private InvoiceInfoBean InvoiceInfo;
        private List<OrderExtendsBean> OrderExtends;

        public long getOrderID() {
            return OrderID;
        }

        public void setOrderID(long orderID) {
            OrderID = orderID;
        }

        public long getUserID() {
            return UserID;
        }

        public void setUserID(long userID) {
            UserID = userID;
        }

        public long getShopID() {
            return ShopID;
        }

        public void setShopID(long shopID) {
            ShopID = shopID;
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

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }

        public String getBuyerMsg() {
            return BuyerMsg;
        }

        public void setBuyerMsg(String buyerMsg) {
            BuyerMsg = buyerMsg;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
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

        public String getSSendRulePrice() {
            return SSendRulePrice;
        }

        public void setSSendRulePrice(String SSendRulePrice) {
            this.SSendRulePrice = SSendRulePrice;
        }

        public String getSSendRuleMoney() {
            return SSendRuleMoney;
        }

        public void setSSendRuleMoney(String SSendRuleMoney) {
            this.SSendRuleMoney = SSendRuleMoney;
        }

        public String getVouchersPrice() {
            return VouchersPrice;
        }

        public void setVouchersPrice(String vouchersPrice) {
            VouchersPrice = vouchersPrice;
        }

        public String getCreateDate() {
            return CreateDate;
        }

        public void setCreateDate(String createDate) {
            CreateDate = createDate;
        }

        public int getInvoiceType() {
            return InvoiceType;
        }

        public void setInvoiceType(int invoiceType) {
            InvoiceType = invoiceType;
        }

        public InvoiceInfoBean getInvoiceInfo() {
            return InvoiceInfo;
        }

        public void setInvoiceInfo(InvoiceInfoBean invoiceInfo) {
            InvoiceInfo = invoiceInfo;
        }

        public List<OrderExtendsBean> getOrderExtends() {
            return OrderExtends;
        }

        public void setOrderExtends(List<OrderExtendsBean> orderExtends) {
            OrderExtends = orderExtends;
        }

        public static class OrderExtendsBean {
            private long OrderExtendID;
            private long ProductID;
            private String ProductName;
            private String ProductImage;
            private String ProductPrice;
            private String Quantity;
            //不确定的东西还可以传入问号

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

        }
        public static class InvoiceInfoBean {
            private String Title;
            private String Content;
            private String ShopLogo;
            private String CompanyName;
            private String IdentityCode;
            private String RegisterAddress;
            private String RegisterPhone;
            private String BankName;
            private String BankAccount;
            private String ConsigneeName;
            private String ConsigneePhone;
            private long ConsigneeProvinceId;
            private long ConsigneeCityId;
            private long ConsigneeDistrictId;
            private String ConsigneeProvinceName;
            private String ConsigneeCityName;
            private String ConsigneeDistrictName;
            private String ConsigneeAddress;

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

            public String getShopLogo() {
                return ShopLogo;
            }

            public void setShopLogo(String shopLogo) {
                ShopLogo = shopLogo;
            }

            public String getCompanyName() {
                return CompanyName;
            }

            public void setCompanyName(String companyName) {
                CompanyName = companyName;
            }

            public String getIdentityCode() {
                return IdentityCode;
            }

            public void setIdentityCode(String identityCode) {
                IdentityCode = identityCode;
            }

            public String getRegisterAddress() {
                return RegisterAddress;
            }

            public void setRegisterAddress(String registerAddress) {
                RegisterAddress = registerAddress;
            }

            public String getRegisterPhone() {
                return RegisterPhone;
            }

            public void setRegisterPhone(String registerPhone) {
                RegisterPhone = registerPhone;
            }

            public String getBankName() {
                return BankName;
            }

            public void setBankName(String bankName) {
                BankName = bankName;
            }

            public String getBankAccount() {
                return BankAccount;
            }

            public void setBankAccount(String bankAccount) {
                BankAccount = bankAccount;
            }

            public String getConsigneeName() {
                return ConsigneeName;
            }

            public void setConsigneeName(String consigneeName) {
                ConsigneeName = consigneeName;
            }

            public String getConsigneePhone() {
                return ConsigneePhone;
            }

            public void setConsigneePhone(String consigneePhone) {
                ConsigneePhone = consigneePhone;
            }

            public long getConsigneeProvinceId() {
                return ConsigneeProvinceId;
            }

            public void setConsigneeProvinceId(long consigneeProvinceId) {
                ConsigneeProvinceId = consigneeProvinceId;
            }

            public long getConsigneeCityId() {
                return ConsigneeCityId;
            }

            public void setConsigneeCityId(long consigneeCityId) {
                ConsigneeCityId = consigneeCityId;
            }

            public long getConsigneeDistrictId() {
                return ConsigneeDistrictId;
            }

            public void setConsigneeDistrictId(long consigneeDistrictId) {
                ConsigneeDistrictId = consigneeDistrictId;
            }

            public String getConsigneeProvinceName() {
                return ConsigneeProvinceName;
            }

            public void setConsigneeProvinceName(String consigneeProvinceName) {
                ConsigneeProvinceName = consigneeProvinceName;
            }

            public String getConsigneeCityName() {
                return ConsigneeCityName;
            }

            public void setConsigneeCityName(String consigneeCityName) {
                ConsigneeCityName = consigneeCityName;
            }

            public String getConsigneeDistrictName() {
                return ConsigneeDistrictName;
            }

            public void setConsigneeDistrictName(String consigneeDistrictName) {
                ConsigneeDistrictName = consigneeDistrictName;
            }

            public String getConsigneeAddress() {
                return ConsigneeAddress;
            }

            public void setConsigneeAddress(String consigneeAddress) {
                ConsigneeAddress = consigneeAddress;
            }
        }
    }


}
