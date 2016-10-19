package com.android.haobanyi.model.bean.post;

import java.util.List;

/**
 * Created by fWX228941 on 2016/9/5.
 *
 * @作者: 付敏
 * @创建日期：2016/09/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：提交订单
 */
public class Orders {
    public OrdersBean getOrders() {
        return Orders;
    }

    public void setOrders(OrdersBean orders) {
        Orders = orders;
    }

    OrdersBean Orders;
    public static class OrdersBean {

        public OrdersBean(long contactManageID, int payMode, String redID, int invoiceType, List<ShopsBean> shops) {
            ContactManageID = contactManageID;
            PayMode = payMode;
            RedID = redID;
            InvoiceType = invoiceType;
            Shops = shops;
        }

        public long getContactManageID() {
            return ContactManageID;
        }

        public void setContactManageID(long contactManageID) {
            ContactManageID = contactManageID;
        }

        public int getPayMode() {
            return PayMode;
        }

        public void setPayMode(int payMode) {
            PayMode = payMode;
        }

        public String getRedID() {
            return RedID;
        }

        public void setRedID(String redID) {
            RedID = redID;
        }

        public int getInvoiceType() {
            return InvoiceType;
        }

        public void setInvoiceType(int invoiceType) {
            InvoiceType = invoiceType;
        }

        public List<ShopsBean> getShops() {
            return Shops;
        }

        public void setShops(List<ShopsBean> shops) {
            Shops = shops;
        }

        long ContactManageID;//联系人
        int PayMode;//支付方式:1在线支付,2 上门收款
        String RedID;//平台红包ID
        int InvoiceType;// 1个人，2增值，9不需要发票,默认是不需要的
        List<ShopsBean> Shops;
        public static class ShopsBean {

            public ShopsBean(long shopID, String message, String vouchersID) {
                ShopID = shopID;
                Message = message;
                VouchersID = vouchersID;
            }
            public ShopsBean(long shopID) {
                ShopID = shopID;
            }

            public long getShopID() {
                return ShopID;
            }

            public void setShopID(long shopID) {
                ShopID = shopID;
            }

            public String getMessage() {
                return Message;
            }

            public void setMessage(String message) {
                Message = message;
            }

            public String getVouchersID() {
                return VouchersID;
            }

            public void setVouchersID(String vouchersID) {
                VouchersID = vouchersID;
            }

            long ShopID;
            String Message;
            String VouchersID;	//代金券ID
        }
    }
}
