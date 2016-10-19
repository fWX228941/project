package com.android.haobanyi.model.bean.post;

import java.util.List;

/**
 * Created by fWX228941 on 2016/9/5.
 *
 * @作者: 付敏
 * @创建日期：2016/09/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：GetVerifyOrderInfo 【确认订单获取订单信息】
 */
public class Vouchers {

    public List<Vouchersbean> getVouchers() {
        return Vouchers;
    }

    public void setVouchers(List<Vouchersbean> vouchers) {
        Vouchers = vouchers;
    }

    List<Vouchersbean> Vouchers;// 添加到购物车


    public static class Vouchersbean {
        public Vouchersbean(long shopID, long vouchersID) {
            VouchersID = vouchersID;
            ShopID = shopID;
        }

        public long getVouchersID() {
            return VouchersID;
        }

        public void setVouchersID(long vouchersID) {
            VouchersID = vouchersID;
        }

        public long getShopID() {
            return ShopID;
        }

        public void setShopID(long shopID) {
            ShopID = shopID;
        }

        long VouchersID;
        long ShopID;
    }
}
